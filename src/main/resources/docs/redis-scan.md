# Redis SCAN 方法说明

## 方法位置

`RedisUtil.scanKeys(String pattern, long count)` 用于按匹配规则扫描 Redis 中的 key，当前主要用于阅读历史异步落库场景，例如扫描所有阅读历史缓存：

```java
redisUtil.scanKeys(CacheConstants.READING_HISTORY + "*", 100);
```

对应扫描的 key 形如：

```text
novel:reading:history:{userId}
```

## 为什么使用 SCAN

Redis 有两种常见的 key 查询方式：

```text
KEYS pattern
SCAN cursor MATCH pattern COUNT count
```

`KEYS` 会一次性遍历 Redis 中所有 key，数据量大时可能阻塞 Redis，不适合在线服务。

`SCAN` 是游标式、分批扫描。它不会一次性把所有 key 全部查出来，适合定时任务、后台同步这类场景。

## 方法代码

```java
public Set<String> scanKeys(String pattern, long count) {
    return redisTemplate.execute(
            (RedisCallback<Set<String>>) connection -> {
                Set<String> keys = new HashSet<>();
                ScanOptions options = ScanOptions.scanOptions()
                        .match(pattern)
                        .count(count)
                        .build();

                try (Cursor<byte[]> cursor = connection.scan(options)) {
                    while (cursor.hasNext()) {
                        keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
                    }
                }

                return keys;
            }
    );
}
```

## 参数说明

### pattern

`pattern` 是 key 匹配规则。

示例：

```java
CacheConstants.READING_HISTORY + "*"
```

如果：

```java
CacheConstants.READING_HISTORY = "novel:reading:history:"
```

那么最终匹配规则是：

```text
novel:reading:history:*
```

可以匹配：

```text
novel:reading:history:1001
novel:reading:history:1002
novel:reading:history:1003
```

### count

`count` 是每次扫描时给 Redis 的数量提示。

例如：

```java
redisUtil.scanKeys("novel:reading:history:*", 100);
```

表示每轮扫描大约取 100 个 key。

注意：`count` 不是最终最多返回 100 个 key，而是 Redis 每次扫描的数量建议。Redis 会根据游标继续扫描，直到扫描结束。

## 执行流程

### 1. 调用 redisTemplate.execute

```java
return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
    ...
});
```

`StringRedisTemplate` 常用的 `opsForValue()`、`opsForHash()` 封装了简单操作。

`SCAN` 属于偏底层的 Redis 连接操作，所以这里通过 `redisTemplate.execute()` 拿到 `RedisConnection` 来执行。

### 2. 创建结果集合

```java
Set<String> keys = new HashSet<>();
```

用 `Set` 保存扫描到的 key，避免重复。

Redis `SCAN` 在数据变化时可能出现重复 key，用 `Set` 可以自然去重。

### 3. 构建扫描参数

```java
ScanOptions options = ScanOptions.scanOptions()
        .match(pattern)
        .count(count)
        .build();
```

含义：

```text
match(pattern) -> 只扫描符合 pattern 的 key
count(count)   -> 每次扫描的数量提示
build()        -> 构建 ScanOptions 对象
```

例如：

```java
.match("novel:reading:history:*")
.count(100)
```

相当于 Redis 命令：

```text
SCAN cursor MATCH novel:reading:history:* COUNT 100
```

### 4. 执行扫描并获取游标

```java
try (Cursor<byte[]> cursor = connection.scan(options)) {
    ...
}
```

`connection.scan(options)` 会返回一个 `Cursor<byte[]>`。

`Cursor` 可以理解为 Redis 扫描结果的迭代器。代码通过它一批一批读取 key。

这里使用 `try (...)` 是为了自动关闭游标，避免连接资源没有释放。

### 5. 遍历扫描结果

```java
while (cursor.hasNext()) {
    keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
}
```

`cursor.next()` 返回的是 `byte[]`。

因为 Redis 底层返回的是字节数组，所以需要转成字符串：

```java
new String(cursor.next(), StandardCharsets.UTF_8)
```

然后放入 `keys` 集合。

### 6. 返回所有匹配到的 key

```java
return keys;
```

最终返回匹配到的 key 集合。

## 阅读历史异步落库中的用法

定时任务可以这样使用：

```java
Set<String> keys = redisUtil.scanKeys(CacheConstants.READING_HISTORY + "*", 100);
if (keys == null || keys.isEmpty()) {
    return;
}

for (String key : keys) {
    Map<Object, Object> entries = redisUtil.getHashEntries(key);
    if (entries == null || entries.isEmpty()) {
        continue;
    }

    for (Object value : entries.values()) {
        if (value == null) {
            continue;
        }

        ReadingHistory readingHistory = JSONUtil.toBean(value.toString(), ReadingHistory.class);
        readingHistoryMapper.upsert(readingHistory);
    }
}
```

流程：

```text
1. 扫描 novel:reading:history:*，拿到所有用户阅读历史缓存 key
2. 对每个 key 执行 HGETALL，拿到该用户所有小说阅读历史
3. 将 Hash value 中的 JSON 字符串转成 ReadingHistory 对象
4. 使用 upsert 写入 MySQL
```

## 注意事项

### SCAN 不保证强一致快照

`SCAN` 扫描期间，如果 Redis 中的 key 正在新增、删除或过期，扫描结果可能不是某一时刻的完整快照。

阅读历史允许短暂延迟和最终一致，所以可以接受。

### SCAN 可能返回重复 key

Redis 官方说明中，`SCAN` 在某些情况下可能返回重复元素。

当前方法用 `HashSet` 保存 key，可以去重。

### count 不是限制总数

`count` 是每次扫描数量的提示，不是总返回数量限制。

如果 Redis 中匹配到 10000 个 key，调用：

```java
scanKeys("novel:reading:history:*", 100)
```

最终仍可能返回全部 10000 个 key，只是内部分批扫描。

### 当前方法会把结果收集到内存

虽然使用了 `SCAN`，但当前实现最后还是把所有匹配 key 放进 `Set` 返回。

对于当前项目和阅读历史场景，这样足够简单。

如果数据量非常大，可以后续优化为边扫描边处理，不一次性返回所有 key。

## 当前代码需要注意

当前 `RedisUtil.scanKeys` 中这一行需要以分号结尾：

```java
keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
```

否则 Java 编译会失败。

package com.fengling.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.context.AuthUserInfo;
import com.fengling.common.context.UserContext;
import com.fengling.common.util.JWTUtil;
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.UserInfo;
import com.fengling.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class OptionalAuthInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行跨域预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            return true;
        }

        if (!token.startsWith("Bearer ")) {
            return true;
        }
        token = token.substring(7);
        if (token.isBlank()) {
            return true;
        }
        if (!jwtUtil.validateJwtToken(token)) {
            return true;
        }
        AuthUserInfo authUserInfo = jwtUtil.parseJWT(token);
        if (authUserInfo == null) {
            return true;
        }
        Long userId = authUserInfo.getUserId();
        String redisToken = redisUtil.getValueForKey(CacheConstants.AUTH_TOKEN + userId);
        if (redisToken == null || redisToken.isBlank() || !token.equals(redisToken)) {
            return true;
        }

        UserInfo userInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .select(UserInfo::getUserStatus)
                        .eq(UserInfo::getId, userId)
        );

        if (userInfo == null) {
            return true;
        }

        if (!CommonConstants.USER_STATUS_NORMAL.equals(userInfo.getUserStatus())) {
            redisUtil.deleteKey(CacheConstants.AUTH_TOKEN + userId);
            return true;
        }

        UserContext.setAuthUserInfo(authUserInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.remove();
    }
}

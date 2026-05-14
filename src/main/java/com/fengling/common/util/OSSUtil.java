package com.fengling.common.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class OSSUtil {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.url-prefix}")
    private String urlPrefix;

    /**
     * 图片上传
     *
     * @param file     文件
     * @param pathName 路径名
     * @return 图片链接名
     */
    public String upload(MultipartFile file, String pathName) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "文件上传失败");
        }
        if (pathName == null || pathName.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "路径名为空");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ResultCodeEnum.FAIL, "只能上传图片");
        }
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new BusinessException(ResultCodeEnum.FAIL, "文件上传失败");
        }
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."))
                .toLowerCase();
        if (!CommonConstants.IMAGE_JPG.equals(suffix) &&
                !CommonConstants.IMAGE_JPEG.equals(suffix) &&
                !CommonConstants.IMAGE_PNG.equals(suffix) &&
                !CommonConstants.IMAGE_WEBP.equals(suffix)
        ) {
            throw new BusinessException(ResultCodeEnum.FAIL, "图片格式不支持");
        }
        String objectName = pathName + "/" + UUID.randomUUID() + suffix;
        OSS ossClient = new OSSClientBuilder().build(
                endpoint,
                accessKeyId,
                accessKeySecret
        );
        try {
            ossClient.putObject(bucketName, objectName, file.getInputStream());
            ossClient.setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead);
        } catch (IOException e) {
            throw new BusinessException(ResultCodeEnum.FAIL, "文件上传失败");
        } finally {
            ossClient.shutdown();
        }

        return urlPrefix + "/" + objectName;
    }

    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }
        String prefix = urlPrefix + "/";
        if (!fileUrl.startsWith(prefix)) {
            return;
        }

        String objectName = fileUrl.substring(prefix.length());
        OSS ossClient = new OSSClientBuilder().build(
                endpoint,
                accessKeyId,
                accessKeySecret
        );
        try {
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException | ClientException e) {
            log.warn("删除OSS文件失败 --> fileUrl: {}", fileUrl, e);
        } finally {
            ossClient.shutdown();
        }
    }
}

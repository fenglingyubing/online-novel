package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户头像上传响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUploadPhotoRespDto {
    /**
     * 图片链接
     */
    private String coverUrl;
}

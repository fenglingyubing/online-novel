package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUploadPhotoRespDto {
    /**
     * 图片链接
     */
    private String coverUrl;
}

package com.fengling.entity.dto;

import com.fengling.common.dto.PageReqDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRecommendReqDto extends PageReqDto {

    /**
     * 推荐类型
     */
    private Integer recommendType;
}

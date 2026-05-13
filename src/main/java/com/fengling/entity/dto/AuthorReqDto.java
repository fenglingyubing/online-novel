package com.fengling.entity.dto;

import lombok.Data;

@Data
public class AuthorReqDto {
    private String username;
    private String password;
    private String authorName;
}

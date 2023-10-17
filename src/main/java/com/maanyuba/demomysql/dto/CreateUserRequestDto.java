package com.maanyuba.demomysql.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String user;
    private String password;
}

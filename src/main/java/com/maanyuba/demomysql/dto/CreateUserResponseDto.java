package com.maanyuba.demomysql.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserResponseDto {

    private String user;
    private boolean blocked;
}

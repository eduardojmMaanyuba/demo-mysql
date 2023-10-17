package com.maanyuba.demomysql.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponse {

    private int statusCode;
    private String message;
    private Object object;
}

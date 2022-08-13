package com.Lycle.Server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicResponse {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private int count;
    private List<Object> result;
    private String token;
}

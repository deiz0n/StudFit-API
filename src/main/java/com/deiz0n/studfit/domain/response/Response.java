package com.deiz0n.studfit.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {

    private Integer code;
    private HttpStatus status;
    private String path;
    private T data;

}

package com.deiz0n.studfit.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseRequest<T> {

    private Integer code;
    private HttpStatus status;
    private String path;
    private T data;

}

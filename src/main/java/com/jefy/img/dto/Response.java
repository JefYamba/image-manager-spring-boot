package com.jefy.img.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 27/04/2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Response<T> {
    protected LocalDateTime timeStamp;
    protected HttpStatus status;
    protected int statusCode;
    protected String message;
    protected T data;
}

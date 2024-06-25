package com.crud.company.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDetail {
    private LocalDateTime timestamp;
    private String message;
}

package com.crud.company.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessValidationException extends RuntimeException{
    private final String message;
}

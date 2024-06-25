package com.emp.management.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final String message;
}

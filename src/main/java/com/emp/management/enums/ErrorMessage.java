package com.emp.management.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    AnotherRecordWithTheSameNameExist("Another record with the same name exist"),
    RecordDoesNotExist("Record doesn't exist"),
    CannotModifyReadOnlyRecord("Cannot modify read-only record"),
    CannotDeleteReadOnlyRecord("Cannot delete read-only record"),
    ;

    private final String message;
    ErrorMessage(String message) {
        this.message = message;
    }

}

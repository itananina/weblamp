package com.itananina.weblamp.weblamp.exceptions;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


public class ValidationException extends RuntimeException {

    @Getter
    private List<String> errorFieldsMessages;

    public ValidationException(List<String> errorFieldsMessages) {
        super(errorFieldsMessages.stream().collect(Collectors.joining(", ")));
        this.errorFieldsMessages = errorFieldsMessages;
    }
}

package com.backend.Exception;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final String field;
    private final Object fieldValue;

    public ResourceNotFoundException(String resource, String field, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resource, field, fieldValue));
        this.resource = resource;
        this.field = field;
        this.fieldValue = fieldValue;
    }

}
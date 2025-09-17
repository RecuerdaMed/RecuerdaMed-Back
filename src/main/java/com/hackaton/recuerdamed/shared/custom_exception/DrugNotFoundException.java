package com.hackaton.recuerdamed.shared.exception.custom_exception;

public class DrugNotFoundException extends RuntimeException {
    public DrugNotFoundException(String message) {
        super(message);
    }
}
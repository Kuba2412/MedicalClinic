package com.Kuba2412.MedicalClinic.handler.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MedicalExcpetion {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
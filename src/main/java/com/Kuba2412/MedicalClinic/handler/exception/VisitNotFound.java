package com.Kuba2412.MedicalClinic.handler.exception;

import org.springframework.http.HttpStatus;

public class VisitNotFound extends MedicalExcpetion {
    public VisitNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

package com.Kuba2412.MedicalClinic.handler.exception;

import org.springframework.http.HttpStatus;

public class InstitutionNotFound extends MedicalExcpetion {
    public InstitutionNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

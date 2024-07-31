package com.Kuba2412.MedicalClinic.handler.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalExcpetion {
    public DoctorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
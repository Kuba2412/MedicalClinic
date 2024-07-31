package com.Kuba2412.MedicalClinic.handler.exception;

import org.springframework.http.HttpStatus;

public class PatientNotFound extends MedicalExcpetion {
    public PatientNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
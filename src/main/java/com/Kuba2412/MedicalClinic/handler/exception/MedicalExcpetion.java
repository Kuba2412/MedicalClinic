package com.Kuba2412.MedicalClinic.handler.exception;

import org.springframework.http.HttpStatus;

public class MedicalExcpetion extends RuntimeException {
    private HttpStatus httpStatus;

    public MedicalExcpetion(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
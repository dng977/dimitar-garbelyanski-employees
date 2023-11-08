package com.dimtar.garbelyanski.employees.exception;

public class BadCsvFormatException extends Exception{

    public BadCsvFormatException(String message) {
        super(message);
    }

    public BadCsvFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

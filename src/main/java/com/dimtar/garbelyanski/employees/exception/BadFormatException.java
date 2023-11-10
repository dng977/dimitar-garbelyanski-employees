package com.dimtar.garbelyanski.employees.exception;

public abstract class BadFormatException extends Exception {
    public BadFormatException(String message) {
        super(message);
    }

    public BadFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

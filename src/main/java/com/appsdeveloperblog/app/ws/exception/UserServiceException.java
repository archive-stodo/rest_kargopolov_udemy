package com.appsdeveloperblog.app.ws.exception;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = 123L;

    public UserServiceException(String message) {
        super(message);
    }
}

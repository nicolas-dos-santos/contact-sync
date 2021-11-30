package com.trio.contactsync.exceptions;

public class ProviderException extends Exception {

    public ProviderException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String getMessage() {
        if(this.getCause() == null){
            return super.getMessage();
        }
        return String.format("%s. Cause: %s", super.getMessage(), this.getCause().getMessage());
    }
}

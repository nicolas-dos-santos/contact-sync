package com.trio.contactsync.exceptions;

public class PublisherException  extends Exception  {

    public PublisherException(String message, Throwable cause){
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

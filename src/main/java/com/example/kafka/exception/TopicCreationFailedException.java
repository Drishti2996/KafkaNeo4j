package com.example.kafka.exception;

public class TopicCreationFailedException extends RuntimeException{

    public TopicCreationFailedException(final String message, final Throwable t) {
        super(message, t);
    }
}

package com.github.gitcommithelper.ai;

/**
 * Exception thrown when an AI provider encounters an error
 */
public class AIProviderException extends Exception {

    public AIProviderException(String message) {
        super(message);
    }

    public AIProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}

package org.flatscrew.latte;

public class ProgramException extends RuntimeException {
    public ProgramException(Throwable error) {
        super(error);
    }

    public ProgramException(String message, Throwable error) {
        super(message, error);
    }
}

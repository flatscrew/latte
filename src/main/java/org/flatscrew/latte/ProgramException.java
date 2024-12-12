package org.flatscrew.latte;

public class ProgramException extends RuntimeException {
    public ProgramException(Throwable error) {
        super(error);
    }
}

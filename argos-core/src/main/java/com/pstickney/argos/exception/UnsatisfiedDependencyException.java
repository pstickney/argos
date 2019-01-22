package com.pstickney.argos.exception;

public class UnsatisfiedDependencyException extends RuntimeException
{
    public UnsatisfiedDependencyException() {
        super();
    }

    public UnsatisfiedDependencyException(String s) {
        super(s);
    }

    public UnsatisfiedDependencyException(String s, Throwable e) {
        super(s, e);
    }
}

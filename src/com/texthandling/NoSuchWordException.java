package com.texthandling;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class NoSuchWordException extends RuntimeException {

    public NoSuchWordException(String message) {
        super(message);
    }
}

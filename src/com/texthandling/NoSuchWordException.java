package com.texthandling;

import java.io.Serializable;

/**
 * Created by Ильнар on 22.04.2015.
 */
public class NoSuchWordException extends RuntimeException implements Serializable {

    public NoSuchWordException(String message) {
        super(message);
    }
}

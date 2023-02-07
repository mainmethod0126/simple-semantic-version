package io.github.mainmethod0126.gradle.simple.versioning.exception;

import java.io.IOException;

public class UnwritableFileException extends IOException {

    public UnwritableFileException() {
    }

    public UnwritableFileException(String message) {
        super(message);
    }

    public UnwritableFileException(Throwable cause) {
        super(cause);
    }

    public UnwritableFileException(String message, Throwable cause) {
        super(message, cause);
    }

}

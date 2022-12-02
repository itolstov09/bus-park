package dev.tolstov.buspark.exception;

public abstract class BusParkBaseException extends RuntimeException {
    public BusParkBaseException(String message) {
        super(message);
    }
}

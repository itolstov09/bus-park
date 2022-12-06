package dev.tolstov.buspark.exception;

public class JsonServiceException extends RuntimeException {
    public JsonServiceException(String message) {
        super("Cannot save data to DB from JSON: " + message);
    }
}

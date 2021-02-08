package de.ckthomas.smarthome.exceptions;

/**
 * @author Christian Thomas
 */
public class HassioException extends RuntimeException {
    public HassioException(String message) {
        super(message);
    }

    public HassioException(String message, Throwable cause) {
        super(message, cause);
    }
}

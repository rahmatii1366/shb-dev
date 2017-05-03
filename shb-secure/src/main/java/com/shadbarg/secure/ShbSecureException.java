package com.shadbarg.secure;

/**
 * @author Mohammad Rahmati, 4/15/2017 7:10 AM
 */
public class ShbSecureException
        extends Exception {
    private static final String EXCEPTION_CLASS_NAME =
            "shb-secure";

    public ShbSecureException() {
        super(EXCEPTION_CLASS_NAME
                .concat(":")
                .concat("unknown message"));
    }

    public ShbSecureException(String message) {
        super(EXCEPTION_CLASS_NAME
                .concat(":").concat(message));
    }

    public ShbSecureException(Exception exception) {
        super(EXCEPTION_CLASS_NAME
                .concat(":")
                .concat(exception.getMessage()));
    }
}

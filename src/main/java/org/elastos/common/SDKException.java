package org.elastos.common;

public class SDKException extends Exception {

    private static final long serialVersionUID = -3056715808373341597L;

    public SDKException(String message) {
        super(message);
        initExMsg(message);
    }
    public SDKException(String message, Throwable ex) {
        super(message, ex);
        initExMsg(message);
    }
    public SDKException(Throwable ex) {
        super(ex);
    }

    private void initExMsg(String message) {
    }
}

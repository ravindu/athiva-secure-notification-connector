package com.athiva.notification.secureemailconnector.domain.common;
/**
 * 
 * @author ravindu.s
 *
 */
public class SecureEmailConnectorTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SecureEmailConnectorTimeoutException(String message) {
        super(message);
    }

    public SecureEmailConnectorTimeoutException(String message, Throwable e) {
        super(message, e);
    }

    public SecureEmailConnectorTimeoutException(Throwable e) {
        super(e);
    }
}

package com.athiva.notification.secureemailconnector.domain.common;
/**
 * 
 * @author ravindu.s
 *
 */
public enum SecureEmailResponseCode {

    SUCCESS("Email send successfull"), AUTHENTICATION_FAILED("Email Authentication Failed"), UNSUPPORTED_ENCODING_TYPE(
            "Unsupported Encoding Type"), MESSAGING_ERROR_OCCOURED("MessagingException Occoured"), SEND_FAILED(
            "Email Send Failed");

    private String name;

    private SecureEmailResponseCode(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }
}

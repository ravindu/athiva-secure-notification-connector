package com.athiva.notification.secureemailconnector.domain.message;
/**
 * 
 * @author ravindu.s
 *
 */
public interface SecureEmailService {

    public SecureEmailResponse sendEmail(SecureEmailRequest emailRequest);
}

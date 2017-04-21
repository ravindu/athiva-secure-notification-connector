package com.athiva.notification.secureemailconnector.infrastructure.configuration;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Configure the email configurations
 * @author ravindu.s
 *
 */
@Configuration
public class JavamailConfiguration {

    @Value("${email.configuration.transportprotocol}")
    private String emailTransportProtocol;

    @Value("${email.configuration.smtp.auth}")
    private String emailAuthEnableStatus;

    @Value("${email.configuration.smtp.host}")
    private String emailHostValue;

    @Value("${email.configuration.smtp.port}")
    private String emailPortValue;

    @Value("${email.configuration.smtp.starttls}")
    private String emailStartTlsStatus;

    @Value("${email.configuration.smtp.connectiontimeout}")
    private Integer emailConnectionTimeout;

    @Value("${email.configuration.smtp.timeout}")
    private Integer emailTimeout;

    @Value("${email.configuration.textencodetype}")
    private String emailTextEncodeType;

    @Value("${email.configuration.emailaccount.username}")
    private String emailAccount;

    @Value("${email.configuration.emailaccount.password}")
    private String emailAccountPassword;

    private static final String EMAIL_TRANSPORT_PROTOCOL_KEY = "mail.transport.protocol";

    private static final String EMAIL_SMTP_AUTH_KEY = "mail.smtp.auth";

    private static final String EMAIL_SMTP_HOST_KEY = "mail.smtp.host";

    private static final String EMAIL_SMTP_PORT_KEY = "mail.smtp.port";

    private static final String EMAIL_SMTP_STARTTLS_KEY = "mail.smtp.starttls.enable";

    private static final String EMAIL_SMTP_CONNETIONTIMEOUT_KEY = "mail.smtp.connectiontimeout";

    private static final String EMAIL_SMTP_TIMEOUT_KEY = "mail.smtp.timeout";

    @Bean
    public Session session() {

        Properties emailProperties = new Properties();
        emailProperties.put(EMAIL_TRANSPORT_PROTOCOL_KEY, emailTransportProtocol);
        emailProperties.put(EMAIL_SMTP_AUTH_KEY, emailAuthEnableStatus);
        emailProperties.put(EMAIL_SMTP_HOST_KEY, emailHostValue);
        emailProperties.put(EMAIL_SMTP_PORT_KEY, emailPortValue);
        emailProperties.put(EMAIL_SMTP_STARTTLS_KEY, emailStartTlsStatus);
        emailProperties.put(EMAIL_SMTP_CONNETIONTIMEOUT_KEY, emailConnectionTimeout);
        emailProperties.put(EMAIL_SMTP_TIMEOUT_KEY, emailTimeout);

        Session session = Session.getDefaultInstance(emailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailAccountPassword);
            }
        });

        return session;
    }

}

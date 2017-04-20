package com.athiva.notification.secureemailconnector.infrastructure.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.athiva.notification.secureemailconnector.application.common.ConnectorUtil;
import com.athiva.notification.secureemailconnector.domain.attachment.Attachment;
import com.athiva.notification.secureemailconnector.domain.common.MessageEncodeType;
import com.athiva.notification.secureemailconnector.domain.common.SecureEmailConnectorTimeoutException;
import com.athiva.notification.secureemailconnector.domain.common.SecureEmailResponseCode;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailRequest;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailResponse;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailService;
/**
 * 
 * @author ravindu.s
 *
 */
@Service
public class SecureEmailServiceImpl implements SecureEmailService {

    @Autowired
    public Session session;

    @Value("${email.configuration.textencodetype}")
    private String textEncodeType;

    @Value("${email.configuration.noofretryattempts}")
    private Integer maxNoOfRetryAttempts;

    private static final String HEADER_MESSAGE_ID_KEY = "Message-ID";

    private static final String HEADER_CONTENT_ENCODING_KEY = "Content-Transfer-Encoding";

    private static final String MULTIPART_RELATED = "related";

    private static final String MULTIPART_ALTERNATIVE = "alternative";

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public SecureEmailResponse sendEmail(final SecureEmailRequest emailRequest) {

        SecureEmailResponse secureEmailResponse = null;
        String messageId = null;
        String thirdPartyExecutionTime = null;
        long executionStartTime = 0;
        long executionEndTime = 0;

        try {
            // create MimeMessage
            MimeMessage mimeMessage = BuildMimeMessage(emailRequest);

            messageId = emailRequest.getMessageId();
            logger.info("Sending email to recipient. [Email Id]-" + messageId);

            executionStartTime = System.nanoTime();

            // send MimeMessage
            Transport.send(mimeMessage);

            executionEndTime = System.nanoTime();
            logger.info("Email sent successfully. [Email Id]-" + messageId);

            thirdPartyExecutionTime = ConnectorUtil.calculateExcutionTimeInMilies(executionStartTime, executionEndTime);

            secureEmailResponse = buildSecureEmailResponse(SecureEmailResponseCode.SUCCESS, null);
            secureEmailResponse.setThirdPartyExcutionTime(thirdPartyExecutionTime);

        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            logger.error("Unsupported text encoding type found. [Email Id]-" + messageId, unsupportedEncodingException);
            throw new RuntimeException("Unsupported encoding type found.", unsupportedEncodingException);

        } catch (AddressException adressException) {
            logger.error("Unsupported email address type found. [Email Id]-" + messageId, adressException);
            throw new IllegalArgumentException("Unsupported email address found.", adressException);

        } catch (AuthenticationFailedException authenticationFailedException) {
            logger.error("Authentication failed while authorizing the email account. [Email Id]-" + messageId,
                    authenticationFailedException);
            secureEmailResponse = buildSecureEmailResponse(SecureEmailResponseCode.AUTHENTICATION_FAILED,
                    authenticationFailedException.getMessage());

        } catch (SendFailedException sendFailedException) {
            logger.error("Email send failed due to unresponsive secure email service. [Email Id]-" + messageId,
                    sendFailedException);

            if (!ArrayUtils.isEmpty(sendFailedException.getValidUnsentAddresses())) {

                logger.info("Prepare to resend the unsent email");
                secureEmailResponse = buildEmailResponseIfUnsentAddresesFound(sendFailedException, emailRequest);

            } else if (!ArrayUtils.isEmpty(sendFailedException.getInvalidAddresses())) {
                secureEmailResponse = buildEmailResponseIfInvalidAddresesFound(sendFailedException);

            } else {
                secureEmailResponse = buildSecureEmailResponse(SecureEmailResponseCode.SEND_FAILED,
                        sendFailedException.getMessage());
            }

        } catch (MessagingException messagingException) {
            logger.error("Error occoured while sending email. MessageId-" + messageId, messagingException);
            throw new SecureEmailConnectorTimeoutException("Messaging error occoured while sending email.",
                    messagingException);
        }

        return secureEmailResponse;
    }

    protected MimeMessage BuildMimeMessage(final SecureEmailRequest emailRequest) throws MessagingException,
            UnsupportedEncodingException, AddressException {
        MimeMessage mimeMessage = new MimeMessage(session);

        // Build MimeMessage Multipart
        Multipart parentMultiPartWithTextAndAttachment = buildMimeMessageMultipart(emailRequest, mimeMessage);

        // set MultiPart to MimeMessage
        mimeMessage.setContent(parentMultiPartWithTextAndAttachment);

        // save changes
        mimeMessage.saveChanges();
        return mimeMessage;
    }

    protected void setSubjectAndDateToMimeMessage(final SecureEmailRequest emailRequest, MimeMessage mimeMessage)
            throws MessagingException {
        mimeMessage.setSubject(emailRequest.getMessageSubject());
        mimeMessage.setSentDate(new Date());
    }

    protected Multipart buildMimeMessageMultipart(final SecureEmailRequest emailRequest, final MimeMessage mimeMessage)
            throws MessagingException, UnsupportedEncodingException, AddressException {

        // set headers
        setMimeMessageHeaders(emailRequest.getMessageId(), mimeMessage);

        // set recipients
        setMimeMessageRecipients(emailRequest, mimeMessage);

        // Set Date and Subject
        setSubjectAndDateToMimeMessage(emailRequest, mimeMessage);

        // set text/html to MultiPart
        Multipart textHtmlMultiPart = addTextHtmlMultipart(emailRequest.getMessageEncodeType(),
                emailRequest.getMessage());

        // set text/html MultiPart to MimeBodyPart
        MimeBodyPart textHtmlBodyContendPart = addMimeBodyPartWithTextHtmlContend(textHtmlMultiPart);

        // create new MultiPart and set derive MultiPart base MultiPart
        Multipart parentMultiPartWithTextAndAttachment = addMultiPartWithBothTextAndAttachment(textHtmlBodyContendPart,
                emailRequest);
        return parentMultiPartWithTextAndAttachment;
    }

    private void setMimeMessageHeaders(final String messageId, final MimeMessage mimeMessage)
            throws MessagingException, UnsupportedEncodingException {
        mimeMessage.setHeader(HEADER_MESSAGE_ID_KEY, messageId);
        mimeMessage.setHeader(HEADER_CONTENT_ENCODING_KEY, MimeUtility.encodeText(textEncodeType));
    }

    private void setMimeMessageRecipients(final SecureEmailRequest emailRequest, final MimeMessage mimeMessage)
            throws MessagingException, AddressException {
        mimeMessage.setFrom(new InternetAddress(emailRequest.getMessageFrom()));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRequest.getMessageTo()));
        if (StringUtils.isNotBlank(emailRequest.getMessageCCTo())) {
            mimeMessage.setRecipient(Message.RecipientType.CC, new InternetAddress(emailRequest.getMessageCCTo()));
        }

    }

    private Multipart addTextHtmlMultipart(final MessageEncodeType messageEncodeType, final String message_body)
            throws MessagingException {

        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(message_body, messageEncodeType.getValue());
        bodyPart.setDisposition(BodyPart.INLINE);

        Multipart textHtmlMultiPart = new MimeMultipart(MULTIPART_ALTERNATIVE);
        textHtmlMultiPart.addBodyPart(bodyPart);

        return textHtmlMultiPart;

    }

    private MimeBodyPart addMimeBodyPartWithTextHtmlContend(final Multipart textHtmlMultiPart)
            throws MessagingException {
        MimeBodyPart textHtmlBodyContendPart = new MimeBodyPart();
        textHtmlBodyContendPart.setContent(textHtmlMultiPart);
        return textHtmlBodyContendPart;
    }

    private Multipart addMultiPartWithBothTextAndAttachment(final MimeBodyPart textHtmlBodyContendPart,
            final SecureEmailRequest emailRequest) throws MessagingException {

        Multipart parentMultiPartWithTextAndAttachment = new MimeMultipart(MULTIPART_RELATED);
        parentMultiPartWithTextAndAttachment.addBodyPart(textHtmlBodyContendPart);

        if (CollectionUtils.isNotEmpty(emailRequest.getAttachmentFilesByteArray())) {

            for (Attachment attachmentFile : emailRequest.getAttachmentFilesByteArray()) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                String filename = attachmentFile.getFileName();
                DataSource source = new ByteArrayDataSource(attachmentFile.getData(),
                        attachmentFile.getAttachmentEncodeType());
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(filename);

                parentMultiPartWithTextAndAttachment.addBodyPart(attachmentBodyPart);
            }
        }

        return parentMultiPartWithTextAndAttachment;
    }

    private SecureEmailResponse buildSecureEmailResponse(final SecureEmailResponseCode responseCode,
            final String rawOutput) {

        SecureEmailResponse secureEmailResponse = new SecureEmailResponse(responseCode, responseCode.getValue());
        if (StringUtils.isNotBlank(rawOutput)) {
            secureEmailResponse.setRawOutput(rawOutput);
        }

        return secureEmailResponse;
    }

    private SecureEmailResponse buildEmailResponseIfInvalidAddresesFound(final SendFailedException sendFailedException) {

        Address[] invalidAddresses = sendFailedException.getInvalidAddresses();
        SecureEmailResponse secureEmailResponse = new SecureEmailResponse(SecureEmailResponseCode.SEND_FAILED,
                "Invalid email address found. [Email address]-" + invalidAddresses.toString());
        secureEmailResponse.setRawOutput(sendFailedException.getMessage());
        return secureEmailResponse;
    }

    private SecureEmailResponse buildEmailResponseIfUnsentAddresesFound(final SendFailedException sendFailedException,
            final SecureEmailRequest emailRequest) {

        SecureEmailResponse secureEmailResponse = null;
        String unsentEmailAddress = retrieveUnsentEmailAddress(sendFailedException.getValidUnsentAddresses());

        if (StringUtils.isNotBlank(unsentEmailAddress)) {

            int noOfAttempt = 0;
            while (secureEmailResponse == null && noOfAttempt < maxNoOfRetryAttempts) {

                try {
                    ++noOfAttempt;
                    emailRequest.setMessageTo(unsentEmailAddress);
                    MimeMessage mimeMessage = BuildMimeMessage(emailRequest);

                    logger.info("Resending email to the recipient. [Email Id]-" + emailRequest.getMessageId());

                    Transport.send(mimeMessage);

                    logger.info("Email resent successful. [Email Id]-" + emailRequest.getMessageId());

                    secureEmailResponse = buildSecureEmailResponse(SecureEmailResponseCode.SUCCESS, null);
                    break;

                } catch (Exception e) {
                    logger.error("Error occoured while resending the email.No of attempts tried-" + noOfAttempt
                            + " Prepare to re-attemp the email sending.");
                }
            }

            if (secureEmailResponse == null) {
                secureEmailResponse = buildSecureEmailResponse(SecureEmailResponseCode.SEND_FAILED,
                        "Email send failed due to invalid email account. [Email address]-" + unsentEmailAddress);
            }

        } else {
            secureEmailResponse = buildSecureEmailResponse(SecureEmailResponseCode.SEND_FAILED,
                    sendFailedException.getMessage());
        }

        return secureEmailResponse;
    }

    private String retrieveUnsentEmailAddress(final Address[] validUnsentAddresses) {

        String unsentEmailAddress = null;

        for (Address unsentAddress : validUnsentAddresses) {
            unsentEmailAddress = unsentAddress.toString();
            break;

        }
        return unsentEmailAddress;
    }

}

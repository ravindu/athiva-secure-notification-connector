package com.athiva.notification.secureemailconnector.application.transformer;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.athiva.notification.connector.service.MessageRequest;
import com.athiva.notification.secureemailconnector.application.common.AssertionUtil;
import com.athiva.notification.secureemailconnector.domain.attachment.Attachment;
import com.athiva.notification.secureemailconnector.domain.common.MessageEncodeType;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailRequest;

/**
 * 
 * @author ravindu.s
 * 
 */
@Component
public class MessageRequestTransformer {

    @Value("${email.configuration.emailaccount.username}")
    private String emailFrom;

    public SecureEmailRequest transformToSecureEmailRequest(MessageRequest messageRequest) {

        SecureEmailRequest secureEmailRequest = new SecureEmailRequest();
        secureEmailRequest.setMessageFrom(emailFrom);

        // TODO - MessageId should be passed from MessageRequest object
        String messageId = UUID.randomUUID().toString();
        AssertionUtil.validateMessageId(messageId);
        secureEmailRequest.setMessageId(messageId);

        String messageBody = messageRequest.getMessageBody();
        AssertionUtil.validateMessageBody(messageBody);
        secureEmailRequest.setMessage(messageBody);

        // TODO - Message subject should be passed from MessageRequest object
        String messageSubject = "Need to set subject from MessageRequest";
        AssertionUtil.validateMessageSubject(messageSubject);
        secureEmailRequest.setMessageSubject(messageSubject);

        AssertionUtil.validateRecepientList(messageRequest.getRecipients());
        String messageTo = messageRequest.getRecipients().get(0).getEmail();
        secureEmailRequest.setMessageTo(messageTo);

        // TODO - If email CC address available, it should be passed from MessageRequest object
        String messageCCTo = null;
        if (StringUtils.isNotBlank(messageCCTo)) {
            AssertionUtil.validateCCEmailAddress(messageCCTo);
            secureEmailRequest.setMessageCCTo(messageCCTo);
        }

        // TODO - Should be passed from MessageRequest object
        MessageEncodeType messageEncodeType = MessageEncodeType.TEXT_HTML;
        AssertionUtil.validateMessageEncodeType(messageEncodeType);
        secureEmailRequest.setMessageEncodeType(messageEncodeType);

        // TODO - Attachment list should get from MessageRequest
        List<Attachment> attachmentList = null;
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            AssertionUtil.validateAttachmentList(attachmentList);
            secureEmailRequest.setAttachmentFilesByteArray(attachmentList);
        }

        return secureEmailRequest;
    }
}

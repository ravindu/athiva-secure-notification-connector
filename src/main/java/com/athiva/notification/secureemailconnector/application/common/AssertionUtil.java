package com.athiva.notification.secureemailconnector.application.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

import com.athiva.notification.application.events.dto.RecipientType;
import com.athiva.notification.connector.service.MessageRequest;
import com.athiva.notification.secureemailconnector.domain.attachment.Attachment;
import com.athiva.notification.secureemailconnector.domain.common.MessageEncodeType;

/**
 * 
 * @author ravindu.s
 * 
 */
public class AssertionUtil {

    public static void validateMessageRequest(MessageRequest messageRequest) {

        Assert.notNull(messageRequest, "MessageRequest cannot be null");

    }

    public static void validateRecepientList(List<RecipientType> recipients) {
        Assert.isTrue(CollectionUtils.isNotEmpty(recipients), "Message recipient list cannot be null or empty");

        Assert.isTrue(recipients.size() == 1, "Multiple message recipients not allowed");

        RecipientType recipient = recipients.get(0);
        Assert.hasText(recipient.getEmail(), "Recipient.TO email address cannot be null or empty");

    }

    public static void validateAttachmentList(List<Attachment> attachmentList) {
        Assert.isTrue(CollectionUtils.isNotEmpty(attachmentList), "Message attachment list cannot be null or empty");

        Assert.isTrue(attachmentList.size() == 1, "Multiple message recipients not allowed");
        Attachment attachment = attachmentList.get(0);
        Assert.isTrue(!ArrayUtils.isEmpty(attachment.getData()), "Attachment cannot be null or empty");
        Assert.hasText(attachment.getFileName(), "Attachment file name cannot be null or empty");
        Assert.hasText(attachment.getAttachmentEncodeType(), "Attachment encode type cannot be null or empty");

    }

    public static void validateMessageId(String messageId) {
        Assert.hasText(messageId, "Message id cannot be null or empty");

    }

    public static void validateMessageSubject(String subjectId) {
        Assert.hasText(subjectId, "Message Subject cannot be null or empty");

    }

    public static void validateMessageBody(String message) {
        Assert.hasText(message, "Message body cannot be null or empty");

    }

    public static void validateCCEmailAddress(String ccEmailAddress) {
        Assert.hasText(ccEmailAddress, "Recipient.CC email address cannot be null or empty");

    }

    public static void validateMessageEncodeType(MessageEncodeType messageEncodeType) {
        Assert.notNull(messageEncodeType, "Message encoding type cannot be null");

    }

}

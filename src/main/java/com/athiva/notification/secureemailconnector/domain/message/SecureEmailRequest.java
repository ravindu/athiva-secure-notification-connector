package com.athiva.notification.secureemailconnector.domain.message;

import java.util.List;

import com.athiva.notification.secureemailconnector.domain.attachment.Attachment;
import com.athiva.notification.secureemailconnector.domain.common.MessageEncodeType;
/**
 * 
 * @author ravindu.s
 *
 */
public class SecureEmailRequest {

    /*** Email Message attributes ***/
    private String messageId;

    private String messageSubject;

    private MessageEncodeType messageEncodeType;

    private String message;

    /*** Email Recipient attribute ***/
    private String messageFrom;

    private String messageTo;

    private String messageCCTo;

    /*** Email Attachment attribute ***/
    private List<Attachment> attachmentFilesByteArray;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public MessageEncodeType getMessageEncodeType() {
        return messageEncodeType;
    }

    public void setMessageEncodeType(MessageEncodeType messageEncodeType) {
        this.messageEncodeType = messageEncodeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public List<Attachment> getAttachmentFilesByteArray() {
        return attachmentFilesByteArray;
    }

    public void setAttachmentFilesByteArray(List<Attachment> attachmentFilesByteArray) {
        this.attachmentFilesByteArray = attachmentFilesByteArray;
    }

    public String getMessageCCTo() {
        return messageCCTo;
    }

    public void setMessageCCTo(String messageCCTo) {
        this.messageCCTo = messageCCTo;
    }

}

package com.athiva.notification.secureemailconnector.domain.attachment;
/**
 * 
 * @author ravindu.s
 *
 */
public class Attachment {

    private String fileName;

    private byte[] data;

    private String attachmentEncodeType;

    public Attachment(String fileName, byte[] data, String attachmentEncodeType) {
        this.fileName = fileName;
        this.data = data;
        this.attachmentEncodeType = attachmentEncodeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getAttachmentEncodeType() {
        return attachmentEncodeType;
    }

    public void setAttachmentEncodeType(String attachmentEncodeType) {
        this.attachmentEncodeType = attachmentEncodeType;
    }

}

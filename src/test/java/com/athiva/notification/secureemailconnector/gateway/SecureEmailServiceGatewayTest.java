package com.athiva.notification.secureemailconnector.gateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athiva.notification.secureemailconnector.common.TestConstants;
import com.athiva.notification.secureemailconnector.domain.attachment.Attachment;
import com.athiva.notification.secureemailconnector.domain.common.MessageEncodeType;
import com.athiva.notification.secureemailconnector.domain.common.SecureEmailResponseCode;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailRequest;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailResponse;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailService;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-secure-notification-context.xml"})
public class SecureEmailServiceGatewayTest {

    @Autowired
    private SecureEmailService secureEmailService;

    @Test
    public void testEmailWithAttachment() throws IOException {

        SecureEmailRequest emailRequest = new SecureEmailRequest();
        emailRequest.setMessage(TestConstants.TEXT_MESSAGE);
        emailRequest.setAttachmentFilesByteArray(buildAttachmentList(TestConstants.ATTACHMENT_FILE_PATH));
        emailRequest.setMessageEncodeType(MessageEncodeType.TEXT_PLAIN);
        emailRequest.setMessageFrom(TestConstants.MESSAGE_FROM);
        emailRequest.setMessageId(UUID.randomUUID().toString());
        emailRequest.setMessageSubject(TestConstants.MESSAGE_SUBJECT);
        emailRequest.setMessageTo(TestConstants.MESSAGE_TO);

        SecureEmailResponse emailResponse = secureEmailService.sendEmail(emailRequest);

        Assert.assertNotNull("Email response cannot be null", emailResponse);
        Assert.assertEquals("Email response code should be SUCCESS", SecureEmailResponseCode.SUCCESS,
                emailResponse.getResponseCode());
        Assert.assertEquals("Email response description should be match with SecureEmailResponseCode description ", "Email send successfull",
                emailResponse.getResponseDescription());
        Assert.assertNull("RawOutput should be null", emailResponse.getRawOutput());

    }

    /**
     * Added a method to build byte array object until it passed by th notification framework
     * 
     * @throws IOException
     */
    public List<Attachment> buildAttachmentList(String filePath) throws IOException {

        InputStream in = new FileInputStream(new File(filePath));
        byte[] data = IOUtils.toByteArray(in);

        List<Attachment> attachmentList = new ArrayList<Attachment>();

        Attachment attachment = new Attachment(TestConstants.ATTACHMENT_NAME, data,
                TestConstants.ATTACHMENT_ENCODE_TYPE);
        attachmentList.add(attachment);

        return attachmentList;
    }
}

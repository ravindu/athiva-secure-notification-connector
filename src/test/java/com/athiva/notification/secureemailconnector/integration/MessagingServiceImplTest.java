package com.athiva.notification.secureemailconnector.integration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athiva.notification.application.common.AthivaNotificationStatus;
import com.athiva.notification.application.events.dto.RecipientType;
import com.athiva.notification.connector.service.ExternalStatus;
import com.athiva.notification.connector.service.MessageRequest;
import com.athiva.notification.connector.service.MessageResponse;
import com.athiva.notification.secureemailconnector.application.service.MessagingServiceImpl;
import com.athiva.notification.secureemailconnector.common.TestConstants;
import com.athiva.notification.secureemailconnector.domain.common.SecureEmailResponseCode;

/**
 * The connector messaging service impl test class.
 * 
 * @author ravindu.s
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-secure-notification-context.xml"})
public class MessagingServiceImplTest {

    @Autowired
    private MessagingServiceImpl messagingServiceImpl;

    @Ignore
    @Test
    public void testSendTextMessageSuccess() {

        MessageRequest request = new MessageRequest();
        request.setMessageBody(TestConstants.TEXT_MESSAGE);
        request.setRecipients(getRecipients());

        MessageResponse response = messagingServiceImpl.sendMessage(request);
        Assert.assertNotNull("Connector message response cannot be null", response);
        Assert.assertEquals("Athiva ResponseStatus should be SUCCESS", AthivaNotificationStatus.SUCCESS,
                response.getNotificationStatus());
        Assert.assertNull("RawOutput should be null", response.getRawResponse());
        Assert.assertTrue("Notification ExternalStatus cannot be null",
                CollectionUtils.isNotEmpty(response.getExternalStatusList()));
        ExternalStatus externalStatus = response.getExternalStatusList().get(0);
        Assert.assertEquals("External code should be \"Email send successfull\" ",
                SecureEmailResponseCode.SUCCESS.toString(), externalStatus.getCode());
        Assert.assertEquals("External Description should be success", SecureEmailResponseCode.SUCCESS.getValue(),
                externalStatus.getDescription());

    }

    @Ignore
    @Test
    public void testSendHTMLMessageSuccess() {

        MessageRequest request = new MessageRequest();
        request.setMessageBody(TestConstants.HTML_MESSAGE);
        request.setRecipients(getRecipients());

        MessageResponse response = messagingServiceImpl.sendMessage(request);
        Assert.assertNotNull("Connector message response cannot be null", response);
        Assert.assertEquals("Athiva ResponseStatus should be SUCCESS", AthivaNotificationStatus.SUCCESS,
                response.getNotificationStatus());
        Assert.assertNull("RawOutput should be null", response.getRawResponse());
        Assert.assertTrue("Notification ExternalStatus cannot be null",
                CollectionUtils.isNotEmpty(response.getExternalStatusList()));
        ExternalStatus externalStatus = response.getExternalStatusList().get(0);
        Assert.assertEquals("External code should be \"Email send successfull\" ",
                SecureEmailResponseCode.SUCCESS.toString(), externalStatus.getCode());
        Assert.assertEquals("External Description should be success", SecureEmailResponseCode.SUCCESS.getValue(),
                externalStatus.getDescription());

    }

    @Ignore
    @Test
    public void testSendTextMessagWithInvalidMailAddress() {

        MessageRequest request = new MessageRequest();
        request.setMessageBody(TestConstants.TEXT_MESSAGE);
        request.setRecipients(getBadRecipients());

        MessageResponse response = messagingServiceImpl.sendMessage(request);
        Assert.assertNotNull("Connector message response cannot be null", response);
        Assert.assertEquals("Athiva ResponseStatus should be INVALID_DATA", AthivaNotificationStatus.INVALID_DATA,
                response.getNotificationStatus());
        Assert.assertNotNull("RawOutput shouldn't be null", response.getRawResponse());

    }

    private List<RecipientType> getRecipients() {

        List<RecipientType> recipients = new ArrayList<RecipientType>();

        RecipientType recipient1 = new RecipientType();
        recipient1.setEmail(TestConstants.MESSAGE_TO);
        recipient1.setPhone(TestConstants.PHONE_NUMBER);
        recipient1.setFirstName(TestConstants.FIRST_NAME);
        recipient1.setLastName(TestConstants.LAST_NAME);
        recipients.add(recipient1);

        return recipients;
    }

    private List<RecipientType> getBadRecipients() {

        List<RecipientType> recipients = new ArrayList<RecipientType>();

        RecipientType recipient1 = new RecipientType();
        recipient1.setEmail(TestConstants.BAD_EMAIL_ADDRESS);
        recipient1.setPhone(TestConstants.PHONE_NUMBER);
        recipient1.setFirstName(TestConstants.FIRST_NAME);
        recipient1.setLastName(TestConstants.LAST_NAME);
        recipients.add(recipient1);

        return recipients;
    }
}

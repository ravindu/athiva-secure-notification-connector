package com.athiva.notification.secureemailconnector.transformer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athiva.notification.application.events.dto.RecipientType;
import com.athiva.notification.connector.service.MessageRequest;
import com.athiva.notification.secureemailconnector.application.transformer.MessageRequestTransformer;
import com.athiva.notification.secureemailconnector.common.TestConstants;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-secure-notification-context.xml"})
public class MessageRequestRequestTransformerTest {

    @Autowired
    private MessageRequestTransformer messageRequestTransformer;

    @Value("${email.configuration.emailaccount.username}")
    private String emailFrom;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSuccessMessageRequestToSecureEmailRequestTransformer() {

        MessageRequest messageRequest = buildSuccessMessageRequest();

        SecureEmailRequest emailrequest = messageRequestTransformer.transformToSecureEmailRequest(messageRequest);
        Assert.assertNotNull("Email message cannot be null", emailrequest.getMessage());
        Assert.assertNotNull("Email encode type cannot be null", emailrequest.getMessageEncodeType());
        Assert.assertNotNull("Email From cannot be null", emailrequest.getMessageFrom());
        Assert.assertNotNull("Email Id cannot be null", emailrequest.getMessageId());
        Assert.assertNotNull("Email subject cannot be null", emailrequest.getMessageSubject());
        Assert.assertNotNull("Email To cannot be null", emailrequest.getMessageTo());

    }

    @Test
    public void testFailureMessageRequestToSecureEmailRequestTransformer() {

        MessageRequest messageRequest = buildFailureMessageRequest();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Recipient.TO email address cannot be null or empty");
        messageRequestTransformer.transformToSecureEmailRequest(messageRequest);

    }

    private MessageRequest buildSuccessMessageRequest() {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageBody(TestConstants.HTML_MESSAGE);

        List<RecipientType> recipientType = new ArrayList<RecipientType>();
        RecipientType recipient = new RecipientType();
        recipient.setEmail(TestConstants.MESSAGE_TO);
        recipient.setFirstName(TestConstants.FIRST_NAME);
        recipient.setLastName(TestConstants.LAST_NAME);
        recipient.setPhone(TestConstants.PHONE_NUMBER);
        recipientType.add(recipient);

        messageRequest.setRecipients(recipientType);

        return messageRequest;
    }

    private MessageRequest buildFailureMessageRequest() {

        // Create message request without an email address
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageBody(TestConstants.HTML_MESSAGE);

        List<RecipientType> recipientType = new ArrayList<RecipientType>();
        RecipientType recipient = new RecipientType();
        recipient.setFirstName(TestConstants.FIRST_NAME);
        recipient.setLastName(TestConstants.LAST_NAME);
        recipient.setPhone(TestConstants.PHONE_NUMBER);
        recipientType.add(recipient);

        messageRequest.setRecipients(recipientType);

        return messageRequest;
    }
}

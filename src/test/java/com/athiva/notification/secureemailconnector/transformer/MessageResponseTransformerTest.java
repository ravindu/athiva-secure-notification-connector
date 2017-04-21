package com.athiva.notification.secureemailconnector.transformer;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athiva.notification.application.common.AthivaNotificationStatus;
import com.athiva.notification.connector.service.ExternalStatus;
import com.athiva.notification.connector.service.MessageResponse;
import com.athiva.notification.secureemailconnector.application.transformer.MessageResponseTransformer;
import com.athiva.notification.secureemailconnector.domain.common.SecureEmailResponseCode;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-secure-notification-context.xml"})
public class MessageResponseTransformerTest {

    @Autowired
    private MessageResponseTransformer messageResponseTransformer;

    @Test
    public void testSuccessSecureEmailResponseToMessageResponseTransformer() {

        SecureEmailResponse emailResponse = buildSSuccessecureEmailResponse();
        MessageResponse messageResponse= messageResponseTransformer.transformToMessageResponse(emailResponse);
        Assert.assertNotNull( "Email response cannot be null", emailResponse);
        Assert.assertEquals("Athiva Notification Status should be SUCCESS", AthivaNotificationStatus.SUCCESS, messageResponse.getNotificationStatus());
        Assert.assertTrue("External status cannot be null", CollectionUtils.isNotEmpty(messageResponse.getExternalStatusList()));
        
        ExternalStatus externalStatus=messageResponse.getExternalStatusList().get(0);
        Assert.assertEquals("Response code should be SUCCESS", SecureEmailResponseCode.SUCCESS.toString(), externalStatus.getCode());
        Assert.assertEquals("Response description should be matched", SecureEmailResponseCode.SUCCESS.getValue(), externalStatus.getDescription());

    }
    
    @Test
    public void testFailureSecureEmailResponseToMessageResponseTransformer() {

        SecureEmailResponse emailResponse = buildFailureSecureEmailResponse();
        MessageResponse messageResponse= messageResponseTransformer.transformToMessageResponse(emailResponse);
        Assert.assertNotNull( "Email response cannot be null", emailResponse);
        Assert.assertEquals("Athiva Notification Status should be FAILED", AthivaNotificationStatus.FAILED, messageResponse.getNotificationStatus());
        Assert.assertTrue("External status cannot be null", CollectionUtils.isNotEmpty(messageResponse.getExternalStatusList()));
        
        ExternalStatus externalStatus=messageResponse.getExternalStatusList().get(0);
        Assert.assertEquals("Response code should be SUCCESS", SecureEmailResponseCode.SEND_FAILED.toString(), externalStatus.getCode());
        Assert.assertEquals("Response description should be matched", SecureEmailResponseCode.SEND_FAILED.getValue(), externalStatus.getDescription());

    }

    private SecureEmailResponse buildSSuccessecureEmailResponse() {

        SecureEmailResponse emailResponse = new SecureEmailResponse(SecureEmailResponseCode.SUCCESS,
                SecureEmailResponseCode.SUCCESS.getValue());
        
        return emailResponse;
    }
    
    private SecureEmailResponse buildFailureSecureEmailResponse() {

        SecureEmailResponse emailResponse = new SecureEmailResponse(SecureEmailResponseCode.SEND_FAILED,
                SecureEmailResponseCode.SEND_FAILED.getValue());
        
        return emailResponse;
    }
}

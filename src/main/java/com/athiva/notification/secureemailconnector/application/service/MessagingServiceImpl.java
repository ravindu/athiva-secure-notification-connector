package com.athiva.notification.secureemailconnector.application.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athiva.notification.application.common.AthivaNotificationStatus;
import com.athiva.notification.connector.service.InvokeConnectorService;
import com.athiva.notification.connector.service.MessageRequest;
import com.athiva.notification.connector.service.MessageResponse;
import com.athiva.notification.secureemailconnector.application.common.AssertionUtil;
import com.athiva.notification.secureemailconnector.application.common.ConnectorUtil;
import com.athiva.notification.secureemailconnector.application.transformer.MessageRequestTransformer;
import com.athiva.notification.secureemailconnector.application.transformer.MessageResponseTransformer;
import com.athiva.notification.secureemailconnector.domain.common.SecureEmailConnectorTimeoutException;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailRequest;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailResponse;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailService;

/**
 * The connector implementation of {@link InvokeConnectorService} for sending notifications.
 * 
 * @author ravindu.s
 */
@Service
public class MessagingServiceImpl implements InvokeConnectorService {

    @Autowired
    private MessageRequestTransformer messageRequestTransformer;

    @Autowired
    private MessageResponseTransformer messageResponseTransformer;

    @Autowired
    private SecureEmailService secureEmailService;

    public MessageResponse sendMessage(MessageRequest messageRequest) {

        long executionStartTime = System.nanoTime();

        Logger logger = Logger.getLogger(getClass());
        logger.info("Connector Received MESSAGE_SERVICE Request");

        SecureEmailRequest emailRequest = null;
        SecureEmailResponse emailResponse = null;
        MessageResponse messageResponse = null;
        String thirdPartyServiceExecutionTime = null;
        String totalServiceExecutionTime = null;

        try {

            AssertionUtil.validateMessageRequest(messageRequest);

            emailRequest = messageRequestTransformer.transformToSecureEmailRequest(messageRequest);

            emailResponse = secureEmailService.sendEmail(emailRequest);
            thirdPartyServiceExecutionTime = emailResponse.getThirdPartyExcutionTime();

            messageResponse = messageResponseTransformer.transformToMessageResponse(emailResponse);

        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("Error found while validating email attributes." + illegalArgumentException.getMessage(),
                    illegalArgumentException);
            messageResponse = messageResponseTransformer.buildErrorEmailResponse(AthivaNotificationStatus.INVALID_DATA,
                    illegalArgumentException.getMessage());

        } catch (SecureEmailConnectorTimeoutException timeoutException) {
            logger.error("Error occoured while sending the email." + timeoutException.getMessage(), timeoutException);
            messageResponse = messageResponseTransformer.buildErrorEmailResponse(
                    AthivaNotificationStatus.MESSAGE_TIMEOUT, timeoutException.getMessage());

        } catch (Exception exception) {
            logger.error("Error occoured while processing the email." + exception.getMessage(), exception);
            messageResponse = messageResponseTransformer.buildErrorEmailResponse(AthivaNotificationStatus.FAILED,
                    exception.getMessage());
        }

        long executionEndTime = System.nanoTime();
        totalServiceExecutionTime = ConnectorUtil.calculateExcutionTimeInMilies(executionStartTime, executionEndTime);

        logger.info("Connector Impl completed MESSAGE_SERVICE Request. [Transaction Summary]-"
                + ConnectorUtil.populateResponseLoggingParameters(messageResponse.getNotificationStatus(),
                        messageResponse.getExternalStatusList(), thirdPartyServiceExecutionTime,
                        totalServiceExecutionTime));

        return messageResponse;
    }

}

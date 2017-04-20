package com.athiva.notification.secureemailconnector.application.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.athiva.notification.application.common.AthivaNotificationStatus;
import com.athiva.notification.connector.service.ExternalStatus;
import com.athiva.notification.connector.service.MessageResponse;
import com.athiva.notification.secureemailconnector.domain.message.SecureEmailResponse;
/**
 * 
 * @author ravindu.s
 *
 */
@Component
public class MessageResponseTransformer {

    public MessageResponse transformToMessageResponse(final SecureEmailResponse emailReponse) {

        MessageResponse messageResponse = null;

        AthivaNotificationStatus notificationStatus = buildNotificationStatus(emailReponse);

        List<ExternalStatus> externalStatus = buildNotificationExtrnalStatus(emailReponse);

        String rawOutput = emailReponse.getRawOutput();

        if (rawOutput != null) {
            messageResponse = new MessageResponse.Builder().withNotificationStatus(notificationStatus)
                    .withExternalStatusList(externalStatus).withRawOutput(rawOutput).build();

        } else {
            messageResponse = new MessageResponse.Builder().withNotificationStatus(notificationStatus)
                    .withExternalStatusList(externalStatus).build();
        }
        return messageResponse;
    }

    private List<ExternalStatus> buildNotificationExtrnalStatus(final SecureEmailResponse emailReponse) {

        List<ExternalStatus> externalStatusList;

        if (emailReponse == null) {
            externalStatusList = null;

        } else {

            String code = emailReponse.getResponseCode().toString();
            String description = emailReponse.getResponseDescription();
            ExternalStatus status = new ExternalStatus(code, description);

            externalStatusList = new ArrayList<ExternalStatus>(1);
            externalStatusList.add(status);
        }
        return externalStatusList;
    }

    private AthivaNotificationStatus buildNotificationStatus(final SecureEmailResponse emailResponse) {

        if (emailResponse == null) {
            return AthivaNotificationStatus.EXTERNAL_GATEWAY_ERROR;
        }

        AthivaNotificationStatus notificationStatus = null;

        switch (emailResponse.getResponseCode()) {
            case SUCCESS:
                notificationStatus = AthivaNotificationStatus.SUCCESS;
                break;

            case AUTHENTICATION_FAILED:
                notificationStatus = AthivaNotificationStatus.INVALID_DATA;
                break;

            default:
                notificationStatus = AthivaNotificationStatus.FAILED;
                break;
        }
        return notificationStatus;
    }

    public MessageResponse buildErrorEmailResponse(AthivaNotificationStatus notifictionStatus, String rawOutput) {

        MessageResponse messageResponse = new MessageResponse.Builder().withNotificationStatus(notifictionStatus)
                .withRawOutput(rawOutput).build();

        return messageResponse;
    }
}

package com.athiva.notification.secureemailconnector.application.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.athiva.notification.application.common.AthivaNotificationStatus;
import com.athiva.notification.connector.service.ExternalStatus;

/**
 * 
 * @author ravindu.s
 * 
 */
public class ConnectorUtil {

    private ConnectorUtil() {

    }

    public static String calculateExcutionTimeInMilies(final long startTime, final long endTime) {
        long timeGap = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        return String.valueOf(timeGap);
    }

    public static Map<String, String> populateResponseLoggingParameters(AthivaNotificationStatus notificationStatus,
            List<ExternalStatus> externalStatusList, String thirdPartyServiceExecutionTime,
            String totalServiceExecutionTime) {

        Map<String, String> transactionDetails = new LinkedHashMap<String, String>();
        transactionDetails.put("[Athiva Notification Status]", notificationStatus.toString());

        if (CollectionUtils.isNotEmpty(externalStatusList)) {
            ExternalStatus status = externalStatusList.get(0);
            transactionDetails.put("[Response Code]", status.getCode());
            transactionDetails.put("[Response Description]", status.getDescription());
        }

        if (StringUtils.isNotBlank(thirdPartyServiceExecutionTime)) {
            transactionDetails.put("[3rd Party Execution Time]", thirdPartyServiceExecutionTime);
        }

        transactionDetails.put("[Total Execution Time]", totalServiceExecutionTime);

        return transactionDetails;
    }
}

package com.athiva.notification.secureemailconnector.domain.message;

import com.athiva.notification.secureemailconnector.domain.common.SecureEmailResponseCode;
/**
 * 
 * @author ravindu.s
 *
 */
public class SecureEmailResponse {

    private SecureEmailResponseCode responseCode;

    private String responseDescription;

    private String rawOutput;

    private String thirdPartyExcutionTime;

    public SecureEmailResponse(SecureEmailResponseCode responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public SecureEmailResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(SecureEmailResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getRawOutput() {
        return rawOutput;
    }

    public void setRawOutput(String rawOutput) {
        this.rawOutput = rawOutput;
    }

    public String getThirdPartyExcutionTime() {
        return thirdPartyExcutionTime;
    }

    public void setThirdPartyExcutionTime(String thirdPartyExcutionTime) {
        this.thirdPartyExcutionTime = thirdPartyExcutionTime;
    }

}

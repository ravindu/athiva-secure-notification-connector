package com.athiva.notification.secureemailconnector.domain.common;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author ravindu.s
 *
 */
public enum MessageEncodeType {

    TEXT_PLAIN("text/plain;charset=utf-8"), TEXT_HTML("text/html;charset=utf-8");

    public static Map<String, MessageEncodeType> encodingTypesMap = null;

    static {

        encodingTypesMap = new HashMap<String, MessageEncodeType>();
        for (MessageEncodeType encode : MessageEncodeType.values()) {
            encodingTypesMap.put(encode.getValue(), encode);
        }

    }

    private final String encodeType;

    private MessageEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public String getValue() {
        return this.encodeType;
    }

    public static MessageEncodeType getEncodeType(String encodingType) {

        MessageEncodeType encoding = encodingTypesMap.get(encodingType);
        return encoding;

    }

}

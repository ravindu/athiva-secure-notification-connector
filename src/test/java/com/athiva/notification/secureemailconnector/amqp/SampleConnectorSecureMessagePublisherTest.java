package com.athiva.notification.secureemailconnector.amqp;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athiva.notification.application.amqp.AthivaEventPublisher;
import com.athiva.notification.application.common.AthivaTransactionType;
import com.athiva.notification.application.common.NotificationConstants;
import com.athiva.notification.application.util.IOUtil;

/**
 * The sample connector secure message publisher test class.
 * 
 * @author hafeez
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/test-applicationContext.xml" })
public class SampleConnectorSecureMessagePublisherTest {

	@Autowired
	private AthivaEventPublisher athivaEventPublisher;

	@Value("${athiva.notification.connector.queue}")
	private String sampleConnectorQueue;

	@Value("${athiva.notification.sample.reply.queue}")
	private String sampleReplyQueue;

	@Test
	public void testPublishSecureMessageSuccess() throws IOException, InterruptedException {

		FileReader reader = new FileReader("src/test/resources/xmldata/SecureNotificationRequestWithCustomMessage.xml");
		String messageBody = IOUtil.getStringFromFileReader(reader);

		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(NotificationConstants.RABBITMQ_TRANSACTION_TYPE_MESSAGE_HEADER_KEY,
				AthivaTransactionType.SEC_NOTIFICATION_EVENT.name());

		athivaEventPublisher.convertAndSendWithReplyQueue(messageBody, MessageProperties.CONTENT_TYPE_XML, headers,
				sampleConnectorQueue, sampleReplyQueue);
		Thread.sleep(50000);
	}
}

package io.pivotal.sqs.listener;

import java.util.List;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

@Component
public class NotificationListener{
	
	private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
	
	@Value("${vcap.services.sqs.credentials.QUEUE_URL}")
	private String sqsURL;

	@Value("${vcap.services.sqs.credentials.SQS_AWS_ACCESS_KEY_ID}")
	private String accessKey;

	@Value("${vcap.services.sqs.credentials.SQS_AWS_SECRET_ACCESS_KEY}")
	private String secretKey;

	@Value("${aws.region}")
	private String awsRegion;

	@Scheduled(fixedRate = 1000)
	public void getMessage() {
		BasicAWSCredentials bAWSc = new BasicAWSCredentials(accessKey, secretKey);
		final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				.withRegion(awsRegion)
				.withCredentials(new AWSStaticCredentialsProvider(bAWSc))
				.build();
        while(true) {
        	log.info("Receiving messages from MyQueue.\n");
        	final ReceiveMessageRequest receiveMessageRequest =
                    new ReceiveMessageRequest(sqsURL)
                    	.withMaxNumberOfMessages(1)
                    	.withWaitTimeSeconds(3);
	        final List<com.amazonaws.services.sqs.model.Message> messages = sqs.receiveMessage(receiveMessageRequest)
	                .getMessages();
	        for (final com.amazonaws.services.sqs.model.Message message : messages) {
	        	log.info("Message");
	        	log.info("  MessageId:     "
	                    + message.getMessageId());
	        	log.info("  ReceiptHandle: "
	                    + message.getReceiptHandle());
	        	log.info("  MD5OfBody:     "
	                    + message.getMD5OfBody());
	        	log.info("  Body:          "
	                    + message.getBody());
//	            if(!"".equals(message.getBody())) {
//
//		            System.out.println("Deleting a message.\n");
//		            final String messageReceiptHandle = messages.get(0).getReceiptHandle();
//		            sqs.deleteMessage(new DeleteMessageRequest(sqsURL,
//		                    messageReceiptHandle));
//		         }
	        }
        }
	}
}

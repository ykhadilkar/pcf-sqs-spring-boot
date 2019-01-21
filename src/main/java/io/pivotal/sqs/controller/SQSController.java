package io.pivotal.sqs.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Controller
public class SQSController {
	
	private static final Logger log = LoggerFactory.getLogger(SQSController.class);

    @Value("${vcap.services.sqs.credentials.QUEUE_URL}")
	private String sqsURL;

    @Value("${vcap.services.sqs.credentials.SQS_AWS_ACCESS_KEY_ID}")
    private String accessKey;

    @Value("${vcap.services.sqs.credentials.SQS_AWS_SECRET_ACCESS_KEY}")
    private String secretKey;

	@Value("${aws.region}")
	private String awsRegion;

	@RequestMapping(value = "/messages", method = RequestMethod.POST)
    public @ResponseBody void write(@RequestBody String notificationData){
		try {
        BasicAWSCredentials bAWSc = new BasicAWSCredentials(accessKey, secretKey);
		final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(bAWSc))
                .build();
		log.info("===============================================");
		log.info("Getting Started with Amazon SQS Standard Queues");
		log.info("===============================================\n");
        
		log.info("Sending a message to MyQueue.\n");
        sqs.sendMessage(new SendMessageRequest(sqsURL, notificationData));
        log.info("Message Sent.\n");
        
		}catch (final AmazonServiceException ase) {
			log.error("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
        	log.error("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
        	log.error("Error Message: " + ace.getMessage());
        }
    }


    @RequestMapping(value = "/messages", method = RequestMethod.DELETE)
    public @ResponseBody void delete(){
        try {
            BasicAWSCredentials bAWSc = new BasicAWSCredentials(accessKey, secretKey);
            final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                    .withRegion(awsRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(bAWSc))
                    .build();
            log.info("===============================================");
            log.info("Getting Started with Amazon SQS Standard Queues");
            log.info("===============================================\n");

            log.info("Deleting (purging) all message to MyQueue.\n");

            sqs.purgeQueue(new PurgeQueueRequest(sqsURL));

            log.info("All Messages Deleted.\n");

        }catch (final AmazonServiceException ase) {
            log.error("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            log.error("Error Message:    " + ase.getMessage());
            log.error("HTTP Status Code: " + ase.getStatusCode());
            log.error("AWS Error Code:   " + ase.getErrorCode());
            log.error("Error Type:       " + ase.getErrorType());
            log.error("Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
            log.error("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            log.error("Error Message: " + ace.getMessage());
        }
    }
}

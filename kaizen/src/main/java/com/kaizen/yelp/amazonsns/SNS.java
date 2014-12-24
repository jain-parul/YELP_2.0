package com.kaizen.yelp.amazonsns;

import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.ListSubscriptionsRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsResult;
import com.amazonaws.services.sns.model.ListTopicsRequest;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.Topic;

public class SNS {


public static void main(String[] args){


		

		SNS sns = new SNS();
		// for testing the subscribe and publishing topic
		
		//sns.userSubscribeToTopic("Sweet Cakes Café", "swetha.patnala@gmail.com");
		sns.userPublishingToTopic("Sweet Cakes Café", "message 3 for category1");
		
	}


	private AmazonSNS connectToSNS (){
		final AmazonSNS snsConnection = new AmazonSNSClient(new BasicAWSCredentials("AKIAIC33WZ7HEEURQ3WA",  "qpXBJfKec2tDPQUQKVMPv7TXuSXTU+q6pXJGdTUe"));
		
		snsConnection.setEndpoint("sns.us-west-1.amazonaws.com");
		return snsConnection;

	}
	
	
	
	public String getTopicName (String category){
		
		
		//String str = "Sweet Cakes Café";

		String topicName = "";

		StringBuilder sb = new StringBuilder(category.toLowerCase());

		for (int i =0 ; i < sb.length() ; i++){

			char c= sb.charAt(i);

			//System.out.println("" +c );

			if ( c >= 'a' && c <= 'z'){

				topicName = topicName + c;
				//System.out.println(" topic" + topic);

			}

		}
		
		System.out.println(" final topic name "+ topicName);

	
		
		return topicName;
	}


	public void userSubscribeToTopic (  String category , String email ){
		
		try {
			
			SNS sns = new SNS();

			AmazonSNS snsconnect = sns.connectToSNS();

			
			
			//String categoryName = category.replaceAll("\\s+","");
			
			String categoryName = sns.getTopicName(category);

			System.out.println(" category name " + categoryName);


			String topicArn = sns.createTopic(snsconnect, categoryName);

			
			


			boolean subscribeStatus = sns.isSubscribed(snsconnect, topicArn, email);

			if(subscribeStatus == true){
				System.out.println(" User already subscribed to topic "+ categoryName);
			}
			else{

				System.out.println(" Sending email to user to  subscribe to topic "+ categoryName);
				sns.subscribeToTopic(snsconnect, topicArn, "email", email);
			}


			
		} catch (AmazonServiceException e) {
			
			System.out.println(" Invalid  access key or secret key");
		}

		

		
		//sns.publishToTopic(snsconnect, topicArn, message, "publishing new review");

	}


	public boolean isSubscribed(AmazonSNS snsConnect ,String topicArn , String endpoint){


		//ListSubscriptionsByTopicRequest listSubscriptionsRequest = new ListSubscriptionsByTopicRequest();
		ListSubscriptionsByTopicResult listofsubs = snsConnect.listSubscriptionsByTopic(topicArn);
		List<Subscription> list = listofsubs.getSubscriptions();


		for (Subscription sub :list){
			String existingEndPoint = sub.getEndpoint();
			System.out.println("endpoint : " + existingEndPoint);
			if( endpoint.equalsIgnoreCase(existingEndPoint)){
				return true;
			}
		}

		return false;
	}

	public  String createTopic(AmazonSNS sns , String topicName){

	
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
		CreateTopicResult created = sns.createTopic(createTopicRequest);

		String topicArn = created.getTopicArn();
		System.out.println("topic arn " + topicArn);
		return topicArn;
	}



	public void subscribeToTopic(AmazonSNS sns , String topicArn , String protocol , String endpoint){

		sns.subscribe(topicArn, protocol, endpoint);
		//sns.subscribe(topicArn, "email", "swetha.patnala@gmail.com");
	}


	public void publishToTopic(AmazonSNS sns ,String topicArn , String message , String subject){

		PublishRequest publishToTopic = new PublishRequest(topicArn, message , subject);
		PublishResult publishResult = sns.publish(publishToTopic);
		publishResult.getMessageId();

		System.out.println("message id "+ publishResult.getMessageId());

	}

	public void userPublishingToTopic(String category , String message){
		
		try {
			
			SNS sns = new SNS();

			AmazonSNS snsconnect = sns.connectToSNS();

			String categoryName = sns.getTopicName(category);
			//String categoryName = category.replaceAll("\\s+","");

			System.out.println(" category name " + categoryName);


			CreateTopicRequest createTopicRequest = new CreateTopicRequest(categoryName);

			CreateTopicResult created = snsconnect.createTopic(createTopicRequest);

			String topicArn = created.getTopicArn();


			String subject =  "Publishing message from "+ category;

			sns.publishToTopic(snsconnect, topicArn, message, subject);
			
			
			
		} catch (AmazonServiceException e) {
			
			System.out.println(" Invalid  access key or secret key");
		}

		



		}

}





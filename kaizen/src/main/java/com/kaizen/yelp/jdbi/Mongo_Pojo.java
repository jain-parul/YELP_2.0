package com.kaizen.yelp.jdbi;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;

import com.kaizen.yelp.domain.Review;
import com.kaizen.yelp.domain.UserLogin;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSONParseException;





public class Mongo_Pojo {
	
	public DBObject toDbObject(UserLogin newUser) {
		DBObject tempDbObject = new BasicDBObject();
		tempDbObject.put("_id", newUser.get_id());
		tempDbObject.put("password", newUser.getPassword());
		tempDbObject.put("email", newUser.getEmail());
		return tempDbObject;
	}
	public DBObject toDbObject(Review newReview) {
		DBObject tempDbObject = new BasicDBObject();
		tempDbObject.put("business_id", newReview.getBusiness_id());
		tempDbObject.put("user_id", newReview.getUser_id());
		tempDbObject.put("rating", newReview.getRating());
		tempDbObject.put("review_content", newReview.getReview_content());
		tempDbObject.put("block", newReview.getBlock());
		return tempDbObject;
	}
	
	public UserLogin toUserObject(String jsonString) {
		UserLogin tempUser = new UserLogin();
		try {
			tempUser = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonString, UserLogin.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempUser;
	}

}

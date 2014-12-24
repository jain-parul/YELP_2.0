package com.kaizen.yelp.ui.resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kaizen.yelp.amazonsns.SNS;
import com.kaizen.yelp.domain.Business;
import com.kaizen.yelp.domain.Search;
import com.kaizen.yelp.repository.UserRepository;
import com.kaizen.yelp.ui.views.BusinessView;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

@Path("/kaizen/{username}/{business_id}")
@Produces(MediaType.TEXT_HTML)
public class BusinessResource {
	private Mongo mongo;
	private UserRepository userRepository;
	public BusinessResource(Mongo mongo, UserRepository userRepository) {
		// TODO Auto-generated constructor stub
		this.mongo = mongo;
		this.userRepository = userRepository;
	}
	
	@POST
	@Path("/signout")
	public void signout() {
		ArrayList<Search> searchRef;
		searchRef = userRepository.getSearch();
		searchRef.clear();
		userRepository.saveSearch(searchRef);
	}
	
	@GET 
	@Path("/subscribe")
	public void subscribe(@PathParam("username") String username, @PathParam("business_id") String business_id) {
		String email = null;
		String name = null;
		DB db = mongo.getDB("273project");
		DBCollection userInfoColl = db.getCollection("userInfo");
		DBCollection coll = db.getCollection("business");

		BasicDBObject query = new BasicDBObject("_id", username);
		DBCursor userCursor = userInfoColl.find(query);
		while (userCursor.hasNext()) {
			BasicDBObject userObj1 = (BasicDBObject) userCursor.next();
			email = userObj1.getString("email");
		}
		System.out.println("id "+business_id );
		BasicDBObject searchQuery = new BasicDBObject();
		if (business_id != null) {
			searchQuery.append("business_id", business_id);
		}
		DBCursor myCol = coll.find(searchQuery);
		while(myCol.hasNext()) {
			 
			  BasicDBObject businessObj = (BasicDBObject) myCol.next();
			  String b_id = businessObj.getString("business_id");
			   name = businessObj.getString("name");
		}
		System.out.println(name);
		SNS sns = new SNS();
		sns.userSubscribeToTopic(name, email);
	}
	@GET
	public BusinessView getBusiness(@PathParam("username") String username, @PathParam("business_id") String business_id) {
		
		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");
		DBCollection userHistory;
		userHistory = db.getCollection("userHistory");
		String foundBusiness_id = "";
		
		BasicDBObject searchQuery = new BasicDBObject();
		if (business_id != null) {
			searchQuery.append("business_id", business_id);
		}
		
		DBCursor myCol = coll.find(searchQuery);
		
		Business business = new Business();
		
		 try { while(myCol.hasNext()) {
		 
		  BasicDBObject businessObj = (BasicDBObject) myCol.next();
		  String b_id = businessObj.getString("business_id");
		  String name = businessObj.getString("name");
		  String full_address = businessObj.getString("full_address");
		  String categories = businessObj.getString("categories");
		  String latitude = businessObj.getString("latitude");
		  String longitude = businessObj.getString("longitude");
		  float review_count = Float.parseFloat(businessObj.getString("review_count"));
		  float stars = Float.parseFloat(businessObj.getString("stars"));
		  
		  DBObject tempDbObject = new BasicDBObject();
		  BasicDBObject searchRecord = new BasicDBObject("username", username);
		  searchRecord.append("business_id", b_id);
		  DBCursor myData = userHistory.find(searchRecord);
		  
		  try { while(myData.hasNext()) {
				BasicDBObject obj= (BasicDBObject) myData.next();
				foundBusiness_id = obj.getString("business_id");
				}
				}finally {
					myData.close();
				}
				if(!foundBusiness_id.equals(b_id))
				{
		  tempDbObject.put("username", username);
		  tempDbObject.put("business_id", b_id);
		  userHistory.insert(tempDbObject);
				}
		  business.setBusiness_id(b_id);
		  business.setName(name);
		  business.setFull_address(full_address);
		  business.setCategories(categories);
		  business.setLatitude(latitude);
		  business.setLongitude(longitude);
		  business.setReview_count(review_count);
		  business.setStars(stars);
		   
		  } } finally { myCol.close(); }
		
		return new BusinessView(username, business);
	}
		
}

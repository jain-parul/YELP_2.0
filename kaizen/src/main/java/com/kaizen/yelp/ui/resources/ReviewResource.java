package com.kaizen.yelp.ui.resources;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kaizen.yelp.amazonsns.SNS;
import com.kaizen.yelp.domain.Review;
import com.kaizen.yelp.domain.Search;
import com.kaizen.yelp.domain.UserLogin;
import com.kaizen.yelp.repository.UserRepository;
import com.kaizen.yelp.ui.views.ReviewView;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

@Path("/kaizen/{username}/review/{business_id}")
@Produces(MediaType.TEXT_HTML)
public class ReviewResource {
	private Mongo mongo;
	private UserRepository userRepository;
	public ReviewResource(Mongo mongo, UserRepository userRepository) {
		// TODO Auto-generated constructor stub
		this.mongo = mongo;
		this.userRepository = userRepository;
	}
/*
	@GET
	public ReviewView getReview(@PathParam("username") String username) {
		
		return new ReviewView(username);
	}
*/
	@GET
	public ReviewView getReview(@PathParam("username") String username, @PathParam("business_id") String business_id) {

		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");
		BasicDBObject searchQuery = new BasicDBObject("business_id", business_id);
		DBCursor myCol = coll.find(searchQuery);

		BasicDBObject obj= (BasicDBObject) myCol.next();
		String name = obj.getString("name");

		ArrayList<Review> reviewList = new ArrayList<Review>();
		long review_count = 0;
		
		DBCollection revColl = db.getCollection("review");
		BasicDBObject reviewQuery = new BasicDBObject("business_id", business_id);
		
		DBCursor revCur = revColl.find(reviewQuery);
		revCur.limit(20);
		
		try { while(revCur.hasNext()) {
			
			BasicDBObject revObj = (BasicDBObject)revCur.next();
									
			String uname = revObj.getString("user_id");
			String review_content = revObj.getString("review_content");
			float rating = Float.parseFloat(revObj.getString("rating"));
			
			Review rev = new Review();
			rev.setUser_id(uname);
			rev.setReview_content(review_content);
			rev.setRating(rating);
			
			reviewList.add(rev);
			
			review_count++;
			
		}
		} finally {
					revCur.close();
					myCol.close();
		}
			
		
		return new ReviewView(username, name, reviewList, review_count);
	}
	
	@POST
	@Path("/signout")
	public void signout() {
		
		ArrayList<Search> searchRef;
		searchRef = userRepository.getSearch();
		searchRef.clear();
		userRepository.saveSearch(searchRef);
	}
	
	@POST
	@Path("/")
	public void postReview(@PathParam("username") String username, @PathParam("business_id") String business_id, @FormParam("rating") String rating, @FormParam("review_content") String review_content, @FormParam("block") String block) {
		
		String name = null;
		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("review");
		
		ArrayList<Search> tempSearchList;
		Review review = new Review();
		
		review.setBusiness_id(business_id);
		review.setUser_id(username);
		review.setRating(Float.parseFloat(rating));
		review.setReview_content(review_content);
		review.setBlock(block);
	
		if(!(rating.isEmpty()&&review_content.isEmpty()&&block.isEmpty()))
		{
			userRepository.saveReview(review);
		}
		DBCollection collBusiness = db.getCollection("business");
		BasicDBObject searchQuery = new BasicDBObject();
		if (business_id != null) {
			searchQuery.append("business_id", business_id);
		}
		DBCursor myCol = collBusiness.find(searchQuery);
		while(myCol.hasNext()) {
			 
			  BasicDBObject businessObj = (BasicDBObject) myCol.next();
			  String b_id = businessObj.getString("business_id");
			   name = businessObj.getString("name");
		}
		if(!(block == null))
		{
			tempSearchList = userRepository.getSearch();
			for (int i=0;i<tempSearchList.size();i++) {
			      Search searchObj = tempSearchList.get(i);
			      if(searchObj.getBusiness_id().equals(business_id))
			      {
			    	  tempSearchList.remove(i);
			      }
			  }
			userRepository.saveSearch(tempSearchList);			
		}


		SNS sns = new SNS();
		sns.userPublishingToTopic(name, review_content);

	}
	
}

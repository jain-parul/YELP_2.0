package com.kaizen.yelp.ui.resources;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kaizen.yelp.domain.Recommendation;
import com.kaizen.yelp.domain.Search;
import com.kaizen.yelp.repository.UserRepository;
import com.kaizen.yelp.ui.views.UserView;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

@Path("/kaizen/{username}/home")
@Produces(MediaType.TEXT_HTML)
public class UserResource {
	private Mongo mongo;
	private UserRepository userRepository;
	public UserResource(Mongo mongo, UserRepository userRepository) {
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
public UserView getUser(@PathParam("username") String username) {
		ArrayList<Search> searchList = new ArrayList<Search>();
		
		searchList = userRepository.getSearch();
		for (int i = 0; i < searchList.size(); i++) {
			System.out.println(searchList.get(i).getName());
		}
		
		DB db = mongo.getDB("273project");
		DBCollection collHistory = db.getCollection("userHistory");
		
		BasicDBObject searchReco = new BasicDBObject("username", username);
		DBCursor myReco = collHistory.find(searchReco);
		myReco.limit(5);
		
		ArrayList<Recommendation> recoList = new ArrayList<Recommendation>();
		
		try { while(myReco.hasNext()) {
			
		
			BasicDBObject obj= (BasicDBObject) myReco.next();
									
			String business_id_reco = obj.getString("business_id");
			
			DBCollection collBusiness = db.getCollection("business");
			BasicDBObject searchRecoData = new BasicDBObject("business_id", business_id_reco);
			DBCursor myData = collBusiness.find(searchRecoData);
			
			DBCollection collReview = db.getCollection("review");
			BasicDBObject searchBlockQuery = new BasicDBObject("business_id", business_id_reco);
			searchBlockQuery.append("user_id", username);
			searchBlockQuery.append("block", "on");
			DBCursor myBlock = collReview.find(searchBlockQuery);
			String blockedBusiness_id = "";
			
			try { while(myBlock.hasNext()) {
			BasicDBObject objBlock= (BasicDBObject) myBlock.next();
			blockedBusiness_id = objBlock.getString("business_id");
			}
			}finally {
				myBlock.close();
			}
			if(!blockedBusiness_id.equals(business_id_reco))
			{try { while(myData.hasNext()) {
				BasicDBObject objReco= (BasicDBObject) myData.next();
				String name = objReco.getString("name");
				String full_address = objReco.getString("full_address");
				float stars = Float.parseFloat(objReco.getString("stars"));
				
				Recommendation reco = new Recommendation();
				reco.setBusiness_id(business_id_reco);
				reco.setName(name);
				reco.setFull_address(full_address);
				reco.setStars(stars);
			
				recoList.add(reco);
				userRepository.saveReco(recoList);
				
				}
				}finally {
					myData.close();
				}
			
			}
			}
			
		} finally {
				myReco.close();
		 	}		
		
		
		return new UserView(username, searchList, recoList);
	}
	
@POST
@Path("/search/")
	public void searchBiz(@PathParam("username") String username, @FormParam("search_business") String search_business, 
			@FormParam("search_city") String search_city, @FormParam("search_day") String search_day, 
			@FormParam("search_startTime") String search_startTime, @FormParam("search_endTime") String search_endTime, 
			@FormParam("parking") String parking, @FormParam("creditcards") String creditcards, 
			@FormParam("takeout") String takeout, @FormParam("wifi") String wifi,   
			@FormParam("kids") String kids, @FormParam("groups") String groups,@FormParam("curLatitude") String curLatitude,
			@FormParam("curLongitude") String curLongitude){
	
		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");
		BasicDBObject searchQuery = new BasicDBObject("city", search_city);
	    	
		searchQuery.append("open", true);

		searchQuery.append("hours." + search_day + ".open",
				new BasicDBObject("$lte", search_startTime)).append(
				"hours." + search_day + ".close", new BasicDBObject("$gt", search_endTime));
		searchQuery.append("categories", search_business);
		
		
		if (parking != null) {
				searchQuery.append("attributes.Parking", new BasicDBObject("$exists", true));
			}
		if (creditcards != null) {
				searchQuery.append("attributes.Accepts Credit Cards", true);
			}
		if (takeout != null) {
			searchQuery.append("attributes.Take-out", true);
		}
		if (wifi != null) {
				searchQuery.append("attributes.Wi-Fi", true);
			}
		if (kids != null) {
				searchQuery.append("attributes.Good for Kids", true);
			}
		if (groups != null) {
				searchQuery.append("attributes.Good For Groups", true);
			}


		DBCursor myCol = coll.find(searchQuery);
		myCol.limit(20);
		
		ArrayList<Search> searchList = new ArrayList<Search>();
		
		try { while(myCol.hasNext()) {
			
		
			BasicDBObject obj= (BasicDBObject) myCol.next();
									
			String business_id = obj.getString("business_id");
			String name = obj.getString("name");
			String full_address = obj.getString("full_address");
			float stars = Float.parseFloat(obj.getString("stars"));
			//double stars = (obj.getDouble("stars"));
			String lat = obj.getString("latitude");
			String longit = obj.getString("longitude");
			double latitude=0;
			double longitude=0;
			if(lat!=null && longit !=null)
			{
				latitude = Double.parseDouble(obj.getString("latitude"));
				longitude = Double.parseDouble(obj.getString("longitude"));
				
			}
			
			DBCollection collReview = db.getCollection("review");
			BasicDBObject searchBlockQuery = new BasicDBObject("business_id", business_id);
			searchBlockQuery.append("user_id", username);
			searchBlockQuery.append("block", "on");
			DBCursor myBlock = collReview.find(searchBlockQuery);
			String blockedBusiness_id = "";
			
			try { while(myBlock.hasNext()) {
			BasicDBObject objBlock= (BasicDBObject) myBlock.next();
			blockedBusiness_id = objBlock.getString("business_id");
			}
			}finally {
				myBlock.close();
			}
			if(!blockedBusiness_id.equals(business_id))
			{
				Search search = new Search();
				search.setBusiness_id(business_id);
				search.setName(name);
				search.setFull_address(full_address);
				search.setStars(stars);
				search.setLatitude(latitude);
  	            search.setLongitude(longitude);
				searchList.add(search);
				userRepository.saveSearch(searchList);
			}
			
	}
}	finally {
				myCol.close();
		 	}		
	
	}

}

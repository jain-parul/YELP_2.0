package com.kaizen.yelp.api;

import java.net.UnknownHostException;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.kaizen.yelp.amazonsns.SNS;
import com.kaizen.yelp.domain.Business;
import com.kaizen.yelp.domain.Review;
import com.kaizen.yelp.dto.BusinessDto;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.yammer.metrics.annotation.Timed;
//import javax.ws.rs.core.Response;
//import com.mongodb.MongoClient;

@Path("/kaizen/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KaizenResource {

	private Mongo mongo;
	private DBCollection coll;

	public KaizenResource(Mongo mongo, DBCollection coll)
			throws UnknownHostException, MongoException {
		this.mongo = mongo;
		this.coll = coll;

	}

	@POST
	@Path("/subscribe")
	@Timed(name = "subscribe")
	public Response userSubscribe(@Context UriInfo uriInfo) {

		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();
		// System.out.println(queryParams.getFirst("name"));
		String businessName = queryParams.getFirst("name");
		// String username = queryParams.getFirst("username");
		String username = "Jan";
		String email = null;
		System.out.println("business name " + businessName);
		System.out.println("user name " + username);

		DB db = mongo.getDB("273project");

		DBCollection userInfoColl = db.getCollection("userInfo");

		BasicDBObject query = new BasicDBObject("username", username);
		DBCursor userCursor = userInfoColl.find(query);
		while (userCursor.hasNext()) {
			BasicDBObject userObj1 = (BasicDBObject) userCursor.next();
			email = userObj1.getString("email");
		}
		System.out.println("email " + email);

		System.out.println("hiii");
		SNS sns = new SNS();
		sns.userSubscribeToTopic(businessName, email);
		return Response.status(201).build();

	}

	@POST
	@Path("/insertReview")
	public boolean validateUser(Review review) {

		DB db = mongo.getDB("273project");
		DBCollection reviewColl = db.getCollection("review");
		BasicDBObject revObj = new BasicDBObject("review", review);
		reviewColl.insert(revObj);
		return true;

	
	}
	
	
	
	@GET
	 @Path("/subscribe")
    	@Timed(name = "subscribe")
	
	public Response userSubscribe( String businesName , String email){
		
		SNS sns = new SNS();
		sns.userSubscribeToTopic(businesName, email);
		
		return Response.status(201).build();
	}
	
	
	
	
	@GET
    	@Path("/publish")
    	@Timed(name = "publish")
	
	public Response userPublish( String businesName , String email){
		
		SNS sns = new SNS();
		sns.userPublishingToTopic(businesName, email);
		
		return Response.status(201).build();
	}

	@GET
	@Timed(name = "get-business-main")
	public BusinessDto getBusinesMain(@Context UriInfo uriInfo) {
		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();
		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");
		String businessID = queryParams.getFirst("business_id");
		String name = queryParams.getFirst("name");
		String state = queryParams.getFirst("state");
		String city = queryParams.getFirst("city");
		String address = queryParams.getFirst("address");
		String zipcode = queryParams.getFirst("zipcode");
		String category = queryParams.getFirst("categories");
		System.out.println(name);

		BasicDBObject searchQuery = new BasicDBObject();

		if (businessID != null) {
			searchQuery.append("business_id", businessID);
		} else {
			if (name != null) {
				searchQuery.append("name", name);
			}
			if (state != null) {
				searchQuery.append("state", state);
			}
			if (city != null) {
				searchQuery.append("city", city);
			}
			if (zipcode != null) {
				searchQuery.append("zipcode", zipcode);
			}
			if (category != null) {
				searchQuery.append("categories", category);
			}
			if (address != null) {
				searchQuery.append("address", address);
			}
		}

		DBCursor busColl = coll.find(searchQuery);
		busColl.limit(20);

		BusinessDto businesses = new BusinessDto();

		try {
			while (busColl.hasNext()) {
				BasicDBObject businessObj = (BasicDBObject) busColl.next();
				String business_id = businessObj.getString("business_id");
				String names = businessObj.getString("name");
				String categories = businessObj.getString("categories");
				String full_address = businessObj.getString("full_address");
				String hours = businessObj.getString("hours");
				String longitude = businessObj.getString("longitude");
				String latitude = businessObj.getString("latitude");

				Business business = new Business();
				business.setBusiness_id(business_id);
				business.setName(names);
				business.setCategories(categories);
				business.setFull_address(full_address);
				business.setHours(hours);
				business.setLongitude(longitude);
				business.setLatitude(latitude);

				businesses.addBusiness(business);

			}
		} finally {
			busColl.close();
		}

		return businesses;
	}





	@POST
	@Path("/publish")
	@Timed(name = "publish")
	public Response userPublish(@Context UriInfo uriInfo) {
		MultivaluedMap<String, String> queryParams = uriInfo
				.getQueryParameters();
		// System.out.println(queryParams.getFirst("name"));
		String businessName = queryParams.getFirst("name");
		System.out.println(businessName);
		String message = queryParams.getFirst("text");


		SNS sns = new SNS();
		sns.userPublishingToTopic(businessName, message);

		return Response.status(201).build();
	}


	@GET
	@Path("/{city}/{categories}")
	@Timed(name = "get-categories")
	public BusinessDto getCategory(@PathParam("city") String city,
			@PathParam("categories") String category) {

		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");
		BasicDBObject searchQuery = new BasicDBObject("city", city);
		searchQuery.append("categories", category);

		DBCursor myCol = coll.find(searchQuery);
		myCol.limit(15);

		BusinessDto businesses = new BusinessDto();

		try {
			while (myCol.hasNext()) {

				BasicDBObject businessObj = (BasicDBObject) myCol.next();
				String business_id = businessObj.getString("business_id");
				String categories = businessObj.getString("categories");
				String full_address = businessObj.getString("full_address");
				String hours = businessObj.getString("hours");

				Business business = new Business();
				business.setBusiness_id(business_id);
				business.setCategories(categories);
				business.setFull_address(full_address);
				business.setHours(hours);

				businesses.addBusiness(business);

			}
		} finally {
			myCol.close();
		}

		return businesses;
	}

	@GET
	@Path("/{city}/{categories}/{hoursDay}/{time1}/{time2}")
	@Timed(name = "get-timebased")
	public BusinessDto getTimeBased(@PathParam("city") String city,
			@PathParam("categories") String category,
			@PathParam("hoursDay") String day,
			@PathParam("time1") String startTime,
			@PathParam("time2") String endTime) {

		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");

		BasicDBObject searchQuery = new BasicDBObject("categories", category);
		searchQuery.append("city", city);
		searchQuery.append("open", true);

		searchQuery.append("hours." + day + ".open",
				new BasicDBObject("$lte", startTime)).append(
				"hours." + day + ".close", new BasicDBObject("$gt", endTime));
		DBCursor myCol = coll.find(searchQuery);
		myCol.limit(15);

		BusinessDto businesses = new BusinessDto();

		try {
			while (myCol.hasNext()) {

				BasicDBObject businessObj = (BasicDBObject) myCol.next();
				String business_id = businessObj.getString("business_id");
				String categories = businessObj.getString("categories");
				String full_address = businessObj.getString("full_address");
				String hours = businessObj.getString("hours");

				Business business = new Business();
				business.setBusiness_id(business_id);
				business.setCategories(categories);
				business.setFull_address(full_address);
				business.setHours(hours);

				businesses.addBusiness(business);

			}
		} finally {
			myCol.close();
		}

		return businesses;
	}

	@GET
	@Path("/{city}/{categories}/{when}")
	@Timed(name = "get-timebased")
	public BusinessDto getCurrentTime(@PathParam("city") String city,
			@PathParam("categories") String category,
			@PathParam("when") String when) {

		DB db = mongo.getDB("273project");
		DBCollection coll = db.getCollection("business");

		Calendar now = Calendar.getInstance();
		System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
				+ "-" + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR)
				+ "-" + now.getTime().getHours() + "-"
				+ now.getTime().getMinutes());

		String[] strDays = new String[] { "Sunday", "Monday", "Tuesday",
				"Wednesday", "Thusday", "Friday", "Saturday" };
		// Day_OF_WEEK starts from 1 while array index starts from 0
		String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
		int hours = now.getTime().getHours();
		// int hours = 7 ;
		int minutes = now.getTime().getMinutes();
		System.out.println("Current day is : " + day + "hours and minutes"
				+ hours + " " + minutes);

		String startTime;

		if (hours < 10) {
			startTime = "0" + hours + ":00";
		} else {
			startTime = hours + ":00";
		}

		BasicDBObject searchQuery = new BasicDBObject("categories", category);
		searchQuery.append("city", city);
		searchQuery.append("open", true);
		searchQuery.append("hours." + day + ".open",
				new BasicDBObject("$lt", startTime)).append(
				"hours." + day + ".close", new BasicDBObject("$gt", startTime));

		DBCursor cursor = coll.find(searchQuery);
		cursor.limit(10);

		BusinessDto businesses = new BusinessDto();

		try {
			while (cursor.hasNext()) {

				BasicDBObject businessObj = (BasicDBObject) cursor.next();
				String business_id = businessObj.getString("business_id");
				String categories = businessObj.getString("categories");
				String full_address = businessObj.getString("full_address");
				String hours_display = businessObj.getString("hours");

				Business business = new Business();
				business.setBusiness_id(business_id);
				business.setCategories(categories);
				business.setFull_address(full_address);
				business.setHours(hours_display);

				businesses.addBusiness(business);

			}
		} finally {
			cursor.close();
		}

		return businesses;
	}
	
}

package com.kaizen.yelp.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;

import com.kaizen.yelp.domain.Business;
import com.kaizen.yelp.domain.Recommendation;
import com.kaizen.yelp.domain.Review;
import com.kaizen.yelp.domain.Search;
import com.kaizen.yelp.domain.UserLogin;
import com.kaizen.yelp.jdbi.Mongo_Pojo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;



public class UserRepository {
	
	Mongo_Pojo mongo;
	DBCollection userInfo, review;
	DB db;
	ArrayList<Search> arrSList = new ArrayList<Search>();
	ArrayList<Business> arrBList = new ArrayList<Business>();
	ArrayList<Recommendation> arrRList = new ArrayList<Recommendation>();
	
	public UserRepository() {
			mongo = new Mongo_Pojo();
		try {
			MongoClient mongoclient = new MongoClient("localhost", 27017);
			db = mongoclient.getDB("273project");
			userInfo = db.getCollection("userInfo");
			review = db.getCollection("review");
		} catch (Exception e) {
			System.out.println("Can't connect");
		}
	}
	
	public UserLogin getUserbyUserName(String userName) {
		checkNotNull(userName, "User Name cannot be null");
		UserLogin tempUser = new UserLogin();
		DBObject user = userInfo.findOne(new BasicDBObject("_id", userName));
		if(!(user == null))
			tempUser = mongo.toUserObject(user.toString());
		return tempUser;
	}

	public boolean saveUser(UserLogin newUser) {
		checkNotNull(newUser, "newProduct instance cannot be null");
		DBObject tempProduct = mongo.toDbObject(newUser);
			userInfo.insert(tempProduct);
		return true;
	}
	
	public boolean saveReview(Review newReview) {
		checkNotNull(newReview, "newProduct instance cannot be null");
		DBObject tempProduct = mongo.toDbObject(newReview);
			review.insert(tempProduct);
		return true;
	}

	public void saveSearch(ArrayList<Search> searchList) {
	
		arrSList = searchList;
	}
	public ArrayList<Search> getSearch(){
		
		return arrSList;
	}
	
	public void saveReco(ArrayList<Recommendation> recoList) {
		
		arrRList = recoList;
	}
	public ArrayList<Recommendation> getReco(){
		
		return arrRList;
	}
	
	public void saveBusiness(ArrayList<Business> businessList) {
		
		arrBList = businessList;
	}
	
	public ArrayList<Business> getBusiness(){
		
		return arrBList;
	}
	
}

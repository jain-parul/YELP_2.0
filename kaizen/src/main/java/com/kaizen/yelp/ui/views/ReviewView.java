package com.kaizen.yelp.ui.views;

import java.util.ArrayList;

import com.kaizen.yelp.domain.Business;
import com.kaizen.yelp.domain.Review;
import com.kaizen.yelp.domain.UserLogin;
import com.yammer.dropwizard.views.View;

public class ReviewView extends View {
	
	UserLogin user = new UserLogin();
	String username = user.get_id();
	Business business = new Business();
	long review_count = 0;
	ArrayList<Review> reviewList = new ArrayList<Review>();
	String name = business.getName();
	public ReviewView(String user, String name, ArrayList<Review> reviewList, long review_count) {
		super("review.mustache");
		this.username = user;
		this.name = name;
		this.reviewList = reviewList;
		this.review_count = review_count;
	}

}

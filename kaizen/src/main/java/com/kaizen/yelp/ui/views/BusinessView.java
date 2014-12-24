package com.kaizen.yelp.ui.views;

import com.kaizen.yelp.domain.Business;
import com.kaizen.yelp.domain.UserLogin;
import com.yammer.dropwizard.views.View;

public class BusinessView extends View {
	
	UserLogin user = new UserLogin();
	Business business;
	
	String username = user.get_id();
	public BusinessView(String user, Business business) {
		super("business.mustache");
		this.username = user;
		this.business = business;
		
	}

}

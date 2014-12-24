package com.kaizen.yelp.ui.views;

import java.util.ArrayList;

import com.kaizen.yelp.domain.Recommendation;
import com.kaizen.yelp.domain.Search;
import com.kaizen.yelp.domain.UserLogin;
import com.yammer.dropwizard.views.View;

public class UserView extends View {
	
	UserLogin user = new UserLogin();
	ArrayList<Search> searchList = new ArrayList<Search>();
	ArrayList<Recommendation> recoList = new ArrayList<Recommendation>();
	
	String username = user.get_id();
	public UserView(String user, ArrayList<Search> searchList, ArrayList<Recommendation> recoList) {
		super("user.mustache");
		this.username = user;
		this.searchList = searchList;
		this.recoList =  recoList;
	}

}

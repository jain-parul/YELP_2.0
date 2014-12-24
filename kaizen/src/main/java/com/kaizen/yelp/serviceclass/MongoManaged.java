package com.kaizen.yelp.serviceclass;

import com.mongodb.Mongo;
import com.yammer.dropwizard.lifecycle.Managed;

public class MongoManaged implements Managed {
	private Mongo m;

	public MongoManaged(Mongo m) {
		this.m = m;

	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws Exception {
		m.close();
	}

}

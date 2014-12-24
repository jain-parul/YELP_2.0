package com.kaizen.yelp.serviceclass;

import com.kaizen.yelp.config.KaizenConfiguration;
import com.kaizen.yelp.repository.UserRepository;
import com.kaizen.yelp.ui.resources.BusinessResource;
import com.kaizen.yelp.ui.resources.CouponResource;
import com.kaizen.yelp.ui.resources.LoginResource;
import com.kaizen.yelp.ui.resources.ReviewResource;
import com.kaizen.yelp.ui.resources.UserResource;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;



public class KaizenService extends Service<KaizenConfiguration> {

	
	public static void main(String[] args) throws Exception {
		new KaizenService().run(args);
	}

	@Override
	public void initialize(Bootstrap<KaizenConfiguration> bootstrap) {
		bootstrap.setName("kaizen-service");
		bootstrap.addBundle(new ViewBundle());
		bootstrap.addBundle(new AssetsBundle());
	}

	@Override
	public void run(KaizenConfiguration configuration, Environment environment)
			throws Exception {
		//Testing Git Pulls
		Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport);
		DB db = mongo.getDB(configuration.mongodb);
		DBCollection coll = db.getCollection("docs");
	//JacksonDBCollection<Business, String> business = JacksonDBCollection.wrap(db.getCollection("docs"),Business.class,String.class);
		MongoManaged mongoManaged = new MongoManaged(mongo);
		environment.manage(mongoManaged);
		environment.addHealthCheck(new MongoHealthCheck(mongo));
		//environment.addResource(new KaizenResource( mongo,  coll));
		/** UI Resources */
		UserRepository userRepository = new UserRepository();
		environment.addResource(new LoginResource(userRepository));
		environment.addResource(new UserResource(mongo, userRepository));
		environment.addResource(new BusinessResource(mongo, userRepository));
		environment.addResource(new ReviewResource(mongo, userRepository));
		environment.addResource(new CouponResource(mongo));
	}

}

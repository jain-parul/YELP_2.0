package com.kaizen.yelp.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class KaizenConfiguration extends Configuration{
	@JsonProperty
	@NotEmpty
	public String mongohost = "127.0.0.1";
	public int mongoport = 27017;
	public String mongodb = "273project";

}

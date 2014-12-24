package com.kaizen.yelp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Recommendation {
	private String business_id;
	private String name;
	private String full_address;
	private float stars;
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFull_address() {
		return full_address;
	}
	public void setFull_address(String full_address) {
		this.full_address = full_address;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	

}

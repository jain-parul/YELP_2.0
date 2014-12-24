package com.kaizen.yelp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Review {
private String user_id;
private String business_id;
private String review_id;
private float rating;
private String review_content;
private String block;

public String getUser_id() {
	return user_id;
}
public void setUser_id(String user_id) {
	this.user_id = user_id;
}
public String getBusiness_id() {
	return business_id;
}
public void setBusiness_id(String business_id) {
	this.business_id = business_id;
}
public String getReview_id() {
	return review_id;
}
public void setReview_id(String review_id) {
	this.review_id = review_id;
}
public float getRating() {
	return rating;
}
public void setRating(float rating) {
	this.rating = rating;
}
public String getReview_content() {
	return review_content;
}
public void setReview_content(String review_content) {
	this.review_content = review_content;
}
public String getBlock() {
	return block;
}
public void setBlock(String block) {
	this.block = block;
}
}

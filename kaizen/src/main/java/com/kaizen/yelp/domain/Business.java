package com.kaizen.yelp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class Business {
private String business_id;
private String name;
private String full_address;
private String hours;
private String categories;
private float stars;
private String longitude;
private String latitude;
private String parking;
private String creditcards;
private String takeout;
private String wifi;
private String kids;
private String groups;
private float review_count;

public String getParking() {
	return parking;
}
public void setParking(String parking) {
	this.parking = parking;
}
public String getCreditcards() {
	return creditcards;
}
public void setCreditcards(String creditcards) {
	this.creditcards = creditcards;
}
public String getTakeout() {
	return takeout;
}
public void setTakeout(String takeout) {
	this.takeout = takeout;
}
public String getWifi() {
	return wifi;
}
public void setWifi(String wifi) {
	this.wifi = wifi;
}
public String getKids() {
	return kids;
}
public void setKids(String kids) {
	this.kids = kids;
}
public String getGroups() {
	return groups;
}
public void setGroups(String groups) {
	this.groups = groups;
}
public String getName() {
        return name;
}
public String getBusiness_id() {
	return business_id;
}
public void setBusiness_id(String business_id) {
	this.business_id = business_id;
}
public String getFull_address() {
	return full_address;
}
public void setFull_address(String full_address) {
	this.full_address = full_address;
}
public void setName(String name) {
        this.name = name;
}

public String getCategories() {
	return categories;
}

public void setCategories(String categories) {
	this.categories = categories;
}
public String getHours() {
	return hours;
}
public void setHours(String hours) {
	this.hours = hours;
}
public float getStars() {
	return stars;
}
public void setStars(float stars) {
	this.stars = stars;
}

public float getReview_count() {
	return review_count;
}
public void setReview_count(float review_count) {
	this.review_count = review_count;
}


public String getLongitude() {
	return longitude;
}
public void setLongitude(String longitude) {
	this.longitude = longitude;
}

public String getLatitude() {
	return latitude;
}
public void setLatitude(String latitude) {
	this.latitude = latitude;
}


}

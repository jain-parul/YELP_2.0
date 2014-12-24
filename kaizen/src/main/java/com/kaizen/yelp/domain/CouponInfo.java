package com.kaizen.yelp.domain;

public class CouponInfo {

	private String dealName;
	private String dealSource;
	private String dealImage;
	private String dealLink;


	public CouponInfo(String dealName , String dealSource , String dealImage, String dealLink) {
		this.dealName = dealName;
		this.dealSource = dealSource;
		this.dealImage = dealImage;
		this.dealLink = dealLink;

	}



	public String getDealName() {
		return dealName;
	}


	public void setDealName(String dealName) {
		this.dealName = dealName;
	}


	public String getDealSource() {
		return dealSource;
	}


	public void setDealSource(String dealSource) {
		this.dealSource = dealSource;
	}


	public String getDealImage() {
		return dealImage;
	}


	public void setDealImage(String dealImage) {
		this.dealImage = dealImage;
	}


	public String getDealLink() {
		return dealLink;
	}


	public void setDealLink(String dealLink) {
		this.dealLink = dealLink;
	}

	
}

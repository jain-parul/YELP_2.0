package com.kaizen.yelp.dto;

import java.util.ArrayList;
import java.util.List;

import com.kaizen.yelp.domain.Business;

public class BusinessDto {
	private List<Business> businesses = new ArrayList<Business>();
	
	public void addBusiness(Business business)
	{
		businesses.add(business);
	}
	/**
     * @return the businesses
     */
    public List<Business> getBusinesses() {
	return businesses;
    }

    /**
     * @param businesses
     *            the businesses to set
     */
    public void setBusinesses(List<Business> businesses) {
	this.businesses = businesses;
    }
}

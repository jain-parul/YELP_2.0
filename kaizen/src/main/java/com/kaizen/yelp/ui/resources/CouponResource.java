package com.kaizen.yelp.ui.resources;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.amazonaws.http.HttpResponse;
import com.kaizen.yelp.coupon.Coupon;
import com.kaizen.yelp.domain.CouponInfo;
import com.kaizen.yelp.ui.views.CouponView;
import com.mongodb.Mongo;

//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;


@Path("/kaizen/coupons/{username}/{category}/{zipcode}")
@Produces(MediaType.TEXT_HTML)

public class CouponResource {
	private Mongo mongo;

	public CouponResource(Mongo mongo) {
		this.mongo = mongo;
	}
	
	@GET

	public CouponView getCouponDetails(@PathParam("username") String username, @PathParam("category") String userSelectedCategory, @PathParam("zipcode") String zipcode) throws  Exception{
		System.out.println(" hello coupon details");

		//System.out.println(userSelectedCategory+" " + zipcode);

		String baseurl = "http://api.8coupons.com/v1/getsubcategory";

		Coupon coupon = new Coupon();
		StringBuilder contentsOfURL = coupon.getFromCouponApi(baseurl);
		//String userSelectedCategory = "Italian";
		//String userSelectedCategory = "Moroccan123";
		String categoryIdFromCoupon = coupon.getSubCategoryId(contentsOfURL, userSelectedCategory);
		ArrayList<CouponInfo> couponlist = null;
		
		try {
			
			
			
			//String zipcode = "85233" ;
			String newQuery = coupon.newQuery(zipcode, categoryIdFromCoupon);


			StringBuilder newQueryContents = coupon.getFromCouponApi(newQuery);
			 couponlist = coupon.getCouponDetails(newQueryContents);

			System.out.println(" coupon list" +couponlist.toString());

			for (int i = 0 ; i < couponlist.size() ; i++){

				System.out.println(" list: "+couponlist.get(i).toString());

			}
			
		} catch (ArrayIndexOutOfBoundsException ex) {
			
		System.out.println(" There are no coupons in this category");
		}
		
		catch (Exception e) {
			System.out.println(" There is some other problem");
		}

		return new CouponView(username,couponlist);
	}
}

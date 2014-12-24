package com.kaizen.yelp.dto;

import java.util.ArrayList;
import java.util.List;

import com.kaizen.yelp.domain.Review;

public class ReviewDto {
        private List<Review> reviews = new ArrayList<Review>();

        public void addReview(Review review)
        {
                reviews.add(review);
        }
        /**
     * @return the reviewes
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviewes
     *            the reviewes to set
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

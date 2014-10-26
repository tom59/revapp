package com.hcomemea.rev.domain;

import java.util.List;

public class HotelReviewSearchResult {

    private long hotelId;

    private List<SolrReview> reviews;

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List<SolrReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SolrReview> reviews) {
        this.reviews = reviews;
    }
}

package com.hcomemea.rev.domain;

import java.util.List;

public class Hotel extends SolrHotel {

    private List<SolrReview> reviews;

    public List<SolrReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SolrReview> reviews) {
        this.reviews = reviews;
    }
}

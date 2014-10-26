package com.hcomemea.rev.domain;

import java.util.List;

public class ReviewSearchResult {

    private List<SolrReview> reviews;
    
    private long totalNumberOfReviewsFound;

    public List<SolrReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SolrReview> reviews) {
        this.reviews = reviews;
    }

    public long getTotalNumberOfReviewsFound() {
        return totalNumberOfReviewsFound;
    }

    public void setTotalNumberOfReviewsFound(long totalNumberOfReviewsFound) {
        this.totalNumberOfReviewsFound = totalNumberOfReviewsFound;
    }
}

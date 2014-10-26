package com.hcomemea.rev.domain;

public class BestHotelSearchRequest {

    private Long destinationId;

    private String searchTerm;

    private int minNumberOfReviews = 5;

    private int maxNumberOfResults = 10;

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getMinNumberOfReviews() {
        return minNumberOfReviews;
    }

    public void setMinNumberOfReviews(int minNumberOfReviews) {
        this.minNumberOfReviews = minNumberOfReviews;
    }

    public int getMaxNumberOfResults() {
        return maxNumberOfResults;
    }

    public void setMaxNumberOfResults(int maxNumberOfResults) {
        this.maxNumberOfResults = maxNumberOfResults;
    }

    
}

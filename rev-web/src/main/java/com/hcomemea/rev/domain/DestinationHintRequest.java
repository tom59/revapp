package com.hcomemea.rev.domain;

public class DestinationHintRequest {

    private Long destinationId;

    private Double minimumScore = 0d;

    private int minimumNumberOfReviews = 50;

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Double getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(Double minimumScore) {
        this.minimumScore = minimumScore;
    }

    public int getMinimumNumberOfReviews() {
        return minimumNumberOfReviews;
    }

    public void setMinimumNumberOfReviews(int minimumNumberOfReviews) {
        this.minimumNumberOfReviews = minimumNumberOfReviews;
    }
}

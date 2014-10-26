package com.hcomemea.rev.domain;

public class DestinationHint {

    private String keyword;
    
    private int numberOfReviews;
    
    private double averageScore;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}

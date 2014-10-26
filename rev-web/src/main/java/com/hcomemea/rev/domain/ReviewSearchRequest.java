package com.hcomemea.rev.domain;

public class ReviewSearchRequest {

    private String keywords;
    private int resultsNumber = 100;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getResultsNumber() {
        return resultsNumber;
    }

    public void setResultsNumber(int resultsNumber) {
        this.resultsNumber = resultsNumber;
    }
}

package com.hcomemea.rev.domain;

import java.util.Map;

public class ReviewAnalysisResult {

    private int totalNumberOfReviews;

    private int numberOfDistinctTerms;

    private int totalNumberOfTerms;

    private Map<String, Integer> keywordsFrequency;

    public int getTotalNumberOfReviews() {
        return totalNumberOfReviews;
    }

    public void setTotalNumberOfReviews(int totalNumberOfReviews) {
        this.totalNumberOfReviews = totalNumberOfReviews;
    }

    public int getTotalNumberOfTerms() {
        return totalNumberOfTerms;
    }

    public void setTotalNumberOfTerms(int totalNumberOfTerms) {
        this.totalNumberOfTerms = totalNumberOfTerms;
    }

    public Map<String, Integer> getKeywordsFrequency() {
        return keywordsFrequency;
    }

    public void setKeywordsFrequency(Map<String, Integer> keywordsFrequency) {
        this.keywordsFrequency = keywordsFrequency;
    }

    public int getNumberOfDistinctTerms() {
        return numberOfDistinctTerms;
    }

    public void setNumberOfDistinctTerms(int numberOfDistinctTerms) {
        this.numberOfDistinctTerms = numberOfDistinctTerms;
    }
}

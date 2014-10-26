package com.hcomemea.rev.domain;

import java.util.List;

public class SemanticGroup {

    private String keyword;
            
    private double averageSemanticScore;

    private int keywordCount;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public double getAverageSemanticScore() {
        return averageSemanticScore;
    }

    public void setAverageSemanticScore(double averageSemanticScore) {
        this.averageSemanticScore = averageSemanticScore;
    }

    public int getKeywordCount() {
        return keywordCount;
    }

    public void setKeywordCount(int keywordCount) {
        this.keywordCount = keywordCount;
    }
}

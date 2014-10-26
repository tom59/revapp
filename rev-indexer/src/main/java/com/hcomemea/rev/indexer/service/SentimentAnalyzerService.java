package com.hcomemea.rev.indexer.service;

import java.util.Map;

public interface SentimentAnalyzerService {
    Map<String, Integer> getKeywordSentimentMap(String review);
}

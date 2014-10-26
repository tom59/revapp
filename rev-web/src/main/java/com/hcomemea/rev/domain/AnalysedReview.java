package com.hcomemea.rev.domain;

import java.util.Map;

public class AnalysedReview {

	private SolrReview review;

	private Map<String, Double> keywords;

	public SolrReview getReview() {
		return review;
	}

	public void setReview(SolrReview review) {
		this.review = review;
	}

	public Map<String, Double> getKeywords() {
		return keywords;
	}

	public void setKeywords(Map<String, Double> keywords) {
		this.keywords = keywords;
	}
}

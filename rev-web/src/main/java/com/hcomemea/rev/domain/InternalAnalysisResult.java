package com.hcomemea.rev.domain;

import java.util.List;

public class InternalAnalysisResult {

	public static final int REVIEW_TO_DISPLAY_PER_PAGE = 5;

	private long page;

	private List<AnalysedReview> analysedReviews;

	private long totalNumber;

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public List<AnalysedReview> getAnalysedReviews() {
		return analysedReviews;
	}

	public void setAnalysedReviews(List<AnalysedReview> analysedReviews) {
		this.analysedReviews = analysedReviews;
	}

	public long getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(long totalNumber) {
		this.totalNumber = totalNumber;
	}

	public boolean hasMore() {
		long index = (page - 1) * REVIEW_TO_DISPLAY_PER_PAGE + analysedReviews.size();
		return totalNumber > index;
	}
}

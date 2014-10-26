package com.hcomemea.rev.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcomemea.rev.domain.AnalysedReview;
import com.hcomemea.rev.domain.InternalAnalysisResult;
import com.hcomemea.rev.domain.ReviewSearchResult;
import com.hcomemea.rev.domain.SolrReview;
import com.hcomemea.rev.service.InternalAnalysisService;
import com.hcomemea.rev.service.ReviewSearchService;

//TODO To implement analysis service
@Service
public class InternalAnalysisServiceImpl implements InternalAnalysisService {

	@Autowired
	ReviewSearchService reviewSearchService;

//	@Autowired
//	SentenceAnalysisService sentenceAnalysisService;

	@Override
	public InternalAnalysisResult getAnalysedReviews(int page) {
		InternalAnalysisResult internalAnalysisResult = new InternalAnalysisResult();
		ReviewSearchResult reviewSearchResult
			= reviewSearchService.loadReviews(page, InternalAnalysisResult.REVIEW_TO_DISPLAY_PER_PAGE);

		List<AnalysedReview> analysedReviews = new ArrayList<>();
		for (SolrReview review : reviewSearchResult.getReviews()) {
			AnalysedReview analysedReview = new AnalysedReview();
//			analysedReview.setKeywords(sentenceAnalysisService.getSummarizedContent(review.getContent()));
			analysedReview.setReview(review);
			analysedReviews.add(analysedReview);
		}
		internalAnalysisResult.setAnalysedReviews(analysedReviews);
		internalAnalysisResult.setPage(page);
		internalAnalysisResult.setTotalNumber(reviewSearchResult.getTotalNumberOfReviewsFound());
		return internalAnalysisResult;
	}

}

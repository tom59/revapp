package com.hcomemea.rev.service.impl;

import com.hcomemea.rev.dao.ReviewSolrDao;
import com.hcomemea.rev.domain.BestHotelSearchRequest;
import com.hcomemea.rev.domain.BestHotelSearchResponse;
import com.hcomemea.rev.domain.DestinationHintRequest;
import com.hcomemea.rev.domain.DestinationHintResponse;
import com.hcomemea.rev.domain.HotelReviewSearchRequest;
import com.hcomemea.rev.domain.HotelReviewSearchResult;
import com.hcomemea.rev.domain.ReviewAnalysisRequest;
import com.hcomemea.rev.domain.ReviewAnalysisResult;
import com.hcomemea.rev.domain.ReviewSearchRequest;
import com.hcomemea.rev.domain.ReviewSearchResult;
import com.hcomemea.rev.domain.SemanticSearchRequest;
import com.hcomemea.rev.domain.SemanticSearchResult;
import com.hcomemea.rev.service.ReviewSearchService;

public class ReviewSearchServiceImpl implements ReviewSearchService{

    private ReviewSolrDao reviewSolrDao;

    @Override
    public ReviewSearchResult search(ReviewSearchRequest request) {
        return reviewSolrDao.findReviews(request);
    }

    public void setReviewSolrDao(ReviewSolrDao reviewSolrDao) {
        this.reviewSolrDao = reviewSolrDao;
    }

    @Override
    public ReviewAnalysisResult analyse(ReviewAnalysisRequest request) {
        return reviewSolrDao.analyseReviews(request);
    }

    public SemanticSearchResult semanticSearch(SemanticSearchRequest request) {
        return reviewSolrDao.findHotel(request);
    }

    public HotelReviewSearchResult findHotelReviews(HotelReviewSearchRequest request) {
        return reviewSolrDao.findHotelReviews(request);
    }

    public BestHotelSearchResponse findBestHotelReviewSearchResult(BestHotelSearchRequest request) {
        return reviewSolrDao.findBestHotelReviewSearchResult(request);
    }

    public DestinationHintResponse findDestinationHint(DestinationHintRequest request) {
        return reviewSolrDao.findDestinationHint(request);
    }

    public ReviewSearchResult loadReviews(int page, int reviewsPerPage) {
    	return reviewSolrDao.loadReviews(page, reviewsPerPage);
    }

}

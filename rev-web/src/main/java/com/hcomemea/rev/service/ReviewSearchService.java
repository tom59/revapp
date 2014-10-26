package com.hcomemea.rev.service;

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

public interface ReviewSearchService {

    public ReviewSearchResult search(ReviewSearchRequest request);
    
    public ReviewAnalysisResult analyse(ReviewAnalysisRequest request);

    public SemanticSearchResult semanticSearch(SemanticSearchRequest request);

    public HotelReviewSearchResult findHotelReviews(HotelReviewSearchRequest request);

    public BestHotelSearchResponse findBestHotelReviewSearchResult(BestHotelSearchRequest request);

    public DestinationHintResponse findDestinationHint(DestinationHintRequest request);

    public ReviewSearchResult loadReviews(int page, int reviewsPerPage);
}

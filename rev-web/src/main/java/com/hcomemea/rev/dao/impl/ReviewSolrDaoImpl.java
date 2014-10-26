package com.hcomemea.rev.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcomemea.rev.dao.ReviewSolrDao;
import com.hcomemea.rev.domain.BestHotelSearchRequest;
import com.hcomemea.rev.domain.BestHotelSearchResponse;
import com.hcomemea.rev.domain.DestinationHint;
import com.hcomemea.rev.domain.DestinationHintRequest;
import com.hcomemea.rev.domain.DestinationHintResponse;
import com.hcomemea.rev.domain.Hotel;
import com.hcomemea.rev.domain.HotelReviewSearchRequest;
import com.hcomemea.rev.domain.HotelReviewSearchResult;
import com.hcomemea.rev.domain.InternalAnalysisResult;
import com.hcomemea.rev.domain.ReviewAnalysisRequest;
import com.hcomemea.rev.domain.ReviewAnalysisResult;
import com.hcomemea.rev.domain.ReviewSearchRequest;
import com.hcomemea.rev.domain.ReviewSearchResult;
import com.hcomemea.rev.domain.SemanticGroup;
import com.hcomemea.rev.domain.SemanticSearchRequest;
import com.hcomemea.rev.domain.SemanticSearchResult;
import com.hcomemea.rev.domain.SolrHotel;
import com.hcomemea.rev.domain.SolrReview;

public class ReviewSolrDaoImpl implements ReviewSolrDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewSolrDaoImpl.class);
    private final SolrServer solrServer;

    /**
     * Constructor requiring an instance of a solr server.
     * @param solrServer Solr Server instance.
     */
    public ReviewSolrDaoImpl(final SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    public ReviewSearchResult findReviews(ReviewSearchRequest request) {
        ReviewSearchResult result = new ReviewSearchResult();
        SolrQuery solrQuery = buildReviewQuery(request.getKeywords(), request.getResultsNumber());
        try {
            QueryResponse response = solrServer.query(solrQuery);
            List<SolrReview> reviews = response.getBeans(SolrReview.class);
            
            result.setReviews(reviews);
            result.setTotalNumberOfReviewsFound(response.getResults().getNumFound());
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public SemanticSearchResult findHotel(SemanticSearchRequest request) {
        SemanticSearchResult result = new SemanticSearchResult();
        SolrQuery solrQuery = buildHotelQuery(request.getHotelId());
        try {
            QueryResponse response = solrServer.query(solrQuery);
            List<SolrHotel> hotels = response.getBeans(SolrHotel.class);

            if(hotels != null && hotels.size() == 1) {
                result.setHotelId(hotels.get(0).getHotelId());

                List<SemanticGroup> semanticGroups = new ArrayList<>();
                for (String semanticCountWord :  hotels.get(0).getSemanticCount().keySet()) {
                    String semanticWord = semanticCountWord.substring("semantic_count_".length());
                    SemanticGroup semanticGroup = new SemanticGroup();
                    semanticGroup.setKeywordCount(hotels.get(0).getSemanticCount().get("semantic_count_" + semanticWord));
                    semanticGroup.setAverageSemanticScore(hotels.get(0).getSemanticScore().get("semantic_score_" + semanticWord));
                    semanticGroup.setKeyword(semanticWord);
                    semanticGroups.add(semanticGroup);
                }
                result.setSemanticGroups(semanticGroups);
            }
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public SolrQuery buildHotelQuery(long hotelId) {
        SolrQuery query = new SolrQuery();
        query.set("q", "hotel_id:" + hotelId);
        query.addFilterQuery("type:HOTEL");
        query.setIncludeScore(true);
        return query;
    }

    public SolrQuery buildReviewQuery(String queryText, int resultCount) {
        SolrQuery query = new SolrQuery(escapeText(queryText));
        query.setRows(resultCount);
        query.set("q", queryText);
        query.addFilterQuery("type:REVIEW");
        query.setIncludeScore(true);
        return query;
    }

    public static String escapeText(String text) {
        String escapedText = "";
        if (text != null) {
            escapedText = text.replace("&", " and ").replace(",", " ");
            escapedText = QueryParser.escape(escapedText).toLowerCase();
        }
        return escapedText;
    }

    @SuppressWarnings("unchecked")
    public ReviewAnalysisResult analyseReviews(ReviewAnalysisRequest request) {
        ReviewAnalysisResult result = new ReviewAnalysisResult();
        try {
            SolrQuery query = new SolrQuery();
            query.setQueryType("/admin/luke");
            query.setParam("fl", "review_content");
            query.setParam("numTerms", request.getResultsNumber().toString());
            QueryResponse response = solrServer.query(query);

            SimpleOrderedMap<Object> fields = (SimpleOrderedMap<Object>)response.getResponse().get("fields");
            SimpleOrderedMap<Object> relevantWords = (SimpleOrderedMap<Object> ) fields.get("review_content");
            NamedList<Object> topTerms = (NamedList<Object>)relevantWords.get("topTerms");
            Map<String, Integer> keywordsFrequency = new LinkedHashMap<String, Integer>();
            for (int i = 0; i< topTerms.size(); i++) {
                keywordsFrequency.put(topTerms.getName(i), (Integer)topTerms.getVal(i));
            }
            result.setKeywordsFrequency(keywordsFrequency);
            result.setNumberOfDistinctTerms((Integer) relevantWords.get("distinct"));

            SimpleOrderedMap<Object> index = (SimpleOrderedMap<Object>)response.getResponse().get("index");
            result.setTotalNumberOfTerms((Integer) index.get("numTerms"));
            result.setTotalNumberOfReviews((Integer) index.get("numDocs"));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public HotelReviewSearchResult findHotelReviews(HotelReviewSearchRequest request) {
        HotelReviewSearchResult hotelReviewSearchResult = new HotelReviewSearchResult();
        try {
            SolrQuery query = new SolrQuery(escapeText(request.getKeyword()));
            query.setRows(100);
            query.set("q", request.getKeyword());
            query.addFilterQuery("type:REVIEW AND hotel_id:" + request.getHotelId());

            QueryResponse response = solrServer.query(query);

            List<SolrReview> reviews = response.getBeans(SolrReview.class);
            hotelReviewSearchResult.setHotelId(request.getHotelId());
            hotelReviewSearchResult.setReviews(reviews);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }

        return hotelReviewSearchResult;
    }

    public BestHotelSearchResponse findBestHotelReviewSearchResult(BestHotelSearchRequest request) {
        BestHotelSearchResponse bestHotelSearchResponse = new BestHotelSearchResponse();
        try {
            List<Hotel> hotels = new ArrayList<>();
            String[] words = request.getSearchTerm().split(" ");
            String query = "";
            String sortFields = "";
            for(String word : words) {
                if (query.length() > 0) {
                    query = query + " AND ";
                    sortFields = sortFields + ", ";
                }
                query = query + "semantic_score_" + word+ ":*";
                sortFields = sortFields + "semantic_score_" + word;
            }
            SolrQuery hotelQuery = new SolrQuery(query);
            hotelQuery.addFilterQuery("type:HOTEL");
            hotelQuery.setIncludeScore(true);
//            hotelQuery.setSortField("sum(" + sortFields + ")", ORDER.desc);
//            hotelQuery.addFilterQuery("sum(" + sortFields + ") : [" + request.getMinNumberOfReviews() + " TO *]");
            hotelQuery.setRows(10);
            QueryResponse response = solrServer.query(hotelQuery);
            List<SolrHotel> solrHotels = response.getBeans(SolrHotel.class);

            for (SolrHotel solrHotel : solrHotels) {
                HotelReviewSearchRequest hotelReviewRequest = new HotelReviewSearchRequest();
                hotelReviewRequest.setHotelId(solrHotel.getHotelId());
                hotelReviewRequest.setKeyword(request.getSearchTerm());
                HotelReviewSearchResult hotelReviewResults = findHotelReviews(hotelReviewRequest);
                if (hotelReviewResults != null && hotelReviewResults.getReviews().size() > 0) {
                    Hotel hotel = new Hotel();
                    hotel.setHotelId(solrHotel.getHotelId());
                    hotel.setReviews(hotelReviewResults.getReviews());
                    hotels.add(hotel);
                }
            }
            bestHotelSearchResponse.setHotels(hotels);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }
        return bestHotelSearchResponse;
    }

    public DestinationHintResponse findDestinationHint(DestinationHintRequest request) {
        DestinationHintResponse destinationHintResponse = new DestinationHintResponse();
        try {
            SolrQuery hotelQuery = new SolrQuery("destination_id:" + request.getDestinationId());
            hotelQuery.addFilterQuery("type:HOTEL");
            hotelQuery.setIncludeScore(true);
            hotelQuery.setRows(10000);
            QueryResponse response = solrServer.query(hotelQuery);
            List<SolrHotel> hotels = response.getBeans(SolrHotel.class);
            Map<String, Integer> numberOfHints = new HashMap<String, Integer>();
            Map<String, List<Double>> scores = new HashMap<String, List<Double>>();
            for (SolrHotel hotel : hotels) {
                for (String semanticScoreKey : hotel.getSemanticScore().keySet()) {
                    String semanticKey = semanticScoreKey.substring("semantic_score_".length());
                    if (numberOfHints.get(semanticKey) == null) {
                        numberOfHints.put(semanticKey, 0);
                        scores.put(semanticKey, new ArrayList<Double>());
                    }
                    numberOfHints.put(semanticKey, numberOfHints.get(semanticKey) + hotel.getSemanticCount().get("semantic_count_"+semanticKey));
                    scores.get(semanticKey).add(hotel.getSemanticScore().get("semantic_score_"+semanticKey));
                }
            }
            //Count average score
            List<DestinationHint> hints = new ArrayList<>();
            for (String key : scores.keySet()) {
                double count = 0;
                for (int i = 0; i < scores.get(key).size(); i++) {
                    count = count + scores.get(key).get(i);
                }
                double averageScore = count / scores.get(key).size();
                if (averageScore >= request.getMinimumScore() && numberOfHints.get(key) >= request.getMinimumNumberOfReviews() ) {
                    DestinationHint hint = new DestinationHint();
                    hint.setAverageScore(averageScore);
                    hint.setKeyword(key);
                    hint.setNumberOfReviews(numberOfHints.get(key));
                    hints.add(hint);
                }
            }
            destinationHintResponse.setHints(hints);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }
        return destinationHintResponse;
    }

    public ReviewSearchResult loadReviews(int page, int reviewsPerPage) {
        ReviewSearchResult result = new ReviewSearchResult();
        SolrQuery solrQuery = buildReviewQuery("*:*", InternalAnalysisResult.REVIEW_TO_DISPLAY_PER_PAGE);
        solrQuery.setStart((page - 1) * reviewsPerPage);
        try {
            QueryResponse response = solrServer.query(solrQuery);
            List<SolrReview> reviews = response.getBeans(SolrReview.class);
            
            result.setReviews(reviews);
            result.setTotalNumberOfReviewsFound(response.getResults().getNumFound());
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}

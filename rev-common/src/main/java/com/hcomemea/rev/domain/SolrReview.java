package com.hcomemea.rev.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;


public class SolrReview {

    @Field("review_id")
    private Long id;

    @Field("review_content")
    private String content;

    @Field("hotel_id")
    private Long hotelId;

    @Field("semantic_score_*")
    private Map<String, Double> semanticScore = new HashMap<>();

    @Field("type")
    private String type = "REVIEW";

    @Field("destination_id")
    private Long destinationId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public Map<String, Double> getSemanticScore() {
        return semanticScore;
    }

    public void setSemanticScore(Map<String, Double> semanticScore) {
        this.semanticScore = semanticScore;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
}

package com.hcomemea.rev.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

public class SolrHotel {

    @Field("hotel_id")
    private Long hotelId;

    @Field("type")
    private String type = "HOTEL";

    @Field("semantic_score_*")
    private Map<String, Double> semanticScore = new HashMap<>();

    @Field("semantic_count_*")
    private Map<String, Integer> semanticCount = new HashMap<>();

    @Field("destination_id")
    private Long destinationId;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Double> getSemanticScore() {
        return semanticScore;
    }

    public void setSemanticScore(Map<String, Double> semanticScore) {
        this.semanticScore = semanticScore;
    }

    public Map<String, Integer> getSemanticCount() {
        return semanticCount;
    }

    public void setSemanticCount(Map<String, Integer> semanticCount) {
        this.semanticCount = semanticCount;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
}

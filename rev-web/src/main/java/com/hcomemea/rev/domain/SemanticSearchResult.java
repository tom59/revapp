package com.hcomemea.rev.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticSearchResult {

    private long hotelId;

    private List<SemanticGroup> semanticGroups  = new ArrayList<>();

    public List<SemanticGroup> getSemanticGroups() {
        return semanticGroups;
    }

    public void setSemanticGroups(List<SemanticGroup> semanticGroups) {
        this.semanticGroups = semanticGroups;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }
}

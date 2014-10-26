package com.hcomemea.rev.indexer.dao;

import java.util.List;

import com.hcomemea.rev.domain.SolrHotel;
import com.hcomemea.rev.domain.SolrReview;

public interface IndexerDao {

    public void deleteAll();
    
    public void index(SolrReview review);

    public void index(List<SolrReview> reviews);

    public void indexHotel(SolrHotel hotel);

    public void indexHotels(List<SolrHotel> hotels);

    public void commit();
}

package com.hcomemea.rev.indexer.dao.impl;

import java.util.List;

import com.hcomemea.rev.domain.SolrHotel;
import com.hcomemea.rev.domain.SolrReview;
import com.hcomemea.rev.indexer.dao.IndexerDao;
import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Indexer implementation based on Solr.
 */
public class IndexerDaoSolr implements IndexerDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexerDaoSolr.class);

    private final SolrServer solrServer;

    /**
     * Constructor requiring an instance of a solr server.
     * @param solrServer Solr Server instance.
     */
    public IndexerDaoSolr(final SolrServer solrServer) {
        this.solrServer = solrServer;
    }


    @Override
    public void deleteAll() {
        try {
            LOGGER.info("Deleting entities");
            solrServer.deleteByQuery("*:*");
            LOGGER.info("Entities have been deleted");
        } catch (Exception e) {
            LOGGER.error("An error occurred while deleting entities", e);
        }
    }

    @Override
    public void index(SolrReview solrEntity) {
        try {
            LOGGER.info("Adding 1 entity");
            solrServer.addBean(solrEntity);
            LOGGER.info("Entities have been added");
        } catch (Exception e) {
            LOGGER.error("An error occurred while indexing entities", e);
        }
    }

    @Override
    public void index(List<SolrReview> solrEntities) {
        try {
            LOGGER.info("Adding " + solrEntities.size() + " reviews");
            solrServer.addBeans(solrEntities);
            LOGGER.info("Entities have been added");
        } catch (Exception e) {
            LOGGER.error("An error occurred while indexing entities", e);
        }
    }

    @Override
    public void indexHotel(SolrHotel solrEntity) {
        try {
            LOGGER.info("Adding 1 hotel");
            solrServer.addBean(solrEntity);
            LOGGER.info("Entities have been added");
        } catch (Exception e) {
            LOGGER.error("An error occurred while indexing entities", e);
        }
    }

    @Override
    public void indexHotels(List<SolrHotel> solrEntities) {
        try {
            LOGGER.info("Adding " + solrEntities.size() + " hotels");
            solrServer.addBeans(solrEntities);
            LOGGER.info("Entities have been added");
        } catch (Exception e) {
            LOGGER.error("An error occurred while indexing entities", e);
        }
    }

    @Override
    public void commit() {
        try {
            solrServer.commit();
        } catch (Exception e) {
            LOGGER.error("An error occurred while commiting", e);
        }
    }

}

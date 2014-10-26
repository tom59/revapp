package com.hcomemea.rev.indexer;

import com.hcomemea.rev.indexer.service.ReviewIndexerService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReviewIndexer {

    public static void main(String args[]) {
        ClassPathXmlApplicationContext ctxt = new ClassPathXmlApplicationContext("setup-context.xml");
        ReviewIndexerService indexerService = ctxt.getBean(ReviewIndexerService.class);
        indexerService.indexReviewsForDestination(712491L);
        ctxt.close();
    }
}

package com.hcomemea.rev.indexer.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.hotels.geography.search.filter.SimpleAttributeFilters.is;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.hcomemea.common.collections.PackedListIterator;
import com.hcomemea.rev.domain.SolrHotel;
import com.hcomemea.rev.domain.SolrReview;
import com.hcomemea.rev.indexer.dao.HotelReviewDao;
import com.hcomemea.rev.indexer.dao.IndexerDao;
import com.hcomemea.rev.indexer.service.ReviewIndexerService;
import com.hcomemea.rev.indexer.service.SentimentAnalyzerService;
import com.hcomemea.review.domain.HotelReview;
import com.hotels.assembly.search.domain.area.GeoCircle;
import com.hotels.geography.domain.Area;
import com.hotels.geography.domain.AreaVariant;
import com.hotels.geography.domain.Circle;
import com.hotels.geography.domain.Coordinate;
import com.hotels.geography.domain.Distance;
import com.hotels.geography.domain.DistanceUnit;
import com.hotels.geography.domain.GeographicEntity;
import com.hotels.geography.domain.GeographicEntityAttribute;
import com.hotels.geography.domain.GeographicEntityType;
import com.hotels.geography.domain.function.GeographicFunctionFactory;
import com.hotels.geography.search.query.GeographyQuery;
import com.hotels.geography.search.query.GeographySearchResult;
import com.hotels.geography.search.query.GeographySearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewIndexerServiceImpl implements ReviewIndexerService {

    private static final int DEFAULT_PACKAGE_SIZE = 500;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewIndexerServiceImpl.class);
    private static final Distance DEFAULT_HOTEL_SEARCH_RADIUS = new Distance(DistanceUnit.MILE, 25.0);
    private GeographySearchService geographySearchService;
    private GeographicFunctionFactory functions;
    private HotelReviewDao hotelReviewDao;
    private IndexerDao indexerDao;
    private SentimentAnalyzerService sentimentAnalyzerService;
    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(8));

    @Override
    public void indexReviewsForDestination(final Long destinationId) {
        Area geographicArea = getGeographicArea(destinationId.toString());
        List<Long> hotelIds = findHotelsInDestination(geographicArea);
        LOGGER.info(hotelIds.size() + " hotels found in destination");
//        hotelIds = hotelIds.subList(1, MAX_NUMBER_OF_HOTELS_TO_INDEX);
//        hotelIds = Arrays.asList(hotelIds.get(0));
        indexerDao.deleteAll();

        PackedListIterator<List<Long>> hotelIdPackIterator = new PackedListIterator<List<Long>>(new ArrayList<>(hotelIds), DEFAULT_PACKAGE_SIZE);
        int i = 0;
        final AtomicInteger count = new AtomicInteger();
        while (hotelIdPackIterator.hasNext()) {
            LOGGER.info("Loading next Hotel pack [" + i + "/" + hotelIds.size() + "] ...");
            List<Long> hotelIdsToProcess =  hotelIdPackIterator.next();
            i += DEFAULT_PACKAGE_SIZE;

            List<HotelReview> reviews = hotelReviewDao.getReviewsByHotelIds(hotelIdsToProcess);
            LOGGER.info(reviews.size() + " reviews found");

            Multimap<Long, HotelReview> hotelReviewMap = Multimaps.index(reviews, new Function<HotelReview, Long>() {
                @Override
                public Long apply(HotelReview input) {
                    return input.getHotelId();
                }
            });

            for (Map.Entry<Long, Collection<HotelReview>> entry : hotelReviewMap.asMap().entrySet()) {
                List<ListenableFuture<Map<String, Integer>>> futureList = newArrayList();
                final Queue<SolrReview> solrReviews = new ConcurrentLinkedQueue<>();
                Long hotelId = entry.getKey();
                for (final HotelReview review : entry.getValue()) {
                    final SolrReview solrReview = new SolrReview();
                    solrReview.setId(review.getId());
                    solrReview.setContent(review.getBody());
                    solrReview.setHotelId(review.getHotelId());
                    solrReview.setDestinationId(destinationId);
                    ListenableFuture<Map<String, Integer>> listenableFuture = service.submit(new Callable<Map<String, Integer>>() {
                        @Override
                        public Map<String, Integer> call() {
                            return sentimentAnalyzerService.getKeywordSentimentMap(review.getBody());
                        }
                    });

                    Futures.addCallback(listenableFuture, new FutureCallback<Map<String, Integer>>() {
                        @Override
                        public void onSuccess(Map<String, Integer> keywordSentimentMap) {
                            for (Map.Entry<String, Integer> entry : keywordSentimentMap.entrySet()) {
                                solrReview.getSemanticScore().put("semantic_score_" + entry.getKey(), Double.valueOf(entry.getValue()));
                            }
                            solrReviews.offer(solrReview);
                            //Do some logging
                            int reviewCount = count.incrementAndGet();
                            if (reviewCount % 100 == 0) {
                                LOGGER.info("Parsed {} reviews", reviewCount);
                            }
                        }

                        @Override
                        public void onFailure(Throwable thrown) {
                        }
                    });
                    futureList.add(listenableFuture);
                }
                if (!futureList.isEmpty()) {
                    addListenerForBulkInsert(futureList, hotelId, destinationId, solrReviews);
                } else {
                    LOGGER.info("Hotel with id {} does not have any review. Skipping...", hotelId);
                }
            }

        }
        LOGGER.info("The reviews have been indexed.");
    }
    
    private void addListenerForBulkInsert(List<ListenableFuture<Map<String, Integer>>> futureList, final Long hotelId, final Long destinationId, final Queue<SolrReview> solrReviews) {
        ListenableFuture<List<Map<String, Integer>>> successfulQueries = Futures.successfulAsList(futureList);

        Futures.addCallback(successfulQueries, new FutureCallback<List<Map<String, Integer>>>() {
            @Override
            public void onSuccess(List<Map<String, Integer>> result) {
                List<SolrReview> list = newArrayList();
                while (!solrReviews.isEmpty()) {
                    list.add(solrReviews.poll());
                }
                indexerDao.index(list);
                indexHotel(hotelId, destinationId, list);
                indexerDao.commit();
                LOGGER.info("Indexed hotel with id {} with {} reviews", hotelId, list.size());
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void indexHotel(Long hotelId, Long destinationId, List<SolrReview> solrReviews) {
        Map<String, Collection<Double>> semanticScores = getSemanticScores(solrReviews);
        Map<String, Integer> semanticCount = newHashMap();
        Map<String, Double> averageSemanticScores = newHashMap();
        for (String semanticWord : semanticScores.keySet()) {
            averageSemanticScores.put(semanticWord, getAverageScore(semanticScores.get(semanticWord)));
            semanticCount.put("semantic_count_" + semanticWord.substring("semantic_score_".length()) , semanticScores.get(semanticWord).size());
        }
        SolrHotel solrHotel = new SolrHotel();
        solrHotel.setHotelId(hotelId);
        solrHotel.setDestinationId(destinationId);
        solrHotel.setSemanticScore(averageSemanticScores);
        solrHotel.setSemanticCount(semanticCount);
        indexerDao.indexHotel(solrHotel);
    }

    private double getAverageScore(Collection<Double> scores) {
        double sum = 0d;
        for (Double score : scores) {
            sum += score;
        }
        return sum / scores.size();
    }

    private Map<String, Collection<Double>> getSemanticScores(List<SolrReview> hotelReviews) {
        Multimap<String, Double> semanticScores = ArrayListMultimap.create();
        for (SolrReview review : hotelReviews) {
            for (String keyword : review.getSemanticScore().keySet()) {
                semanticScores.put(keyword, review.getSemanticScore().get(keyword));
            }
        }
        return semanticScores.asMap();
    }

    private Area getGeographicArea(String destinationId) {
        Area geographicArea = null;
        GeographySearchResult geoResult = geographySearchService.getEntities(
                new GeographyQuery.GeographyQueryBuilder()
                    .entity(GeographicEntityType.CITY, GeographicEntityType.NEIGHBORHOOD)
                    .withAreaVariant(AreaVariant.CHAMPION)
                    .filter(is(GeographicEntityAttribute.MHOTEL_DESTINATION_ID, destinationId)).build());
    
        if (geoResult != null && geoResult.size() > 0) {
            GeographicEntity destinationEntity = geoResult.getFirst();
            Area area = destinationEntity.getArea();
            Coordinate searchAreaCenter = area.apply(functions.geographicCenter());
            GeoCircle expandedArea = new GeoCircle(new com.hotels.assembly.search.domain.area.Coordinate(searchAreaCenter.getLatitude(),
                    searchAreaCenter.getLongitude()), (float) DEFAULT_HOTEL_SEARCH_RADIUS.valueAs(DistanceUnit.KILOMETER));
            geographicArea = new Circle(new Coordinate(expandedArea.getCenter().getLatitude(), expandedArea.getCenter().getLongitude()), 
                    new Distance(DistanceUnit.KILOMETER, expandedArea.getRadius().doubleValue()));
        }
        return geographicArea;
    }

    private List<Long> findHotelsInDestination(Area expandedArea) {
        List<Long> hotelIds = new ArrayList<>();
        GeographyQuery.GeographyQueryBuilder builder = new GeographyQuery.GeographyQueryBuilder();
        builder.entity(GeographicEntityType.HOTEL);
        builder.insideOf(expandedArea);

        GeographySearchResult geoResult = geographySearchService.getEntities(builder.build());
        if (geoResult != null) {
            for (GeographicEntity geographicEntity : geoResult) {
                hotelIds.add(Long.valueOf((String)geographicEntity.getAttribute(GeographicEntityAttribute.MHOTEL_DESTINATION_ID)));
            }
        }
        return hotelIds;
    }

    public void setGeographySearchService(GeographySearchService geographySearchService) {
        this.geographySearchService = geographySearchService;
    }

    public void setFunctions(GeographicFunctionFactory functions) {
        this.functions = functions;
    }

    public void setHotelReviewDao(HotelReviewDao hotelReviewDao) {
        this.hotelReviewDao = hotelReviewDao;
    }

    public void setIndexerDao(IndexerDao indexerDao) {
        this.indexerDao = indexerDao;
    }

    @Autowired
    public void setSentimentAnalyzerService(SentimentAnalyzerService sentimentAnalyzerService) {
        this.sentimentAnalyzerService = sentimentAnalyzerService;
    }
}

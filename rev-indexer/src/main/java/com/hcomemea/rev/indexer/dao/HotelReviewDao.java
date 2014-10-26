package com.hcomemea.rev.indexer.dao;


import java.util.List;

import com.hcomemea.common.dao.EntityDAO;
import com.hcomemea.common.ibatis.daofactory.SQLMapOperation;
import com.hcomemea.review.domain.HotelReview;

public interface HotelReviewDao extends EntityDAO<HotelReview> {

    @SQLMapOperation(name = "getReviewsByHotelIds", parameters = {"hotelids"})
    public List<HotelReview> getReviewsByHotelIds(List<Long> hotelids);
}

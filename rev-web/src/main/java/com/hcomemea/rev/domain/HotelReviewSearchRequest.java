package com.hcomemea.rev.domain;

public class HotelReviewSearchRequest {

    private Long hotelId;

    private String keyword;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

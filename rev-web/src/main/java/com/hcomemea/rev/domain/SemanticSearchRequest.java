package com.hcomemea.rev.domain;

public class SemanticSearchRequest {

    private long hotelId;
    private float guestReviews;

    public long getHotelId() {
        return hotelId;
    }
    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }
    public float getGuestReviews() {
        return guestReviews;
    }
    public void setGuestReviews(float guestReviews) {
        this.guestReviews = guestReviews;
    }
}

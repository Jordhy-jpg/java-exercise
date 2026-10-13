package com.exercise.publicapi.dto;

public class CreateListingResponseDto {

    private ListingDto listing;

    public ListingDto getListing() {
        return listing;
    }

    public void setListing(ListingDto listing) {
        this.listing = listing;
    }
}

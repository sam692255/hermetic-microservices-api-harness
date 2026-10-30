package com.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferPayload {
    @JsonProperty("offerId")
    private String offerId;

    @JsonProperty("cardTier")
    private String cardTier;

    @JsonProperty("cashbackPercentage")
    private double cashbackPercentage;

    @JsonProperty("active")
    private boolean active;

    public OfferPayload() {}

    public OfferPayload(String offerId, String cardTier, double cashbackPercentage, boolean active) {
        this.offerId = offerId;
        this.cardTier = cardTier;
        this.cashbackPercentage = cashbackPercentage;
        this.active = active;
    }

    public String getOfferId() { return offerId; }
    public String getCardTier() { return cardTier; }
    public double getCashbackPercentage() { return cashbackPercentage; }
    public boolean isActive() { return active; }
}
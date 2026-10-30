package com.api.tests;

import com.api.models.OfferPayload;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class OfferServiceHermeticTest {
    private WireMockServer wireMockServer;

    @BeforeClass
    public void setupHermeticServer() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        RestAssured.baseURI = "http://localhost:8089";

        // Stubbing microservice endpoint for hermetic isolation
        wireMockServer.stubFor(post(urlEqualTo("/v1/offers/validate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"offerId\":\"OFFER_901\",\"cardTier\":\"PLATINUM\",\"cashbackPercentage\":12.5,\"active\":true}")));
    }

    @Test(description = "Verify credit card offer payload serialization and contract validation")
    public void verifyOfferEligibilityContract() {
        OfferPayload requestPayload = new OfferPayload("OFFER_901", "PLATINUM", 12.5, true);

        OfferPayload response = given()
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .when()
                .post("/v1/offers/validate")
                .then()
                .statusCode(200)
                .extract()
                .as(OfferPayload.class);

        Assert.assertEquals(response.getOfferId(), "OFFER_901");
        Assert.assertEquals(response.getCardTier(), "PLATINUM");
        Assert.assertEquals(response.getCashbackPercentage(), 12.5);
        Assert.assertTrue(response.isActive());
    }

    @AfterClass
    public void tearDownServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
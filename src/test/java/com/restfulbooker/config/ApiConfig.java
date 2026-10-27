package com.restfulbooker.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public final class ApiConfig {
    private static final String DEFAULT_BASE_URL = "https://restful-booker.herokuapp.com";

    private ApiConfig() {
    }

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(System.getProperty("baseUrl", DEFAULT_BASE_URL))
                .setContentType("application/json")
                .setAccept("application/json")
                .build();
    }

    public static void configure() {
        RestAssured.requestSpecification = requestSpecification();
    }
}

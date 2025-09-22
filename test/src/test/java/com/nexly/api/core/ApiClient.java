package com.nexly.api.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {
    
    public ApiClient() {
        RestAssured.baseURI = ConfigManager.getBaseUrl();
    }
    
    public Response get(String endpoint) {
        return given()
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }
    
    public Response get(String endpoint, Map<String, Object> queryParams) {
        RequestSpecification request = given().log().all();
        
        if (queryParams != null && !queryParams.isEmpty()) {
            request.queryParams(queryParams);
        }
        
        return request
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }
    
    public Response post(String endpoint, Object body) {
        return given()
                .log().all()
                .contentType("application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
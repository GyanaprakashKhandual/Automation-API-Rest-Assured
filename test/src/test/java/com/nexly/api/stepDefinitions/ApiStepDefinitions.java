package com.nexly.api.stepDefinitions;

import com.nexly.api.core.ApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

public class ApiStepDefinitions {

    private final ApiClient apiClient;
    private Response response;

    public ApiStepDefinitions() {
        apiClient = new ApiClient();
    }

    @Given("I set the API endpoint")
    public void i_set_the_api_endpoint() {
        // Base URL is already set in ApiClient constructor
        System.out.println("API endpoint set to: " + System.getProperty("base.url", "http://localhost:5000"));
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = apiClient.get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode.intValue(),
                "Expected status code: " + expectedStatusCode + " but got: " + actualStatusCode);
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedText) {
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains(expectedText),
                "Response body does not contain: " + expectedText + ". Actual response: " + responseBody);
    }

    @Then("the response content type should be {string}")
    public void the_response_content_type_should_be(String expectedContentType) {
        String actualContentType = response.getContentType();
        Assert.assertTrue(actualContentType.contains(expectedContentType),
                "Expected content type: " + expectedContentType + " but got: " + actualContentType);
    }
}
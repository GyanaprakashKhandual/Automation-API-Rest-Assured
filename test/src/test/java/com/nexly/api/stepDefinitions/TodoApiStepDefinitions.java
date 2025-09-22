package com.nexly.api.stepDefinitions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * TODO API Step Definitions
 * Complete step definitions for testing TODO API endpoints
 */
public class TodoApiStepDefinitions {

    private String baseUrl;
    private RequestSpecification request;
    private Response response;
    private Map<String, Object> requestBody = new HashMap<>();
    private String createdTodoId;
    private List<String> createdTodoIds = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    // Bearer token provided in the requirements
    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY4OWY0YmNlNDY5NmNmOTI3ZjUyOWVmYiIsImlhdCI6MTc1ODUzNjQ2MCwiZXhwIjoxNzYxMTI4NDYwfQ.SGRNRUOArAnKlmwObcsAcxWLt5Mni9w-c7AqcDSGwtw";

    // Background steps
    @Given("^I set the API base URL to \"([^\"]*)\"$")
    public void iSetTheApiBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
        this.request = RestAssured.given()
                .contentType("application/json")
                .accept("application/json");
    }

    @And("^I set the authorization header with bearer token$")
    public void iSetTheAuthorizationHeader() {
        this.request = this.request.header("Authorization", "Bearer " + BEARER_TOKEN);
    }

    @Given("^I remove the authorization header$")
    public void iRemoveTheAuthorizationHeader() {
        this.request = RestAssured.given()
                .contentType("application/json")
                .accept("application/json");
    }

    // Request preparation steps
    @Given("^I prepare the TODO data with:$")
    public void iPrepareTheTodoDataWith(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        this.requestBody.clear();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if ("startDate".equals(key) || "endDate".equals(key)) {
                this.requestBody.put(key, value + "T00:00:00.000Z");
            } else if ("startTime".equals(key) || "endTime".equals(key)) {
                this.requestBody.put(key, "2024-01-01T" + value + ":00.000Z");
            } else if ("links".equals(key)) {
                String[] linksArray = value.split(",");
                this.requestBody.put(key, linksArray);
            } else {
                this.requestBody.put(key, value);
            }
        }
    }

    @Given("^I prepare invalid TODO data with:$")
    public void iPrepareInvalidTodoDataWith(DataTable dataTable) {
        iPrepareTheTodoDataWith(dataTable);
    }

    @Given("^I prepare update data with:$")
    public void iPrepareUpdateDataWith(DataTable dataTable) {
        iPrepareTheTodoDataWith(dataTable);
    }

    @Given("^I prepare invalid update data with:$")
    public void iPrepareInvalidUpdateDataWith(DataTable dataTable) {
        iPrepareTheTodoDataWith(dataTable);
    }

    // Data creation steps
    @Given("^I have created a TODO with workType \"([^\"]*)\"$")
    public void iHaveCreatedATodoWithWorkType(String workType) {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", workType);
        todoData.put("workDesc", "Test TODO for " + workType);
        todoData.put("priority", "Medium");
        todoData.put("status", "TODO");

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO with workType: " + workType);

        try {
            JsonNode responseJson = objectMapper.readTree(createResponse.getBody().asString());
            String todoId = responseJson.get("todo").get("_id").asText();
            createdTodoIds.add(todoId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse created TODO response", e);
        }
    }

    @Given("^I have created a TODO with priority \"([^\"]*)\"$")
    public void iHaveCreatedATodoWithPriority(String priority) {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", "Personal");
        todoData.put("workDesc", "Test TODO with priority " + priority);
        todoData.put("priority", priority);
        todoData.put("status", "TODO");

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO with priority: " + priority);
    }

    @Given("^I have created a TODO with status \"([^\"]*)\"$")
    public void iHaveCreatedATodoWithStatus(String status) {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", "Personal");
        todoData.put("workDesc", "Test TODO with status " + status);
        todoData.put("priority", "Medium");
        todoData.put("status", status);

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO with status: " + status);
    }

    @Given("^I have created a TODO with workDesc \"([^\"]*)\"$")
    public void iHaveCreatedATodoWithWorkDesc(String workDesc) {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", "Personal");
        todoData.put("workDesc", workDesc);
        todoData.put("priority", "Medium");
        todoData.put("status", "TODO");

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO with workDesc: " + workDesc);
    }

    @Given("^I have created a TODO with startDate \"([^\"]*)\"$")
    public void iHaveCreatedATodoWithStartDate(String startDate) {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", "Personal");
        todoData.put("workDesc", "Test TODO with start date");
        todoData.put("priority", "Medium");
        todoData.put("status", "TODO");
        todoData.put("startDate", startDate + "T00:00:00.000Z");

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO with startDate: " + startDate);
    }

    @Given("^I have created a TODO with workType \"([^\"]*)\" and priority \"([^\"]*)\"$")
    public void iHaveCreatedATodoWithWorkTypeAndPriority(String workType, String priority) {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", workType);
        todoData.put("workDesc", "Test TODO with " + workType + " and " + priority);
        todoData.put("priority", priority);
        todoData.put("status", "TODO");

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO with workType: " + workType + " and priority: " + priority);
    }

    @Given("^I have created (\\d+) TODOs$")
    public void iHaveCreatedTodos(int count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> todoData = new HashMap<>();
            todoData.put("workType", "Personal");
            todoData.put("workDesc", "Test TODO " + (i + 1));
            todoData.put("priority", "Medium");
            todoData.put("status", "TODO");

            Response createResponse = this.request
                    .body(todoData)
                    .post("/");

            Assert.assertEquals(createResponse.getStatusCode(), 201,
                    "Failed to create TODO " + (i + 1));
        }
    }

    @Given("^I have created a TODO and stored its ID$")
    public void iHaveCreatedATodoAndStoredItsId() {
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("workType", "Personal");
        todoData.put("workDesc", "Test TODO for ID storage");
        todoData.put("priority", "Medium");
        todoData.put("status", "TODO");

        Response createResponse = this.request
                .body(todoData)
                .post("/");

        Assert.assertEquals(createResponse.getStatusCode(), 201,
                "Failed to create TODO for ID storage");

        try {
            JsonNode responseJson = objectMapper.readTree(createResponse.getBody().asString());
            this.createdTodoId = responseJson.get("todo").get("_id").asText();
            Assert.assertNotNull(this.createdTodoId, "Created TODO ID should not be null");
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract TODO ID from response", e);
        }
    }

    // HTTP request steps
    @When("^I send a (GET|POST|PUT|DELETE) request to \"([^\"]*)\"$")
    public void iSendARequestTo(String method, String endpoint) {
        String url = endpoint.replace("{todoId}", this.createdTodoId != null ? this.createdTodoId : "");

        try {
            switch (method.toUpperCase()) {
                case "GET":
                    this.response = this.request.get(url);
                    break;
                case "POST":
                    this.response = this.request
                            .body(this.requestBody)
                            .post(url);
                    break;
                case "PUT":
                    this.response = this.request
                            .body(this.requestBody)
                            .put(url);
                    break;
                case "DELETE":
                    this.response = this.request.delete(url);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to send " + method + " request to " + url, e);
        }
    }

    // Response verification steps
    @Then("^the response status code should be (\\d+)$")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assert.assertEquals(this.response.getStatusCode(), expectedStatusCode,
                "Expected status code " + expectedStatusCode + " but got " + this.response.getStatusCode() +
                        ". Response body: " + this.response.getBody().asString());
    }

    @And("^the response should contain \"([^\"]*)\"$")
    public void theResponseShouldContain(String expectedText) {
        String responseBody = this.response.getBody().asString();
        Assert.assertTrue(responseBody.contains(expectedText),
                "Response should contain '" + expectedText + "' but response was: " + responseBody);
    }

    @And("^the response should contain \"([^\"]*)\" array$")
    public void theResponseShouldContainArray(String arrayName) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            Assert.assertTrue(responseJson.has(arrayName) && responseJson.get(arrayName).isArray(),
                    "Response should contain array '" + arrayName + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify array in response", e);
        }
    }

    @And("^the response should contain \"([^\"]*)\" object$")
    public void theResponseShouldContainObject(String objectName) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            Assert.assertTrue(responseJson.has(objectName) && responseJson.get(objectName).isObject(),
                    "Response should contain object '" + objectName + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify object in response", e);
        }
    }

    @And("^the response should contain \"([^\"]*)\" field$")
    public void theResponseShouldContainField(String fieldName) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            Assert.assertTrue(responseJson.has(fieldName),
                    "Response should contain field '" + fieldName + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify field in response", e);
        }
    }

    @And("^the response should contain validation errors$")
    public void theResponseShouldContainValidationErrors() {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            Assert.assertTrue(responseJson.has("errors") || responseJson.has("message"),
                    "Response should contain validation errors");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify validation errors in response", e);
        }
    }

    @And("^the response should contain todo data with workType \"([^\"]*)\"$")
    public void theResponseShouldContainTodoDataWithWorkType(String expectedWorkType) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            String actualWorkType = responseJson.get("todo").get("workType").asText();
            Assert.assertEquals(actualWorkType, expectedWorkType,
                    "Expected workType '" + expectedWorkType + "' but got '" + actualWorkType + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify workType in response", e);
        }
    }

    @And("^the response should contain todo data with workDesc \"([^\"]*)\"$")
    public void theResponseShouldContainTodoDataWithWorkDesc(String expectedWorkDesc) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            String actualWorkDesc = responseJson.get("todo").get("workDesc").asText();
            Assert.assertEquals(actualWorkDesc, expectedWorkDesc,
                    "Expected workDesc '" + expectedWorkDesc + "' but got '" + actualWorkDesc + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify workDesc in response", e);
        }
    }

    @And("^the todos array should not be empty$")
    public void theTodosArrayShouldNotBeEmpty() {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");
            Assert.assertTrue(todosArray.isArray() && todosArray.size() > 0,
                    "Todos array should not be empty");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify todos array is not empty", e);
        }
    }

    @And("^all todos in response should have workType \"([^\"]*)\"$")
    public void allTodosInResponseShouldHaveWorkType(String expectedWorkType) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");

            for (JsonNode todo : todosArray) {
                String actualWorkType = todo.get("workType").asText();
                Assert.assertEquals(actualWorkType, expectedWorkType,
                        "All todos should have workType '" + expectedWorkType + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify workType for all todos", e);
        }
    }

    @And("^all todos in response should have priority \"([^\"]*)\"$")
    public void allTodosInResponseShouldHavePriority(String expectedPriority) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");

            for (JsonNode todo : todosArray) {
                String actualPriority = todo.get("priority").asText();
                Assert.assertEquals(actualPriority, expectedPriority,
                        "All todos should have priority '" + expectedPriority + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify priority for all todos", e);
        }
    }

    @And("^all todos in response should have status \"([^\"]*)\"$")
    public void allTodosInResponseShouldHaveStatus(String expectedStatus) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");

            for (JsonNode todo : todosArray) {
                String actualStatus = todo.get("status").asText();
                Assert.assertEquals(actualStatus, expectedStatus,
                        "All todos should have status '" + expectedStatus + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify status for all todos", e);
        }
    }

    @And("^all todos in response should contain \"([^\"]*)\" in workDesc$")
    public void allTodosInResponseShouldContainInWorkDesc(String searchText) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");

            for (JsonNode todo : todosArray) {
                String workDesc = todo.get("workDesc").asText().toLowerCase();
                Assert.assertTrue(workDesc.contains(searchText.toLowerCase()),
                        "All todos should contain '" + searchText + "' in workDesc");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify search text in workDesc", e);
        }
    }

    @And("^all todos in response should have startDate after \"([^\"]*)\"$")
    public void allTodosInResponseShouldHaveStartDateAfter(String dateString) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");
            LocalDate compareDate = LocalDate.parse(dateString);

            for (JsonNode todo : todosArray) {
                if (todo.has("startDate") && !todo.get("startDate").isNull()) {
                    String startDateStr = todo.get("startDate").asText();
                    LocalDate startDate = LocalDate.parse(startDateStr.substring(0, 10));
                    Assert.assertTrue(startDate.isAfter(compareDate) || startDate.isEqual(compareDate),
                            "All todos should have startDate after " + dateString);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify startDate filter", e);
        }
    }

    @And("^the response should contain pagination with current page (\\d+)$")
    public void theResponseShouldContainPaginationWithCurrentPage(int expectedPage) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            int actualPage = responseJson.get("pagination").get("current").asInt();
            Assert.assertEquals(actualPage, expectedPage,
                    "Expected current page " + expectedPage + " but got " + actualPage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pagination current page", e);
        }
    }

    @And("^the todos array should have maximum (\\d+) items$")
    public void theTodosArrayShouldHaveMaximumItems(int maxItems) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            JsonNode todosArray = responseJson.get("todos");
            Assert.assertTrue(todosArray.size() <= maxItems,
                    "Todos array should have maximum " + maxItems + " items but had " + todosArray.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify todos array size", e);
        }
    }

    @And("^the todo should have the same ID as created$")
    public void theTodoShouldHaveTheSameIdAsCreated() {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            String actualId = responseJson.get("todo").get("_id").asText();
            Assert.assertEquals(actualId, this.createdTodoId,
                    "Expected todo ID '" + this.createdTodoId + "' but got '" + actualId + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify todo ID", e);
        }
    }

    @And("^the response todo should have workDesc \"([^\"]*)\"$")
    public void theResponseTodoShouldHaveWorkDesc(String expectedWorkDesc) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            String actualWorkDesc = responseJson.get("todo").get("workDesc").asText();
            Assert.assertEquals(actualWorkDesc, expectedWorkDesc,
                    "Expected workDesc '" + expectedWorkDesc + "' but got '" + actualWorkDesc + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify updated priority", e);
        }
    }

    @And("^the total count should be (\\d+)$")
    public void theTotalCountShouldBe(int expectedTotal) {
        try {
            JsonNode responseJson = objectMapper.readTree(this.response.getBody().asString());
            int actualTotal = responseJson.get("total").asInt();
            Assert.assertEquals(actualTotal, expectedTotal,
                    "Expected total count " + expectedTotal + " but got " + actualTotal);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify total count in stats", e);
        }
    }

    // Helper methods for common validations
    private void validateTodoFields(JsonNode todo, String fieldName, String expectedValue) {
        try {
            String actualValue = todo.get(fieldName).asText();
            Assert.assertEquals(actualValue, expectedValue,
                    "Expected " + fieldName + " '" + expectedValue + "' but got '" + actualValue + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate " + fieldName + " field", e);
        }
    }

    private void validateArrayNotEmpty(JsonNode responseJson, String arrayName) {
        try {
            JsonNode array = responseJson.get(arrayName);
            Assert.assertTrue(array != null && array.isArray() && array.size() > 0,
                    arrayName + " array should not be empty");
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate " + arrayName + " array", e);
        }
    }

    private void validateObjectExists(JsonNode responseJson, String objectName) {
        try {
            Assert.assertTrue(responseJson.has(objectName) &&
                    responseJson.get(objectName).isObject(),
                    "Response should contain " + objectName + " object");
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate " + objectName + " object", e);
        }
    }

    private void validateFieldExists(JsonNode responseJson, String fieldName) {
        try {
            Assert.assertTrue(responseJson.has(fieldName),
                    "Response should contain " + fieldName + " field");
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate " + fieldName + " field", e);
        }
    }

    private void validateErrorResponse(String responseBody) {
        try {
            JsonNode responseJson = objectMapper.readTree(responseBody);
            boolean hasErrors = responseJson.has("errors") ||
                    responseJson.has("message") ||
                    responseJson.has("error");
            Assert.assertTrue(hasErrors,
                    "Response should contain error information. Response: " + responseBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate error response", e);
        }
    }

    // Additional utility methods for data validation
    private boolean isValidObjectId(String id) {
        return id != null && id.matches("^[0-9a-fA-F]{24}$");
    }

    private boolean isValidISODate(String dateString) {
        try {
            return dateString != null && dateString.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$");
        } catch (Exception e) {
            return false;
        }
    }

    private void cleanup() {
        // Clean up created todos if needed
        for (String todoId : createdTodoIds) {
            try {
                RestAssured.given()
                        .header("Authorization", "Bearer " + BEARER_TOKEN)
                        .contentType("application/json")
                        .delete("/" + todoId);
            } catch (Exception e) {
                // Ignore cleanup errors
                System.err.println("Failed to cleanup TODO: " + todoId);
            }
        }
        createdTodoIds.clear();

        if (createdTodoId != null) {
            try {
                RestAssured.given()
                        .header("Authorization", "Bearer " + BEARER_TOKEN)
                        .contentType("application/json")
                        .delete("/" + createdTodoId);
            } catch (Exception e) {
                // Ignore cleanup errors
                System.err.println("Failed to cleanup TODO: " + createdTodoId);
            }
            createdTodoId = null;
        }
    }
}
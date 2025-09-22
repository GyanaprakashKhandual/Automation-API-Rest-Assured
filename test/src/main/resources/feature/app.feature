Feature: Sample API Tests

  Scenario: Verify GET request to root endpoint
    Given I set the API endpoint
    When I send a GET request to "/"
    Then the response status code should be 200
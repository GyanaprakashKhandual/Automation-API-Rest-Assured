Feature: TODO API Tests

  Background:
    Given I set the API base URL to "http://localhost:3000/api/v1/to-do"
    And I set the authorization header with bearer token

  @create-todo
  Scenario: Create a new TODO successfully
    Given I prepare the TODO data with:
      | workType | Personal         |
      | workDesc | Complete project |
      | priority | HIGH             |
      | status   | TODO             |
    When I send a POST request to "/"
    Then the response status code should be 201
    And the response should contain "TODO created successfully"
    And the response should contain todo data with workType "Personal"
    And the response should contain todo data with workDesc "Complete project"

  @create-todo-validation
  Scenario: Create TODO with invalid data should fail
    Given I prepare invalid TODO data with:
      | workType | InvalidType     |
      | priority | InvalidPriority |
    When I send a POST request to "/"
    Then the response status code should be 400
    And the response should contain validation errors

  @get-all-todos
  Scenario: Get all TODOs successfully
    Given I have created a TODO with workType "Personal"
    And I have created a TODO with workType "Professional"
    When I send a GET request to "/"
    Then the response status code should be 200
    And the response should contain "todos" array
    And the response should contain "pagination" object
    And the todos array should not be empty

  @get-todos-with-filters
  Scenario: Get TODOs with workType filter
    Given I have created a TODO with workType "Personal"
    And I have created a TODO with workType "Professional"
    When I send a GET request to "/?workType=Personal"
    Then the response status code should be 200
    And all todos in response should have workType "Personal"

  @get-todos-with-pagination
  Scenario: Get TODOs with pagination
    Given I have created 5 TODOs
    When I send a GET request to "/?page=1&limit=3"
    Then the response status code should be 200
    And the response should contain pagination with current page 1
    And the todos array should have maximum 3 items

  @get-todos-with-search
  Scenario: Search TODOs by description
    Given I have created a TODO with workDesc "Complete project documentation"
    And I have created a TODO with workDesc "Fix bug in login"
    When I send a GET request to "/?search=project"
    Then the response status code should be 200
    And all todos in response should contain "project" in workDesc

  @get-single-todo
  Scenario: Get single TODO by ID successfully
    Given I have created a TODO and stored its ID
    When I send a GET request to "/{todoId}"
    Then the response status code should be 200
    And the response should contain "todo" object
    And the todo should have the same ID as created

  @get-single-todo-not-found
  Scenario: Get single TODO with invalid ID should fail
    When I send a GET request to "/507f1f77bcf86cd799439011"
    Then the response status code should be 404
    And the response should contain "TODO not found"

  @update-todo
  Scenario: Update TODO successfully
    Given I have created a TODO and stored its ID
    And I prepare update data with:
      | workDesc | Updated description |
      | priority | Medium              |
      | status   | In Progress         |
    When I send a PUT request to "/{todoId}"
    Then the response status code should be 200
    And the response should contain "TODO updated successfully"
    And the response todo should have workDesc "Updated description"
    And the response todo should have priority "Medium"

  @update-todo-not-found
  Scenario: Update non-existent TODO should fail
    Given I prepare update data with:
      | workDesc | Updated description |
    When I send a PUT request to "/507f1f77bcf86cd799439011"
    Then the response status code should be 404
    And the response should contain "TODO not found"

  @update-todo-validation
  Scenario: Update TODO with invalid data should fail
    Given I have created a TODO and stored its ID
    And I prepare invalid update data with:
      | priority | InvalidPriority |
    When I send a PUT request to "/{todoId}"
    Then the response status code should be 400
    And the response should contain validation errors

  @delete-todo
  Scenario: Delete TODO successfully
    Given I have created a TODO and stored its ID
    When I send a DELETE request to "/{todoId}"
    Then the response status code should be 200
    And the response should contain "TODO deleted successfully"

  @delete-todo-not-found
  Scenario: Delete non-existent TODO should fail
    When I send a DELETE request to "/507f1f77bcf86cd799439011"
    Then the response status code should be 404
    And the response should contain "TODO not found"

  @get-todo-stats
  Scenario: Get TODO statistics successfully
    Given I have created a TODO with status "Completed"
    And I have created a TODO with status "In Progress"
    And I have created a TODO with status "TODO"
    When I send a GET request to "/stats"
    Then the response status code should be 200
    And the response should contain "total" field
    And the response should contain "completed" field
    And the response should contain "inProgress" field
    And the response should contain "todo" field
    And the response should contain "workTypeBreakdown" object
    And the total count should be 3

  @priority-filtering
  Scenario: Filter TODOs by priority
    Given I have created a TODO with priority "HIGH"
    And I have created a TODO with priority "Medium"
    When I send a GET request to "/?priority=HIGH"
    Then the response status code should be 200
    And all todos in response should have priority "HIGH"

  @status-filtering
  Scenario: Filter TODOs by status
    Given I have created a TODO with status "Completed"
    And I have created a TODO with status "In Progress"
    When I send a GET request to "/?status=Completed"
    Then the response status code should be 200
    And all todos in response should have status "Completed"

  @date-range-filtering
  Scenario: Filter TODOs by date range
    Given I have created a TODO with startDate "2024-01-01"
    And I have created a TODO with startDate "2024-06-01"
    When I send a GET request to "/?startDateFrom=2024-05-01"
    Then the response status code should be 200
    And all todos in response should have startDate after "2024-05-01"

  @multiple-filters
  Scenario: Apply multiple filters simultaneously
    Given I have created a TODO with workType "Personal" and priority "HIGH"
    And I have created a TODO with workType "Professional" and priority "HIGH"
    And I have created a TODO with workType "Personal" and priority "Medium"
    When I send a GET request to "/?workType=Personal&priority=HIGH"
    Then the response status code should be 200
    And all todos in response should have workType "Personal"
    And all todos in response should have priority "HIGH"

  @unauthorized-access
  Scenario: Access API without authorization should fail
    Given I remove the authorization header
    When I send a GET request to "/"
    Then the response status code should be 401
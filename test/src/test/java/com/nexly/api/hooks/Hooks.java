package com.nexly.api.hooks;

import com.aventstack.extentreports.Status;
import com.nexly.api.utils.ExtentReportManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentReportManager.getInstance().createTest(scenario.getName()).log(Status.FAIL, "Scenario Failed");
        } else {
            ExtentReportManager.getInstance().createTest(scenario.getName()).log(Status.PASS, "Scenario Passed");
        }
        System.out.println("Finished scenario: " + scenario.getName() + " with status: " + scenario.getStatus());
    }
}
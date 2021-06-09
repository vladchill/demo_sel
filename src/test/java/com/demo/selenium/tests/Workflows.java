package com.demo.selenium.tests;

import com.demo.selenium.pages.demo_yay.LoginPage;
import com.demo.selenium.pages.BaseTest;
import com.demo.selenium.pages.demo_yay.WorkflowNewBlockPage;
import com.demo.selenium.pages.demo_yay.WorkflowsPage;
import com.demo.selenium.utils.Properties;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

public class Workflows extends BaseTest {

    @Test
    @Description("Demo Task")
    @AllureId("DT1.1")
    public void collectionWorkflowSaveTest() {
        LoginPage loginPage = new LoginPage();
        WorkflowsPage workflowsPage = loginPage
                .openPage()
                .signInWith(Properties.getUsername(), Properties.getPassword())
                .waitForPageLoad()
                .navBlockPage
                .clickOnNavWorkflows()
                .waitForPageLoad();
        WorkflowNewBlockPage workflowNewBlockPage = workflowsPage
                .clickOnNewWorkFlowButton();

        String headerFromNewWorkflow = workflowNewBlockPage
                .waitForPageLoad()
                .getHeaderText();
        workflowNewBlockPage
                .clickTimelineTab()
                .waitForPageLoad()
                .clickCreateCall()
                .clickCreateEmail()
                .clickSaveButton();
        List<String> customWorkflowsList = workflowsPage
                .navBlockPage
                .clickOnNavWorkflows()
                .waitForPageLoad()
                .getCustomNamesList();

        Assertions.assertThat(customWorkflowsList)
                .as("List of all created workflows")
                .containsOnlyOnce(headerFromNewWorkflow)
                .endsWith(headerFromNewWorkflow);
    }
}
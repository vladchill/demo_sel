package com.demo.selenium.pages.demo_yay;

import com.demo.selenium.pages.main_front.MainFrontPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class WorkflowsPage extends MainFrontPage {

    private final String header = "Workflows";

    @FindBy(xpath = "//div[@class='header-page-title']")
    WebElement headerText;

    @FindBy(xpath = "//span[text()='New Workflow']")
    WebElement newWorkFlowButton;

    //div[@class='collection-workflows-list']

    @FindBy(xpath = "//div[@class='collection-workflows-list']")
    WebElement workFlowWrapper;

    @FindBy(xpath = "(//h3[contains(@class,'workflow-title')])[position() !=last()]")
    List<WebElement> workflowListName;

    public NavBlockPage navBlockPage;


    public WorkflowsPage() {
        PageFactory.initElements(getDriver(), this);
        navBlockPage = new NavBlockPage();
    }

    @Step
    public WorkflowsPage waitForPageLoad() {
        waitForElementWithText(headerText, header);
        driverWait.waitforVisibility(workFlowWrapper);
        return this;
    }

    @Step
    public WorkflowNewBlockPage clickOnNewWorkFlowButton() {
        newWorkFlowButton.click();
        return new WorkflowNewBlockPage();
    }

    @Step
    public List<String> getCustomNamesList() {
        return workflowListName.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}

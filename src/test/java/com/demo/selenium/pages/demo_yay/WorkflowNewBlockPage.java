package com.demo.selenium.pages.demo_yay;

import com.demo.selenium.pages.main_front.MainFrontPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class WorkflowNewBlockPage extends MainFrontPage {

    private final String header = "Workflows";

    @FindBy(xpath = "//h3[contains(text(),'Custom workflow')]")
    WebElement headerText;

    @FindBy(xpath = "//ul[@class='content-tab-links-holder']/li/a[text()='TIMELINE']")
    WebElement timelineTab;

    public WorkflowNewBlockPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @Step
    public WorkflowNewBlockPage waitForPageLoad() {
        driverWait.waitforVisibility(headerText);
        return this;
    }

    @Step
    public TimeLineTabPage clickTimelineTab() {
        timelineTab.click();
        return new TimeLineTabPage();
    }

    @Step
    public String getHeaderText() {
        return headerText.getText();
    }
}

package com.demo.selenium.pages.demo_yay;

import com.demo.selenium.pages.main_front.MainFrontPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.util.concurrent.TimeUnit;

public class TimeLineTabPage extends MainFrontPage {

    private final String header = "Workflows";

    @FindBy(xpath = "//h3[contains(text(),'Custom workflow')]")
    WebElement headerText;

    @FindBy(xpath = "//ul[@data-type='CREATION']//i[text()='add']")
    WebElement currentPlusButton;

    @FindBy(xpath = "//ul[@data-type='CREATION']//li[@data-type='CALL']")
    WebElement currentCallButton;

    @FindBy(xpath = "//ul[@data-type='CREATION']//li[@data-type='EMAIL']")
    WebElement currentEmailButton;

    @FindBy(xpath = "//a[text()='Save']")
    WebElement saveButton;

    public TimeLineTabPage() {
        PageFactory.initElements(new AjaxElementLocatorFactory(getDriver(), 60), this);
    }

    @Step
    public TimeLineTabPage waitForPageLoad() {
        driverWait.waitforVisibility(headerText);
        return this;
    }

    @Step
    public TimeLineTabPage clickCreateCall() {
        hoverTo(currentPlusButton);
        driverWait.waitforVisibility(currentCallButton);
        currentCallButton.click();
        waitForAnimation();
        return this;
    }

    @Step
    public TimeLineTabPage clickCreateEmail() {
        driverWait.getWaitDriver().pollingEvery(500, TimeUnit.MILLISECONDS).until(w -> { hoverTo(headerText);hoverTo(currentPlusButton); return currentEmailButton.isDisplayed(); });

        currentEmailButton.click();
        return this;
    }

    @Step
    public TimeLineTabPage clickSaveButton() {
        saveButton.click();
        return this;
    }
}

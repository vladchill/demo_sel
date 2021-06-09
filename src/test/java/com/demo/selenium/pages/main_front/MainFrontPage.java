package com.demo.selenium.pages.main_front;

import com.demo.selenium.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

public class MainFrontPage extends BasePage {

    public MainFrontPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @Step
    protected void waitForElementWithText(WebElement titleText, String titleExpected) {
        logTextToAllure(this.getClass().getSimpleName() + " class with:");
        textToAttachment(titleExpected, "Title text expected");
        waitForAnimation();
        driverWait.waitforVisibility(titleText);
        try {
            driverWait.getWaitDriver().until((ExpectedCondition<Boolean>) w -> titleText.getText().contains(titleExpected));
        } catch (TimeoutException ex) {
            Assert.assertEquals(titleText.getText(), titleExpected, "Failed after timeout wait cause Title is diff");
            throw ex;
        }
        waitForAnimation();
    }

    @Override
    public String toString() {
        Class aClass = this.getClass();
        return aClass.getSimpleName() + " - " + aClass.getPackage().toString();
    }
}

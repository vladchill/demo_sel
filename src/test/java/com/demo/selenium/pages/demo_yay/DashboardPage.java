package com.demo.selenium.pages.demo_yay;

import com.demo.selenium.pages.main_front.MainFrontPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage extends MainFrontPage {

    private final String header = "Dashboard";

    @FindBy(xpath = "//div[@class='header-page-title']")
    WebElement headerText;

    public NavBlockPage navBlockPage;


    public DashboardPage() {
        PageFactory.initElements(getDriver(), this);
        navBlockPage = new NavBlockPage();
    }

    @Step
    public DashboardPage waitForPageLoad() {
        waitForElementWithText(headerText, header);
        return this;
    }
}

package com.demo.selenium.pages.demo_yay;

import com.demo.selenium.constants.Environment;
import com.demo.selenium.pages.main_front.MainFrontPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends MainFrontPage {

    private final String header = "Sign In";

    @FindBy(xpath = "//h1[@class='sign-in__title']")
    WebElement headerText;

    @FindBy(xpath = "//input[@name='username']")
    WebElement loginField;

    @FindBy(xpath = "//input[@name='password']")
    WebElement passwordField;

    @FindBy(xpath = "//div[@class='sign-in__btns']/button")
    WebElement signInButton;

    public LoginPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @Step
    public LoginPage openPage() {
        openURL(getCurrentEnv().link);
        return this;
    }

    @Step
    public LoginPage openPage(Environment environment) {
        openURL(environment.link);
        return this;
    }

    @Step
    public LoginPage waitForPageLoad() {
        waitForElementWithText(headerText, header);
        return this;
    }

    @Step
    public LoginPage typeLogin(String login) {
        typeText(loginField, login);
        return this;
    }

    @Step
    public LoginPage typePassword(String password) {
        typeText(passwordField, password);
        return this;
    }

    @Step
    public DashboardPage signInWith(String email, String password) {
        typeLogin(email);
        typePassword(password);
        signInButton.click();
        return new DashboardPage();
    }
}
package com.demo.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class WebDriverWaitLogged {

    private final int  TIMEOUT_10_SECONDS = 10;
    private final int TIMEOUT_30_SECONDS = 30;
    private final int TIMEOUT_60_SECONDS = 60;
    private final int TIMEOUT_120_SECONDS = 120;
    private WebDriver driver;
    private WebDriverWait wait;


    public WebDriverWaitLogged(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 15);
    }

    public WebDriverWait getWaitDriver() {
        return wait;
    }

    public void waitforVisibility(WebElement element){
        wait.withTimeout(TIMEOUT_30_SECONDS, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitforVisibility(By locator){
        wait.withTimeout(TIMEOUT_30_SECONDS, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitforPresence(By locator){
        wait.withTimeout(TIMEOUT_30_SECONDS, TimeUnit.SECONDS).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void waitForInvisibility(By by) {
        wait.withTimeout(TIMEOUT_30_SECONDS, TimeUnit.SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public void waitForInvisibility(WebElement element) {
        wait.withTimeout(TIMEOUT_30_SECONDS, TimeUnit.SECONDS).until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForInvisibility(int waitElementTimeout, int timeout, WebElement element, String message) {
        driver.manage().timeouts().implicitlyWait(waitElementTimeout, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until((ExpectedCondition) o -> !element.isDisplayed());
        } catch (WebDriverException ex) {
            //swallow
        } finally {
            driver.manage().timeouts().implicitlyWait(TIMEOUT_60_SECONDS, TimeUnit.SECONDS);
        }
    }

    public WebElement waitForClickable(WebElement element) {
        wait.withTimeout(TIMEOUT_30_SECONDS, TimeUnit.SECONDS).until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }





}

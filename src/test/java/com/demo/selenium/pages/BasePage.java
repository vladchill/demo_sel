package com.demo.selenium.pages;


import com.demo.selenium.listeners.TestListener;
import com.demo.selenium.constants.Environment;
import com.demo.selenium.utils.CheckFile;
import com.paulhammant.ngwebdriver.NgWebDriver;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public abstract class BasePage {
    protected NgWebDriver ngDriver;
    protected WebDriverWaitLogged driverWait;
    private WebDriver driver;
    private Actions actions;

    public BasePage() {
        driver = BaseTest.getDriver();
        actions = new Actions(driver);
        driverWait = new WebDriverWaitLogged(driver);
        ngDriver = new NgWebDriver((JavascriptExecutor) driver);
        PageFactory.initElements(getDriver(), this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    protected Actions getActions(){
        return actions;
    }

    @Step
    protected void openURL(String url) {
        driver.navigate().to(url);
    }

    protected void clickByActions(WebElement element){
        actions.moveToElement(element).click().build().perform();
    }

    protected void clickSimpleWithJS(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
    }

    protected String getValueOfCssPropertyForPseudoElement(String cssSelector, int positionInFoundList, String pseudoClass, String cssProperty){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        String script = String.format("return window.getComputedStyle(document.querySelectorAll('%1$s')[%2$s],'%3$s').getPropertyValue('%4$s')",
                cssSelector, positionInFoundList-1, pseudoClass, cssProperty);
        return (String) executor.executeScript(script);
    }

    @Step
    protected void hoverTo(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    @Step
    protected WebElement scrollToElement(WebElement element, boolean isIntoView) {
        ((JavascriptExecutor) driver).executeScript(String.format("arguments[0].scrollIntoView(%b);", isIntoView),
                element);
        return element;
    }

    @Step
    protected void scroll(int y) {
        String script = String.format("scroll(0, %d);", y);
        ((JavascriptExecutor) driver).executeScript(script);
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    @Step
    protected void scrollPageDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    @Step
    protected void scrollUntilClick(WebElement el, int pxIncrement) {
        int i = 0;
        while(true) {
            try {
                scroll(i+=pxIncrement);
                el.click();
                break;
            } catch (ElementClickInterceptedException ex) {
                if ((Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return (document.body.scrollHeight - document.body.offsetHeight) <=  window.pageYOffset")) break;
                threadSleep(50);
            }
        }
    }

    @Step
    protected void clickTry(WebElement el) {
        int i = 0;
        while(true) {
            try {
                el.click();
                break;
            } catch (ElementClickInterceptedException ex) {
                threadSleep(50);
            }
        }
    }

    @Step
    public void attachPageScreenshot() {
        TestListener.attachScreenshot(new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver));
    }

    @Step
    protected Screenshot getElementScreenshot(WebElement element) {
        return new AShot().takeScreenshot(driver, element);
    }

    @Step
    public ImageDiff getDiff(Screenshot start, Screenshot end) {
        ImageDiff diff = new ImageDiffer().makeDiff(start, end);
        TestListener.attachScreenshot(diff);
        return diff;
    }

    @Step
    protected void setElementAttributeWithJS(String attributeName, String attributeValue, WebElement webElement) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", webElement, attributeName,
                attributeValue);
    }

    //work with text

    @Step
    protected void typeTextWithoutClear(WebElement webElement, String text) {
//        scrollToElement(webElement, false);
        webElement.sendKeys(text);
    }

    @Step
    protected void typeText(WebElement webElement, String text) {
//        scrollToElement(webElement, false);
        webElement.clear();
        webElement.sendKeys(text);
    }

    protected void typeTextByActions(WebElement element, String text){
        actions.moveToElement(element).click().sendKeys(text).build().perform();
    }

    protected void typeTextWithTime(WebElement webElement, String text, int timeInMilis) {
        for (char sign: text.toCharArray()) {
            webElement.sendKeys(String.valueOf(sign));
            threadSleep(timeInMilis);
        }
    }

    @Step
    protected String getText(WebElement element) {
        String type = element.getTagName().toLowerCase();

        if (type.equals("input") || type.equals("textarea")) {
            String placeholder = element.getAttribute("placeholder");
            return (placeholder != null && placeholder.length() > 0)
                    ? element.getAttribute("value").replace(placeholder, "")
                    : element.getAttribute("value");
        }
        if (type.equals("select")) {
            return new Select(element).getFirstSelectedOption().getText();
        }
        return element.getText();
    }


    /**
     * Select dropdown option by visible text, if not successful, tries select by value
     *
     * @param selectItemText visible text
     * @param element
     */
    protected void selectDropDownListOptionByText(WebElement element, String selectItemText) {
        Select dropDownList = new Select(element);
        // if element has wrong text we can try select item by value
        try {
            dropDownList.selectByVisibleText(selectItemText);
        } catch (NoSuchElementException e) {
            dropDownList.selectByValue(selectItemText);
        }
    }

    protected void selectDropDownListOptionByValue(WebElement element, String value) {
        Select dropDownList = new Select(element);
        dropDownList.selectByValue(value);
    }

    protected List<String> getDropDownListValues(WebElement select) {
        Select dropDownList = new Select(select);
        return dropDownList.getOptions().stream().map(e -> e.getAttribute("value")).collect(Collectors.toList());
    }

    /**
     *  Select item from navigation menu
     * @param menuButtom to open navigation menu
     * @param menuItems navigation menu items
     * @param item navigation menu item
     */
    protected void selectFromNavigationMenu(WebElement menuButtom, List<WebElement> menuItems, String item) {
        try {
            driverWait.waitforVisibility(menuButtom);
            actions.moveToElement(menuButtom).perform();
            menuItems.stream().filter(webElement -> webElement.getText().equals(item))
                    .findFirst()
                    .get()
                    .click();
        } catch (WebDriverException e) {
            Assert.fail("Navigation menu or its item wasn't found");
            throw e;
        }
    }

    //waits

    protected void waitForNetwork(int timeoutInSeconds) {
        System.out.println("Checking active ajax calls::");
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
            for (int i = 0; i < timeoutInSeconds; i++) {
                Object numberOfAjaxConnections = jsDriver.executeScript("return angular.element(document.body).injector().get(\"$http\").pendingRequests.length");
                if (numberOfAjaxConnections instanceof Long) {
                    Long n = (Long) numberOfAjaxConnections;
                    System.out.println("Number of active ajax calls: " + n);
                    if (n == 0L)
                        break;
                }
                threadSleep(1000);
            }
        } else {
            System.out.println("Web getDriver: " + driver + " cannot execute javascript");
        }
    }

    public void waitForAnimation() {
        waitForDocumentReady();
        waitForJQuery();
    }

    public void waitForDocumentReady() {
        driverWait.getWaitDriver().until((ExpectedCondition<Boolean>) wdriver -> ((JavascriptExecutor) driver).executeScript(
                "return document.readyState"
        ).equals("complete"));
    }

    public void waitForJQuery(){
        driverWait.getWaitDriver().until((ExpectedCondition<Boolean>) wdriver -> (boolean)((JavascriptExecutor) driver).executeScript(
                "return jQuery.active <= 1"
        ));
    }




    protected boolean isElementPresent(int timeoutInSeconds, By by) {
        boolean isPresent = false;
        try {
//            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            driverWait.getWaitDriver().withTimeout(timeoutInSeconds, TimeUnit.SECONDS).until(ExpectedConditions.presenceOfElementLocated(by));
            isPresent = true;
        } catch (WebDriverException e) {
        } finally {
//            driver().manage().timeouts().implicitlyWait(ELEMENT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }
        return isPresent;
    }


    @Step
    public void threadSleep(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // working with tabs

    @Step
    public <T extends BasePage> T switchTab() {
        ArrayList<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(1));
        return (T)this;
    }

    @Step
    public void switchToMainTab() {
        ArrayList<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(0));
    }

    @Step
    protected void switchTabsUsingPartOfUrl(String platform) {
        String currentHandle = null;
        try {
            final Set<String> handles = driver.getWindowHandles();
            if (handles.size() > 1) {
                currentHandle = driver.getWindowHandle();
            }
            if (currentHandle != null) {
                for (final String handle : handles) {
                    driver.switchTo().window(handle);
                    if (driver.getCurrentUrl().contains(platform) && !currentHandle.equals(handle)) {
                        break;
                    }
                }
            }
//            else {
//                for (final String handle : handles) {
//                    driver.switchTo().window(handle);
//                    if (driver.getCurrentUrl().contains(platform)) {
//                        break;
//                    }
//                }
//            }
        } catch (Exception e) {
            System.out.println("Switching tabs failed");
        }
    }

    public void uploadFile(WebElement inputElement, String fileInResources){
            inputElement.sendKeys(CheckFile.getFileFromResources(fileInResources).getPath());// /img/expected/testcheck.jpg
    }

    public static Environment getCurrentEnv(){
        String envFromSysVar = System.getProperty("site.env", "STG");
        return Arrays.stream(Environment.values())
                .filter(env -> env.name.equals(envFromSysVar))
                .findAny()
                .get();
    }

    //logs
    @Step("{0}")
    public void logTextToAllure(String text) {
        //empty method
    }

    @Step("{0}")
    public void logTextToAllureAndConsole(String text) {
        System.out.println(text);
    }

    @Attachment
    public String textToAttachment(String textToAttachment) {
        return textToAttachment;
    }

    @Attachment("{1}")
    public String textToAttachment(String textToAttachment, String header) {
        return textToAttachment;
    }

    @Step
    public void back(){
        driver.navigate().back();
    }

    @Step
    public void maximizePage() {
        driver.manage().window().maximize();
    }

}

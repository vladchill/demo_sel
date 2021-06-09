package com.demo.selenium.pages;

import com.demo.selenium.constants.Platforms;
import com.demo.selenium.listeners.EventHandler;
import com.demo.selenium.listeners.TestListener;
import com.demo.selenium.utils.DriverFactory;
import com.demo.selenium.utils.Properties;
import com.demo.selenium.utils.allure.AllureUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Listeners(TestListener.class)
public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private EventFiringWebDriver driver;

    public static WebDriver getDriver() {
//        return driver;
        return DRIVER.get();
    }

    public static boolean allureCounterRun = false;

    @BeforeClass
    public void setUp() {
        driver = Properties.getGridURL().isEmpty()
                ? new EventFiringWebDriver(DriverFactory.initDriver(Properties.getBrowser()))
                : new EventFiringWebDriver(DriverFactory.initDriver(Properties.getBrowser(), Properties.getGridURL()));
        driver.register(new EventHandler());
        driver.manage().timeouts().setScriptTimeout(50,TimeUnit.SECONDS);

        switch (Platforms.isEnvWeb) {
            case Platforms.WEB:
                driver.manage().window().setSize(new Dimension(1400, 1050));
                break;
            case Platforms.TABLET:
                driver.manage().window().setSize(new Dimension(900, 1050));
                break;
            case Platforms.MOBILE:
                driver.manage().window().setSize(new Dimension(700, 1000));
                break;
        }
//        System.setProperty(ESCAPE_PROPERTY, "false");
        DRIVER.set(driver);
        if (!allureCounterRun) {
            AllureUtils.createProperties(getDriver());
            allureCounterRun = true;
        }
        System.out.println("Browser version "+((RemoteWebDriver)(((EventFiringWebDriver)getDriver()).getWrappedDriver())).getCapabilities().getVersion());
        System.out.println("Driver version "
                +((Map<String, String>)((RemoteWebDriver)(((EventFiringWebDriver)getDriver()).getWrappedDriver()))
                .getCapabilities()
                .getCapability("chrome"))
                .get("chromedriverVersion"));
    }

    @AfterClass
    public void tearDown() {
        if (DRIVER.get() != null) {
            //logs from browser
            LogEntries logEntries = DRIVER.get().manage().logs().get(LogType.BROWSER);
            for (LogEntry entry : logEntries) {
                System.out.println((String.format("%s %s %s", new Date(entry.getTimestamp()), entry.getLevel(),
                        entry.getMessage())));
            }

            DRIVER.get().quit();
            DRIVER.remove();
        }
    }
}

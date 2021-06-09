package com.demo.selenium.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.SkipException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;

public class DriverFactory {

    public static WebDriver initDriver(String browser) {
        DesiredCapabilities capabilities;
        switch (browser) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", getResourceByName("/geckodriver"));
                return new FirefoxDriver();
            case "chrome":
            default:
                System.setProperty("webdriver.chrome.driver", getResourceByName("/chromedriver"));
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
//                chromePrefs.put("download.default_directory", URLs.downloadPath);
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePrefs);
//                options.addArguments("--disable-notifications");
                return new ChromeDriver(options);
        }
    }

    /**
     * @param resourceName The name of the resource ex /chromedriver
     * @return Path to resource
     */
    private static String getResourceByName(String resourceName){
        try {
            return new File(DriverFactory.class.getResource(resourceName + getExecutableExtension()).toURI()).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WebDriver initDriver(String browser, String gridUrl) {
        // prepare capabilities for required browser
        DesiredCapabilities capabilities;
        switch (browser) {
            case "android":
                capabilities = DesiredCapabilities.android();
                break;
            case "firefox":
                capabilities = DesiredCapabilities.firefox();
                break;
            case "ie":
            case "internet explorer":
                capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                break;
            case "edge":
            case "MicrosoftEdge":
                capabilities = DesiredCapabilities.edge();
                break;
            case "chrome":
            default:
                LoggingPreferences loggingprefs = new LoggingPreferences();
                loggingprefs.enable(LogType.BROWSER, Level.WARNING);
                loggingprefs.enable(LogType.PERFORMANCE, Level.WARNING);
                capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability("enableVNC", true);
                capabilities.setCapability("sessionTimeout", "2m");
                capabilities.setCapability("enableVideo", true);//http://172.0.0.1:4444/video/
//                capabilities.setCapability("videoName", "my-cool-video.mp4");//changing name
                capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
                break;
        }

        // create instance of WebDriver
        try {
            return new RemoteWebDriver(new URL(gridUrl), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SkipException("Unable to create RemoteWebDriver instance!");
        }
    }

    private static String getExecutableExtension() {
        return OSValidator.isWindows()
                ? ".exe"
                : OSValidator.isMac()
                ? "_mac"
                : "_linux";
    }
}

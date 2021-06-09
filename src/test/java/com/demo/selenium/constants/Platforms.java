package com.demo.selenium.constants;

public interface Platforms {
    String MOBILE = "mobile";
    String TABLET = "tablet";
    String WEB = "web";

    String isEnvWeb = System.getProperty("platform", "web");// web, mobile, tablet
}

package com.demo.selenium.constants;

public enum Environment {

    PRD("PRD", "http://test-prd.nl"),
    STG("STG", "https://www.demo.yaypay.com/login"),
    QA("QA", "https://test-qa.nl");

    public final String name;
    public final String link;

    Environment(String name, String link) {
        this.name = name;
        this.link = link;
    }

    @Override
    public String toString() {
        return name;
    }
}

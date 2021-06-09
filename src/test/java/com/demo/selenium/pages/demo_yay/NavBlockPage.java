package com.demo.selenium.pages.demo_yay;

import com.demo.selenium.pages.main_front.MainFrontPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class NavBlockPage extends MainFrontPage {

    @FindBy(xpath = "//div[@class='left-menu']/ul/li//p")
    List<WebElement> leftNavList;

    public NavBlockPage() {
        PageFactory.initElements(getDriver(), this);
    }

    private void clickOnLeftMenuByName(String name) {
        leftNavList.stream()
                .filter(el -> el.getText().equals(name))
                .findFirst()
                .get()
                .click();
        waitForAnimation();
    }

    @Step
    public DashboardPage clickOnNavDashboard() {
        clickOnLeftMenuByName("Dashboard");
        return new DashboardPage();
    }

    @Step
    public WorkflowsPage clickOnNavWorkflows() {
        clickOnLeftMenuByName("Workflows");
        return new WorkflowsPage();
    }

}

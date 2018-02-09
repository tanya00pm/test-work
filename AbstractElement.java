package ru.levelup.qa.at.test.work.page.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ru.levelup.qa.at.test.work.services.webdriver.WebDriverUtils;

public class AbstractElement {

    protected WebDriver driver;

    public AbstractElement(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        WebDriverUtils.waitForPageUpdated(driver);
    }
}

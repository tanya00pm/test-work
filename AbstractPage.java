package ru.levelup.qa.at.test.work.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ru.levelup.qa.at.test.work.page.elements.HeaderElements;
import ru.levelup.qa.at.test.work.services.webdriver.WebDriverUtils;

public class AbstractPage {

    protected WebDriver driver;

    protected HeaderElements header;

    public AbstractPage(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.header = new HeaderElements(driver);
        WebDriverUtils.waitForPageUpdated(driver);
    }

    public HeaderElements getHeader() {
        return header;
    }
}

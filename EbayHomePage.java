package ru.levelup.qa.at.test.work.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.levelup.qa.at.test.work.services.webdriver.DriverParams;

public class EbayHomePage extends AbstractPage {

    public EbayHomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(DriverParams.URL);
    }

}

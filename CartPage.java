package ru.levelup.qa.at.test.work.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.levelup.qa.at.test.work.services.webdriver.DriverParams;

import java.util.List;

public class CartPage extends AbstractPage {
    @FindBy(id = "gh-cart-1")
    private WebElement cartIcon;

    @FindBy(id = "ShopCart")
    private WebElement cartView;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(DriverParams.cartURL);
    }

    public WebElement getFirstElement() {
       return cartView.findElements(By.className("c-std")).get(0);
   }
}

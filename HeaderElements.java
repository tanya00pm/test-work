package ru.levelup.qa.at.test.work.page.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import org.openqa.selenium.By;

public class HeaderElements extends AbstractElement {

    @FindBy(id = "gh-shop-a")
    private WebElement shopByCategoryButton;

    @FindBy(id = "gh-ac")
    private WebElement searchTextBox;

    @FindBy(id = "gh-cat")
    private WebElement searchCategorySelect;

    @FindBy(id = "gh-btn")
    private WebElement searchButton;

    @FindBy(id = "gh-as-a")
    private WebElement advancedButton;

    @FindBy(id = "gh-sbc")
    private WebElement categoryTable;

    @FindBy(id = "e1-12")
    private WebElement categorySubMenu;

    @FindBy(className = "nav-module")
    private WebElement categorySubMenu2;

    @FindBy(id = "isCartBtn_btn")
    private WebElement cartButton;

    public HeaderElements(WebDriver driver) {
        super(driver);
    }

    public void sendKeysToSearchTextBox(final String searchText) {
        searchTextBox.clear();
        searchTextBox.sendKeys(searchText);
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    public void clickAdvancedSearchMode() {
        advancedButton.click();
    }

    public void clickShopByCategoryButton() {
        shopByCategoryButton.click();
    }

    public void clickCartButton() {
        cartButton.click();
    }

    public void selectCategoryByName(String categoryName)
    {
        List<WebElement> anchors = categoryTable.findElements(By.tagName("a"));
        for(WebElement a:anchors)
        {
            if(a.getText().contains(categoryName))
            {
                a.click();
                break;
            }
        }
    }

    public void selectStandardCategorySubMenu(String categoryName)
    {
        List<WebElement> anchors = categorySubMenu.findElements(By.tagName("a"));
        for(WebElement a:anchors)
        {
            if(a.getText().contains(categoryName))
            {
                a.click();
                break;
            }
        }
    }

    public void selectStandardCategorySubMenu2(String categoryName)
    {
        List<WebElement> anchors = categorySubMenu2.findElements(By.tagName("a"));
        for(WebElement a:anchors)
        {
            if(a.getText().contains(categoryName))
            {
                a.click();
                break;
            }
        }
    }
}

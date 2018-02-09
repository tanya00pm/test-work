package ru.levelup.qa.at.test.work.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SearchResultPage extends AbstractPage {

    @FindBy(className = "sort-menu-container")
    private WebElement sortMenuContainer;

    @FindBy(id = "SortMenu")
    private WebElement sortMenu;

    @FindBy(id = "ListViewInner")
    private WebElement resultView;

    @FindBy(id = "e1-12")
    private WebElement categoryBox;

    private String currentCategory;

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getCurrentCategory(){
        WebElement catw = categoryBox.findElement(By.className("rlp-b")).findElement(By.className("cat-app"));

        if(catw != null)
            return catw.getText().trim();

        return currentCategory;
    }

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    public void focusSortMenu()
    {
        new Actions(driver).moveToElement(sortMenuContainer).build().perform();
    }

    public void setSortingCriteria(String categoryName)
    {
        List<WebElement> anchors = sortMenu.findElements(By.tagName("a"));
        for(WebElement a:anchors)
        {
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.elementToBeClickable(a));

            if(a.getText().contains(categoryName))
            {
                a.click();
                break;
            }
        }
    }

    public List<WebElement> getResultElements() {
        return resultView.findElements(By.className("li"));
    }

    public Product getProductByIndex(List<WebElement> list, int index) {

        String name = "";
        double price = 0;

        name = list.get(index).findElement(By.tagName("h3")).getText();

        WebElement wprice = list.get(index).findElements(By.tagName("li")).get(0);

        price = Double.parseDouble(wprice.getText().replace(" руб.", "")
                .replace(",", ".").trim());

        return new Product(name, price);
    }

    public Product initProductByParams(WebElement webElement) {

        Product p = new Product();

        // name
        p.setName(webElement.findElement(By.tagName("h3")).getText());

        // condition 'новый'
        p.setNew(webElement.findElement(By.className("lvsubtitle")).getText().trim().equals("Совершенно новый"));

        // price
        WebElement wprice = webElement.findElements(By.tagName("li")).get(0).findElement(By.className("bold"));
        p.setPrice(
                (wprice.getText().contains("до")) ?
                        p.getMaxPriceFromRange(wprice.getText()) :
                        Double.parseDouble(wprice.getText().replace("руб.", "")
                                .replace(",", ".").replace(" ", "").trim())
        );

        // Outlet
        p.setOutlet((webElement.findElements(By.tagName("li")).get(0).getText()).contains("Было"));

        // Category
        p.setCategory(getCurrentCategory());

        // Location
        List<WebElement> es = webElement.findElements(By.tagName("li"));
        for(WebElement e:es)
        {
            if(e.getText().contains("Страна:")) {
                p.setLocationCountry(e.getText().replace("Страна: ", "").trim());
            }
        }

        return p;
    }
}

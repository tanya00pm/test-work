package ru.levelup.qa.at.test.work.tests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.levelup.qa.at.test.work.page.objects.*;
import ru.levelup.qa.at.test.work.services.webdriver.DriverParams;
import ru.levelup.qa.at.test.work.page.elements.HeaderElements;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.support.ui.Select;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BasicTest {

    protected WebDriver driver;
    protected EbayHomePage ebayHomePage;
    protected SearchResultPage searchResultPage;
    protected RegistrationFormPage registrationForm;
    protected HeaderElements element;
    protected String url;
    protected int invalidLinksCount;
    protected ArrayList<Product> productList;

    @BeforeClass
    public void setUpDriver() {
        ChromeDriverManager.getInstance().setup();
    }

    @BeforeMethod
    public void setUpTest() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(DriverParams.TIMEOUT_MS, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(DriverParams.PAGE_LOAD_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        ebayHomePage = new EbayHomePage(driver);
        ebayHomePage.open();
    }

    @Test
    public void goodsCostTest()
    {
        element = new HeaderElements(driver);

        element.sendKeysToSearchTextBox("iPhone");
        element.clickSearchButton();

        String costRange = driver.findElement(By.xpath(".//*[@id='item33d99771e6']/ul[1]/li[1]/span/span")).getText();
        String beginPriceStr = costRange.substring(0, costRange.indexOf("до"))
                .replace(" руб.", "")
                .replace(" ", "")
                .replace(",", ".")
                .trim();

        driver.findElement(By.xpath(".//*[@id='item33d99771e6']/div[1]/div/a/img")).click();

        String actualPriceStr = driver.findElement(By.id("convbidPrice")).getText()
                .replace(" руб.", "")
                .replace(" ", "")
                .replace(",", ".")
                .trim();

        Double beginPrice = Double.parseDouble(beginPriceStr);
        Double actualPrice = Double.parseDouble(actualPriceStr);

        System.out.println("Begin Price = "+ beginPrice);
        System.out.println("Actual Price = "+ actualPrice);

        Assert.assertEquals(beginPrice, actualPrice);

    }

    @Test
    public void sortingTest()
    {
        element = new HeaderElements(driver);

        element.clickShopByCategoryButton();
        element.selectCategoryByName("Гитары");
        element.selectStandardCategorySubMenu("Бас-гитары");

        searchResultPage = new SearchResultPage(driver);

        searchResultPage.focusSortMenu();
        searchResultPage.setSortingCriteria("по цене + доставке: по возрастанию");

        List<WebElement> results = searchResultPage.getResultElements();

        productList = new ArrayList<>();

        for (int i=0; i<3; i++) {

            Product p = new Product(searchResultPage.getProductByIndex(results, i).getName(),
                                    searchResultPage.getProductByIndex(results, i).getPrice());
            productList.add(p);

         }

        Assert.assertTrue((productList.get(0).getPrice() <= productList.get(1).getPrice()),
                "First product price ("+productList.get(0).getPrice()+") >= second product price ("+productList.get(1).getPrice()+")");

        Assert.assertTrue((productList.get(1).getPrice() <= productList.get(2).getPrice()),
                "Second product price ("+productList.get(1).getPrice()+") >= third product price ("+productList.get(2).getPrice()+")");

    }

    @Test
    public void goodsPurchaseTest()
    {
        login();

        element = new HeaderElements(driver);
        element.clickShopByCategoryButton();
        element.selectCategoryByName("Компьютеры и планшеты");

        driver.findElement(By.className("nav-module"))
                                .findElement(By.linkText("Аксессуары для ноутбуков и стационарных ПК"))
                                .click();

        element.selectStandardCategorySubMenu2("Гарнитуры");

        searchResultPage = new SearchResultPage(driver);
        List<WebElement> results = searchResultPage.getResultElements();

        // click random product from search results
        int  n = new Random().nextInt(50) + 1;
        results.get(n).findElement(By.tagName("h3")).findElement(By.tagName("a")).click();
        // case for specified product
        //driver.findElement(By.xpath(".//*[@id='item2128dac3af']/h3/a")).click();

        Product product  = new Product( );
        product.setName(driver.findElement(By.id("itemTitle")).getText().trim());

        // set all necessary parameters
        // ...
        // set color option if exists
        WebElement colorEl = driver.findElement(By.id("msku-sel-1"));
        if(colorEl.isDisplayed()) {
            Select color = new Select (colorEl);
            color.selectByIndex(1);
        }

        new WebDriverWait(driver, 20)
                .until(ExpectedConditions.elementToBeClickable(By.id("isCartBtn_btn")));

        element.clickCartButton();

        CartPage cartPage = new CartPage(driver);

        for(WebElement e:
                cartPage.getFirstElement().findElements(By.tagName("a")))
        {
            if(e.getAttribute("href").contains("itm")) {
                e.click();
                break;
            }
        }

        WebElement title= driver.findElement(By.id(("itemTitle")));
        Product productCart  = new Product( );
        productCart.setName(title.getText().trim());

        // comparing can be performed for several parameters
        // here by name as example
        if(productCart.equalsByName(product))
            System.out.println("Product has been added to cart succesfully");
        Assert.assertTrue(productCart.equalsByName(product), "product in cart '"
                + productCart.getName() + "' is NOT similar to product choosen '"+product.getName()+"'");

    }

    @Test
    public void emailValidationTest()
    {
        driver.findElement(By.xpath(".//*[@id='gh-ug-flex']/a")).click();

        registrationForm = new RegistrationFormPage(driver);
        registrationForm.initMandatoryFields("test name", "test last name",
                                            DriverParams.testEmail,  "000111a");

        WebElement registrationButton = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.elementToBeClickable(By.id("ppaFormSbtBtn")));

        registrationButton.click();

        String mainContent = driver.findElement(By.id("mainContent")).getText();
        String subStrValidation = "Учетная запись с этим адресом эл. почты уже зарегистрирована "
                + DriverParams.testEmail + ".";

        url = driver.getCurrentUrl();
        Assert.assertTrue(url.equals("https://reg.ebay.com/reg/PartialReg"), "Wrong URL redirection");

        Assert.assertTrue(mainContent.contains(subStrValidation), "Something wrong with email validation page.");
    }

    @Test
    public void linksValidationTest()
    {
        element = new HeaderElements(driver);
        element.clickShopByCategoryButton();

        List<WebElement> links = driver.findElement(By.id("gh-sbc")).findElements(By.tagName("a"));
        for(WebElement link:links) {
            String linkURL = link.getAttribute("href");
            System.out.println("Verifying of " + linkURL);
            if (linkURL != null && !linkURL.contains("javascript"))
                verifyURLStatus(linkURL, link.getText());
            else {
                System.out.println("Invalid URL " + linkURL + " for menu item "+ link.getText());
                invalidLinksCount++;
            }
        }
        System.out.println("Total count of invalid links is succesfully" + invalidLinksCount);
    }

    @Test
    public void advancedSearchTest()
    {
        element = new HeaderElements(driver);
        element.clickAdvancedSearchMode();

        Product pToCompare = new Product();

        // крем
        driver.findElement(By.xpath(".//*[@id='_nkw']")).sendKeys("крем");
        pToCompare.setName("крем");

        // condition 'новый'
        if ( !driver.findElement(By.id("LH_ItemConditionNew")).isSelected() )
            driver.findElement(By.id("LH_ItemConditionNew")).click();
        pToCompare.setNew(true);

        // price <= 1500 RUR
        driver.findElement(By.xpath(".//*[@id='adv_search_from']/fieldset[3]/input[3]")).sendKeys("15 000");
        pToCompare.setPrice(1500);

        // Category 'Красота и здоровье'
        Select category = new Select (driver.findElement(By.id("e1-1")));
        category.selectByVisibleText("Красота и здоровье");
        pToCompare.setCategory("Красота и здоровье");

        // Outlet
        if ( !driver.findElement(By.id("LH_SaleItems")).isSelected() )
            driver.findElement(By.id("LH_SaleItems")).click();
        pToCompare.setOutlet(true);

        // Location Radio Button
        driver.findElement(By.xpath(".//*[@id='LH_LocatedInRadio']")).click();
        // Location 'Китай'
        Select locationCountry = new Select (driver.findElement(By.id("_salicSelect")));
        locationCountry.selectByVisibleText("Китай");
        pToCompare.setLocationCountry("Китай");

        // click Search
        driver.findElement(By.id("searchBtnLowerLnk")).click();

        /* -- init products from search result page and compare to pattern Product 'pToCompare' -- */
        productList = new ArrayList<>();

        searchResultPage = new SearchResultPage(driver);
        List<WebElement> results = searchResultPage.getResultElements();

        for(WebElement res:results) {

            Product productItem = searchResultPage.initProductByParams(res);

            /*
            if(productItem.equalsByParams(pToCompare))
                System.out.println("equals");
            else
                System.out.println("item with name '" + productItem.getName() + "' is NOT equal to compare Item.");
                */
            Assert.assertTrue(productItem.equalsByParams(pToCompare), "item with name '" + productItem.getName() + "' is NOT equal to compare Item.");

            productList.add(searchResultPage.initProductByParams(res));
        }
        /* --------------------------------------------------------- */

    }

    @AfterMethod
    public void tearDown() {
       // driver.close();
    }

    public void verifyURLStatus(String URL, String itemName) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Invalid URL " + URL + " for menu item "+ itemName +
                        "; Status code "+response.getStatusLine().getStatusCode());
                invalidLinksCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // for test #2
    public void login() {
        driver.findElement(By.xpath(".//*[@id='gh-ug']/a")).click();
        driver.findElement(By.id("userid")).sendKeys(DriverParams.testEmail);
        driver.findElement(By.id("pass")).sendKeys(DriverParams.testPwd);
        driver.findElement(By.xpath(".//*[@id='sgnBt']")).click();
    }
}

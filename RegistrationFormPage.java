package ru.levelup.qa.at.test.work.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class RegistrationFormPage extends AbstractPage {

    @FindBy(xpath = ".//*[@id='firstname']")
    private WebElement userFirstName;

    @FindBy(xpath = ".//*[@id='lastname']")
    private WebElement userLastName;

    @FindBy(id = "email")
    private WebElement userEmail;

    @FindBy(id = "PASSWORD")
    private WebElement userPwd;


    public RegistrationFormPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public RegistrationFormPage initMandatoryFields(String firstName, String lastName, String email, String password)
    {
        userFirstName.sendKeys(firstName);
        userLastName.sendKeys(lastName);
        userEmail.sendKeys(email);
        userPwd.sendKeys(password);

        return new RegistrationFormPage(driver);
    }

}

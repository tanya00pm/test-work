package ru.levelup.qa.at.test.work.services.webdriver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class WebDriverUtils {

    /**
     * JavaScript code to check if all the ajax requests completed
     */
    private static final String JS_AJAX_PROGRESS =
            "var userWindow = window;" +
                    "var docReady = window.document.readyState == 'complete';" +
                    "var isJqueryComplete = typeof(userWindow.jQuery) != 'function' || userWindow.jQuery.active == 0;" +
                    "var isPrototypeComplete = typeof(userWindow.Ajax) != 'function' || userWindow.Ajax.activeRequestCount == 0;" +
                    "var isDojoComplete = typeof(userWindow.dojo) != 'function' || userWindow.dojo.io.XMLHTTPTransport.inFlight.length == 0;" +
                    "var result = docReady && isJqueryComplete && isPrototypeComplete && isDojoComplete;" +
                    "return result;";

    private static final ExpectedCondition<Object> isAJAXCompleted = new ExpectedCondition<Object>() {
        @Override
        public Boolean apply(WebDriver webDriver) {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            return Boolean.parseBoolean(js.executeScript(JS_AJAX_PROGRESS).toString());
        }
    };

    public static void waitForPageUpdated(WebDriver driver) {
        Wait<WebDriver> wait = new WebDriverWait(driver, DriverParams.TIMEOUT_MS/ 7000, 20);
        wait.until(isAJAXCompleted);
        driver.manage().timeouts().setScriptTimeout(DriverParams.TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

}

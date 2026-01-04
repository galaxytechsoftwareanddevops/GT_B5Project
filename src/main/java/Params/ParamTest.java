package Params;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ParamTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    @Parameters({"browser"})
    public void setup(String browser) {

        if (browser == null || browser.isEmpty()) {
            throw new RuntimeException("Browser parameter is missing in testng.xml");
        }

        switch (browser.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            default:
                throw new RuntimeException("Invalid browser name: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(dataProvider = "dp")
    public void automationExerciseLogin(String email, String password) {

        driver.get("https://automationexercise.com/login");

        WebElement userName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id='form']/div/div/div[1]/div/form/input[2]"))
        );
        userName.sendKeys(email);

        WebElement pwd = driver.findElement(
                By.xpath("//*[@id='form']/div/div/div[1]/div/form/input[3]"));
        pwd.sendKeys(password);

        WebElement login = driver.findElement(
                By.xpath("//*[@id='form']/div/div/div[1]/div/form/button"));
        login.click();

        WebElement homeButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id='header']/div/div/div/div[2]/div/ul/li[1]/a"))
        );

        Assert.assertTrue(homeButton.isDisplayed(),
                "Login failed for user: " + email);

        System.out.println("Login successful for: " + email);

        // Logout
        driver.findElement(
                By.xpath("//*[@id='header']/div/div/div/div[2]/div/ul/li[4]/a"))
                .click();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "dp")
    public Object[][] loginData() {
        return new Object[][]{
                {"student1galaxytech@gmail.com", "Admin@123"},
                {"galaxytechtestinghub@gmail.com", "Admin@123"},
                {"student3galaxytech@gmail.com", "Admin@123"}
        };
    }
}

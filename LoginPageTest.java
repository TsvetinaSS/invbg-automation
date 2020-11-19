package ui.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ui.enums.Message;
import ui.pages.HomePage;
import ui.pages.LoginPage;

public class LoginPageTest {
    //This is the place where we write all tests related to Login page
    private WebDriver driver;
    private final String BLANK = "";
    private final String VALID_EMAIL = "karamfilovs@gmail.com";
    private final String VALID_PASSWORD = "123456";


    @BeforeAll
    public static void beforeAll(){
        //This is setup needed for chrome browser ( you can change it to Firefox)
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach(){
        //Start the browser Chrome (You can change it to Firefox
        driver = new ChromeDriver();
        //Delete all browser cookies just for safety or to terminate existing session
        driver.manage().deleteAllCookies();
        //Maximize browser to be able to see better (when old like me)
        driver.manage().window().maximize();
    }

    @Test
    @DisplayName("Cant login with blank credentials(email/password)")
    public void cantLoginWithBlankCredentials(){
        //Create Login page equivalent/abstraction
        LoginPage loginPage = new LoginPage(driver);
        //Navigates to login page
        loginPage.goTo();
        //Enter email
        loginPage.enterEmail(BLANK);
        //Enter password
        loginPage.enterPassword(BLANK);
        //Click Login button
        loginPage.clickLoginButton();
        //Assert that the error message is correct (expected | actual)
        Assertions.assertEquals(Message.BLANK_LOGIN.getMessage(), loginPage.getErrorMessage());
    }

    @Test
    @DisplayName("Cant login with invalid credentials(email/password)")
    public void cantLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        //Navigates to login page
        loginPage.goTo();
        //Enter email
        loginPage.enterEmail(VALID_EMAIL);
        //Enter password
        loginPage.enterPassword("12345678");
        //Click Login button
        loginPage.clickLoginButton();
        //Assert that the error message is correct (expected | actual)
        Assertions.assertEquals(Message.BAD_USERNAME_PASSWORD.getMessage(), loginPage.getErrorMessage());
    }


    @Test
    @DisplayName("Cant login with blank password")
    public void cantLoginWithBlankPassword() {
        LoginPage loginPage = new LoginPage(driver);
        //Navigates to login page
        loginPage.goTo();
        //Enter email
        loginPage.enterEmail(VALID_EMAIL);
        //Enter password
        loginPage.enterPassword(BLANK);
        //Click Login button
        loginPage.clickLoginButton();
        //Assert that the error message is correct (expected | actual)
        Assertions.assertEquals(Message.BAD_USERNAME_PASSWORD.getMessage(), loginPage.getErrorMessage());
    }
    @Test
    @DisplayName("Can login with valid username & password")
    public void canLoginWithValidCredentials(){
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);
        //Assert that the user is logged in
        Assertions.assertEquals(VALID_EMAIL, homePage.getUserPanelText());
    }

    @Test
    @DisplayName("Can login with valid username & password and logout")
    public void canLoginWithValidCredentialsAndLogout(){
        LoginPage loginPage = new LoginPage(driver);
        //Create Home page abstraction
        HomePage homePage = new HomePage(driver);
        //Login with default username password (here I use mine)
        loginPage.defaultLogin();
        //Assert that the user is logged in
        Assertions.assertEquals(VALID_EMAIL, homePage.getUserPanelText());
        //Logout
        homePage.logout();
        //Assert that the logout message is correct (expected | actual)
        Assertions.assertEquals(Message.LOGOUT_SUCCESS.getMessage(), loginPage.logoutSuccessMessage());
    }

    @AfterEach
    public void afterEach(){
        //Kill the damn browser
        driver.quit();
    }
}

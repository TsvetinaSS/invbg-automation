package ui.tests;

import api.Item;
import api.ItemAPI;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ui.enums.Message;
import ui.pages.ItemPage;
import ui.pages.LoginPage;

public class ItemPageTest {
    private WebDriver driver;

    @BeforeAll
    public static void beforeAll(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach(){
        //Start the browser
        driver = new ChromeDriver();
        //Delete all browser cookies just for safety or to terminate existing session
        driver.manage().deleteAllCookies();
        //Maximize browser to be able to see better (when old like me)
        driver.manage().window().maximize();
    }

    @AfterEach
    public void afterEach(){
        //Kill the damn browser
        driver.quit();
    }

    @Test
    @DisplayName("Can create item")
    public void canCreateItem(){
        LoginPage loginPage = new LoginPage(driver);
        ItemPage itemPage = new ItemPage(driver);
        ItemAPI itemAPI = new ItemAPI();

        loginPage.defaultLogin();
        for(int i=0; i< 5; i++){
            itemPage.goTo();
            itemPage.createItem("UI Test for Item Creation " + i);
            //Assert successful item creation message
            Assertions.assertEquals(Message.SUCCESS_ITEM_CREATE.getMessage(), itemPage.getSuccessCreateMessage());
        }
        itemPage.goTo();
        itemAPI.cleanAllItems();

    }

    @Test
    @DisplayName("Can search for not matching items")
    public void canSearchForNotMatchingItems(){
        String itemName = "UI Test for Item Creation";
        LoginPage loginPage = new LoginPage(driver);
        ItemPage itemPage = new ItemPage(driver);
        ItemAPI itemAPI = new ItemAPI();
        loginPage.defaultLogin();
        itemPage.goTo();
        itemPage.createItem(itemName);
        //Assert successful item creation message
        Assertions.assertEquals(Message.SUCCESS_ITEM_CREATE.getMessage(), itemPage.getSuccessCreateMessage());
        itemAPI.cleanAllItems();
        itemPage.goTo();
        itemPage.clickSearchButton();
        itemPage.enterItemToSearchFor(itemName);
        itemPage.triggerItemSearch();
        Assertions.assertEquals(Message.EMPTY_ITEM_LIST.getMessage(), itemPage.getEmptyResultMessage());
    }
}

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.example.DriverFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.PageFactory;

import java.net.MalformedURLException;
import java.time.Duration;

public class Module5 {
    private final DriverFactory driverFactory = new DriverFactory();
    private AndroidDriver<?> driver;

    @AndroidFindBy(accessibility = "Каталог")
    MobileElement catalogElement;

    @AndroidFindBy(id = "block_search_widget")
    MobileElement searchWidget;

    @AndroidFindBy(id = "search_src_text")
    MobileElement searchText;

    @AndroidFindBy(uiAutomator = "resourceIdMatches(\".*id/title\").textContains(\"товаров\")")
    MobileElement foundTitle;

    @AndroidFindBy(id = "filter_label")
    MobileElement filterLabel;

    @AndroidFindBy(id = "filter_switch")
    MobileElement filterSwitch;

    @AndroidFindBy(id = "apply_filters")
    MobileElement applyFiltersButton;

    @AndroidFindBy(id= "specify_category_pager")
    MobileElement specifyCategoryPager;

    @Before
    public void setDriver() throws MalformedURLException{
        driver = driverFactory.setUp();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Test
    public void homeTest{
        Assert.assertFalse(catalogElement.isSelected());
        catalogElement.click();
        swipe(Direction.UP);
        swipe(Direction.DOWN);
        searchWidget.click();
        String television = "Телевизор";
        searchText.sendKeys(television);
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        String foundTitleText = foundTitle.getText();
        filterLabel.click();
        filterSwitch.click();
        applyFiltersButton.click();
        String foundSaleTitleText = foundTitle.getText();
        Assert.assertNotEquals(foundTitleText, foundSaleTitleText);
        driver.lockDevice(Duration.ofSeconds(3));
        Assert.assertEquals(television,searchText.getText());

    }

    @After
    public void tearDown(){
        driver.quit();
    }

    private enum Direction{
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    private void swipe(Direction direction){
        int border = 5;
        int press = 300;
        Dimension dims = driver.manage().window().getSize();
        PointOption<?> pointOptionStart = PointOption.point(dims.width / 2, dims.height /2);
        PointOption<?> pointOptionEnd;
        switch (direction){
            case RIGHT:
                pointOptionEnd = PointOption.point(dims.width - border,dims.height /2 );
                break;
            case LEFT:
                pointOptionEnd = PointOption.point(border, dims.height /2);
                break;
            case UP:
                pointOptionEnd = PointOption.point(dims.width/2, border);
                break;
            case DOWN:
                pointOptionEnd = PointOption.point(dims.width/2, dims.height- border);
                break;
            default:
                throw new IllegalArgumentException("swipe is not supported");

        }
        new TouchAction<>(driver)
                .press(pointOptionStart)
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(press)))
                .moveTo(pointOptionEnd)
                .release()
                .perform();

    }
}

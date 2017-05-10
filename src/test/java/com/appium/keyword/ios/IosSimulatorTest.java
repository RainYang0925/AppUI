package com.appium.keyword.ios;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.appium.element.MyElement;
import com.appium.keyword.Engine_Excel;
import com.appium.locator.AppLocator;
import com.appium.locator.GetLocator;
import com.framework.utils.ConfigManager;
import com.framework.utils.ExcelUtils;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import com.appium.locator.IOSLocator;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class IosSimulatorTest {

	protected static AppLocator<MobileElement> locator;
	protected static Engine_Excel keywordTest;
	protected ExcelUtils excelUtils;

	@BeforeClass
	public void beforeClass() throws IOException, InterruptedException {
		String iOSExcelTestPath = ConfigManager.getIOSExcelName();
		excelUtils = new ExcelUtils(iOSExcelTestPath);
		locator = new GetLocator().getIOSSimLocator();
		keywordTest = new Engine_Excel(iOSExcelTestPath);
	}

	@Test
	public void test_iOSNsPredicateString() {
		keywordTest.runTest(new MainKeyWord(locator));
	}
	
	@AfterClass
	public void afterClass() {
		locator.quit();
	}
	
}

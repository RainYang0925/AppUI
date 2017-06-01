package com.appium.keyword.ios;

import com.appium.keyword.Engine_Excel;
import com.appium.locator.AppLocator;
import com.appium.locator.GetLocator;
import com.framework.utils.ConfigManager;
import com.framework.utils.ExcelUtils;
import io.appium.java_client.MobileElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

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

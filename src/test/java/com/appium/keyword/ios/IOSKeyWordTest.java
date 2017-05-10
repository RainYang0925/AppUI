package com.appium.keyword.ios;

import java.io.IOException;

import com.framework.utils.ConfigManager;
import com.framework.utils.ExcelUtils;
import com.framework.utils.SleepUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.appium.keyword.Engine_Excel;
import com.appium.locator.AppLocator;
import com.appium.locator.GetLocator;

import io.appium.java_client.MobileElement;
import org.testng.annotations.Test;

public class IOSKeyWordTest {
	
	protected static AppLocator<MobileElement> locator;
	protected static Engine_Excel keywordTest;
	protected ExcelUtils excelUtils;

	@BeforeSuite
	public void beforeSuite() throws IOException, InterruptedException {
		String iOSExcelTestPath = ConfigManager.getIOSExcelName();
		excelUtils = new ExcelUtils(iOSExcelTestPath);
		locator = new GetLocator().getIOSLocator(excelUtils.getIOSDeviceTestConfig(1));
		keywordTest = new Engine_Excel();
	}

	@Test
	public void test() {
		keywordTest.runTest(new MainKeyWord(locator));
	}

	@AfterSuite
	public void afterSuite() {
		locator.quit();
	}

}

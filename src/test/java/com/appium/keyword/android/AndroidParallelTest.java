package com.appium.keyword.android;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.appium.locator.GetLocator;
import com.framework.utils.ConfigManager;
import com.framework.utils.ExcelUtils;
import com.framework.utils.SleepUtils;
import com.appium.keyword.Engine_Excel;
import com.appium.locator.AndroidLocator;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;

public class AndroidParallelTest {

	protected Logger logger = LogManager.getLogger(getClass());
	protected AndroidLocator<MobileElement> locator;
	protected ExcelUtils excelUtils;
	protected Engine_Excel engine;

	@BeforeTest
	@Parameters({"deviceNum"})
	public void beforeTest(int deviceNum) throws IOException, InterruptedException {
		String androidExcelTestPath = ConfigManager.getAndroidExcelName();
		excelUtils = new ExcelUtils(androidExcelTestPath);
		engine = new Engine_Excel(androidExcelTestPath);
		locator = new GetLocator().getAndroidLocator(excelUtils.getAndroidDeviceTestConfig(deviceNum));
	}
	
	@Test
	public void testLogin() {
		engine.runTest(new MainKeyWord(locator));
	}

	@AfterTest
	public void afterTest() {
		locator.quit();
	}
}

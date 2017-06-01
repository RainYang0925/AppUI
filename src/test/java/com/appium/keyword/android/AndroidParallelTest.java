package com.appium.keyword.android;

import com.appium.keyword.Engine_Excel;
import com.appium.locator.AndroidLocator;
import com.appium.locator.GetLocator;
import com.framework.utils.ConfigManager;
import com.framework.utils.ExcelUtils;
import io.appium.java_client.MobileElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

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

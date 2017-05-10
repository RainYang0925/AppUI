package com.appium.keyword.android;

import java.io.IOException;

import org.testng.annotations.*;

import com.appium.keyword.Engine_Excel;
import com.appium.locator.AndroidLocator;
import com.appium.locator.GetLocator;
import com.framework.utils.ConfigManager;
import com.framework.utils.ExcelUtils;

import io.appium.java_client.MobileElement;

public class AndroidSimpleTest {
	
	protected AndroidLocator<MobileElement> locator;
	protected ExcelUtils excelUtils;
	protected Engine_Excel engine;
	
	@BeforeTest
	@Parameters({"deviceNum"})
	public void beforeTest(int deviceNum) throws IOException {
		String androidExcelTestPath = ConfigManager.getAndroidExcelName();
		excelUtils = new ExcelUtils(androidExcelTestPath);
		engine = new Engine_Excel(androidExcelTestPath);
		locator = new GetLocator().getAndroidLocator(excelUtils.getAndroidDeviceTestConfig(deviceNum));
	}

	@Test
	public void test() {
		engine.runTest(new MainKeyWord(locator));
	}
	
	@AfterTest
	public void afterTest() {
		locator.quit();
	}
}

package com.framework.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.appium.keyword.data.CaseDatas;

public class ExcelUtilsTest {

	private Logger logger = LogManager.getLogger();
	private ExcelUtils excelUtils;
	
	@BeforeClass
	public void BeforeClass() {
		String fullPath = System.getProperty("user.dir") + "/testDatas/vvm-android-test-appium-demo.xlsx";
		excelUtils = new ExcelUtils(fullPath);
	}
	
	@Test
	public void test_readTestCases() {
		logger.traceEntry();
		long start  = System.currentTimeMillis();
		CaseDatas caseDatas = excelUtils.getTestCase("登录", 1);
		long end = System.currentTimeMillis();
		logger.info(caseDatas.toString());
		System.out.println(end - start);
	}
	
	@Test
	public void test_getExcelType() {
		logger.traceEntry();
		Assert.assertEquals(excelUtils.getExcelType(), ExcelUtils.XLSX);
	}
	
	@Test
	public void test_readExcel() {
		logger.traceEntry();
		Assert.assertNotNull(excelUtils.readExcelByAllSheets());
	}
	
	@Test
	public void test_getSheetNames() {
		logger.traceEntry();
		Assert.assertNotNull(excelUtils.getSheetNames());
	}
	
	@Test
	public void test_allSheets() {
		logger.traceEntry();
		Map<String, List<HashMap<String, String>>> mapByAllSheets = excelUtils.readExcelByAllSheets();
		String[] sheetName = excelUtils.getSheetNames();
		for (int i = 0; i < mapByAllSheets.size(); i++) {
			Assert.assertNotNull(mapByAllSheets.get(sheetName[i]));
		}
	}
	
	@Test
	public void test_readSheet() {
		List<HashMap<String, String>> listMap = excelUtils.readSheet(ExcelConfig.ConfigSheet);
		for (int i = 0; i < listMap.size(); i++) {
			Map<String, String> datas = listMap.get(i);
			System.err.println(datas.get(ExcelConfig.Device_Name) + " - " + datas.get(ExcelConfig.BundleId) + " - " + datas.get(ExcelConfig.UDID));
		}
	}

	@Test
	public void test_isSheetExist() {
		Assert.assertTrue(excelUtils.isSheetExist(ExcelConfig.ConfigSheet));
	}
}

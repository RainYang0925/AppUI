package com.framework.utils;

import org.testng.annotations.Test;

public class FileUtilsTest {

	@Test
	public void test_getClassPath() {
		System.out.println(FileUtils.getClassPath(getClass()));
	}
	
	@Test
	public void test_getDeleteFile() {
		FileUtils.deleteFile("/Users/gaojiewen/Documents/workspace/AppUI/test-result/report/vvm-client-report-2017_04_07_18_17_13_191.html");
	}
}

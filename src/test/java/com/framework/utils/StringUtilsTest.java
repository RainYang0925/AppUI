package com.framework.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringUtilsTest {

	@Test
	public void test_isEmpty() {
		Assert.assertTrue(StringUtils.isEmpty(null));
		Assert.assertTrue(StringUtils.isEmpty(""));
	}
	
	@Test
	public void test_isNotEmpty() {
		Assert.assertTrue(StringUtils.isNotEmpty("test"));
	}
	
	@Test
	public void test_breakupString() {
		String srcString = "/Users/gaojiewen/Downloads";
		String[] decString = {"", "Users", "gaojiewen", "Downloads"};
		Assert.assertEquals(StringUtils.breakupString(srcString, "/"), decString);
	}
	
	@Test
	public void test_inputString() {
		
	}
}

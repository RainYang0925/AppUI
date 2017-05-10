package com.appium.locator;

/**
 * 从 xml 文件读取到的元素定位类型，匹配后，使用指定的定位方式
 * 
 * */

public interface LocatorType {

	String className = "className";
	String AccessibilityId = "AccessibilityId";
	String xpath = "xpath";
	String id = "id";
	String linkText = "linkText";
	String cssSelector = "cssSelector";
	String name = "name";
	String AndroidUIAutomator = "AndroidUIAutomator";
	String IosUIAutomation = "IosUIAutomation";
	String iOSNsPredicate = "iOSNsPredicate";
	String iOSClassChain = "iOSClassChain";
}

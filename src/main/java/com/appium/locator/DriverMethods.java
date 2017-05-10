package com.appium.locator;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.AppiumDriver;

/**
 * 判断 driver 的一些方法，判断是否为空，sessionId 是否为空
 * 
 * @version 1.0
 * 
 */

public class DriverMethods {

	/**
	 * 判断 AppiumDriver 是否为空
	 * 
	 * @param driver {@link org.openqa.selenium.WebDriver}
	 * 
	 * @return true or false
	 */
	public static boolean isEmpty(final WebDriver driver) {
		return driver == null ? true : false ;
	}
	
	/**
	 * 判断 AppiumDriver 是否不为空
	 * 
	 * @param driver {@link org.openqa.selenium.WebDriver}
	 * 
	 * @return true or false
	 */
	public static boolean isNotEmpty(final WebDriver driver) {
		return !isEmpty(driver);
	}
	
	/**
	 * 判断 AppiumDriver 的 SessionId 是否为空
	 * 
	 * @param driver {@link io.appium.java_client.AppiumDriver}
	 * 
	 * @return true or false
	 */
	public static boolean sessionIdIsEmpty(final AppiumDriver<?> driver) {
		return driver == null ? true : false;
	}
	
	/**
	 * 判断 AppiumDriver 的 SessionId 是否不为空
	 * 
	 * @param driver {@link io.appium.java_client.AppiumDriver}
	 * 
	 * @return true or false
	 */
	public static boolean sessionIdIsNotEmpty(final AppiumDriver<?> driver) {
		return !sessionIdIsEmpty(driver);
	}
}

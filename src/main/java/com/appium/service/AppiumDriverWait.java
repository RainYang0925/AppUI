package com.appium.service;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 简单封装 Selenium 的 WebDriverWait 方法，实现移动端的元素等待
 * 
 * @version 1.0
 * 
 */

public class AppiumDriverWait extends WebDriverWait {

	/**
	 * 主叫将 driver 从 WebDriver 变为 AppiumDriver
	 * 
	 * @param driver {@link io.appium.java_client.AppiumDriver}
	 * @param timeOutInSeconds 超时秒数
	 * 
	 */
	public AppiumDriverWait(AppiumDriver<?> driver, long timeOutInSeconds) {
		super(driver, timeOutInSeconds);
	}
	
}

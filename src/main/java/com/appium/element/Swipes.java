package com.appium.element;

import com.appium.locator.Locator;
import io.appium.java_client.MobileElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;

/**
 * 滑动功能脚本（Appium_Java）包含：上滑、下滑、左滑、右滑、滑动返回、下滑通知中心、收回通知中心，左滑元素
 * 
 * @version 1.2
 * 
 */

public class Swipes {
	
	private int width;
	private int height;
	private Logger logger = LogManager.getLogger(this.getClass());
	private Locator<? extends MobileElement> locator;

	/**
	 * 构造函数，使用指定的 Locator
	 * 
	 * @param locator {@link com.appium.locator.Locator}
	 * 
	 */
	public Swipes(Locator<? extends MobileElement> locator) {
		this.locator = locator;
		Dimension windowsSize = locator.getWindowSize();
		width = windowsSize.width;
		height = windowsSize.height;
	}

	/**
	 * 向上滑动的方法
	 * 
	 */
	public void swipeToUp() {
		int startX = width / 2,
				startY = height * 4 / 5;
		int endX = startX,
				endY = height / 5;
		logger.info("swipe to up");
		locator.swipe(startX, startY, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}

	/**
	 * 向下滑动的方法
	 * 
	 */
	public void swipeToDown() {
		int startX = width / 2,
				startY = height / 5;
		int endX = startX, 
				endY = height * 4 / 5;
		logger.info("swipe to down");
		locator.swipe(startX, startY, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}

	/**
	 * 向左滑动的方法
	 * 
	 */
	public void swipeToLeft() {
		int startX = width * 9 / 10,
				startY = height / 2;
		int endX = width / 10,
				endY = startY;
		logger.info("swipe to left");
		locator.swipe(startX, startY, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}

	/**
	 * 向右滑动的方法
	 * 
	 */
	public void swipeToRight() {
		int startX = width / 10,
				startY = height / 2;
		int endX = width * 9 / 10,
				endY = startY;
		logger.info("swipe to right");
		locator.swipe(startX, startY, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}

	/**
	 * 滑动返回的方法
	 * 
	 */
	public void swipeBack() {
		int startY = height / 2;
		int endX = width * 3 / 4,
				endY = startY;
		logger.info("swipe back");
		locator.swipe(0, startY, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}

	/**
	 * 下滑通知中心的方法
	 * 
	 */
	public void swipeStatusBar() {
		int startX = width / 2;
		int endX = startX,
				endY = height * 3 / 4;
		logger.info("swipe the status bar down");
		locator.swipe(startX, 0, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}

	/**
	 * 将通知中心滑动收回的方法
	 * 
	 */
	public void swipeStatusBarBack() {
		int startX = width / 2,
				startY = height - 5;
		int endX = startX,
				endY = height / 4;
		logger.info("swipe the open status bar back");
		locator.swipe(startX, startY, endX, endY, Locator.DEFAULT_SWIPE_MILLSECONDS);
	}
	
}

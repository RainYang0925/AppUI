package com.appium.keyword;

import com.appium.element.Swipes;
import com.framework.utils.SleepUtils;
import io.appium.java_client.MobileBy;

/**
 * Android App关键字框架基类，实现一些Android常用的操作
 * 
 * @version 1.2
 * 
 */

public abstract class AndroidKeyWord extends BaseKeyWord {

	/**
	 * 点击开关，需等待加载完，才能进行下一步操作
	 * 
	 */
	public void clickSwitch() {
		if (locator.isElementExist(elementBy)) {
			click();
		} else {
			Engine_Excel.bResult = false;
		}
		do {
			locator.setWaitTime(1);
		} while (locator.isElementExist(MobileBy.className("android.widget.ProgressBar")));
	}
	
	/**
	 * 返回键的操作，点击返回键，会发生页面变动，需重新查找元素，需加等待时间
	 * 
	 */
	public void clickBack() {
		SleepUtils.threadSleepBySeconds(1);
		click();
	}

	/**
	 * 向上滑动的操作
	 */
	public void swipeToUp() {
		new Swipes(locator).swipeToUp();
	}
}

package com.framework.utils;

import com.appium.locator.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 封装线程等待方法
 * 
 * @version 1.0
 * 
 */

public class SleepUtils {
	
	private static Logger logger = LogManager.getLogger();

	/**
	 * 线程等待方法，以秒计算
	 * 
	 * @param seconds 等待时间（单位：s）
	 * 
	 */
	public static void threadSleepBySeconds(int seconds) {
		try {
			logger.info("准备线程睡眠：" + seconds + " 秒");
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			logger.error("线程睡眠时，发生错误", e);
		}
	}
	
	/**
	 * appium 的显式等待
	 * 
	 * @param locator {@link com.appium.locator.Locator}
	 * @param seconds 最长等待秒数
	 * 
	 */
	public static void appiumSleepBySeconds(Locator<?> locator, int seconds) {
		locator.waitElement(seconds);
	}
	
	/**
	 * 线程等待方法，以毫秒计算
	 * 
	 * @param millis  等待时间（单位：ms）
	 *
	 */
	public static void threadSleepByMillis(long millis) {
		try {
			logger.info("准备线程睡眠：" + millis + " 毫秒");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error("线程睡眠时，发生错误", e);
		}
	}
}

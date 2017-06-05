package com.appium.locator;

import com.appium.element.MyElement;
import com.appium.manager.android.AndroidConfig;
import com.appium.manager.android.AndroidDeviceInfo;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.openqa.selenium.WebDriverException;

import java.util.List;

/**
 * Android 的定位类，用来封装  AndroidDriver，直接调用
 * 
 * @version 1.1
 * 
 */

public class AndroidLocator<T extends MobileElement> extends AppLocator<T> {

	private AndroidDriver<T> androidDriver;
	private AndroidDeviceInfo androidDeviceInfo;
	private AndroidConfig androidConfig = new AndroidConfig();

	/**
	 * 构造函数，使用指定的 AndroidDriver
	 * 
	 * @param androidDriver {@link io.appium.java_client.android.AndroidDriver}
	 * 
	 */
	public AndroidLocator(AndroidDriver<T> androidDriver) {
		super(androidDriver);
		this.androidDriver = androidDriver;
	}
	
	/**
	 * 构造函数，使用指定的 AndroidDriver，自定义的元素等待时间
	 * 
	 * @param androidDriver {@link io.appium.java_client.android.AndroidDriver}
	 * @param element_wait_seconds 元素等待秒数
	 * 
	 */
	public AndroidLocator(AndroidDriver<T> androidDriver, int element_wait_seconds) {
		super(androidDriver, element_wait_seconds);
		this.androidDriver = androidDriver;
	}
	
	/**
	 * 获取 AndroidLocator 的 driver，以便使用未封装的方法。
	 * 
	 */
	@Override
	public AndroidDriver<T> getDriver() {
		return androidDriver;
	}

	public void setDeviceInfo(AndroidDeviceInfo deviceInfo) {
		this.androidDeviceInfo = deviceInfo;
	}

	@Override
	public AndroidDeviceInfo getDeviceInfo() {
		return androidDeviceInfo;
	}

	/**
	 * 封装 Applocator 的 quit 方法，并添加设置默认输入法
	 * 
	 */
	@Override
	public void quit() {
		super.quit();
		androidConfig.setDefaultInputMethod(androidDeviceInfo.getDeviceID(), androidDeviceInfo.getDefaultInputMethod());
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 AndroidUIAutomator 方式，查找单一元素
	 * 
	 * @param uiautomatorText uiautomator的定位信息
	 * 
	 * @return MyElement {@link MyElement}
	 * 
	 */
	public MyElement getLocatorByAndroidUIAutomator(String uiautomatorText) {
		return super.getLocator(MobileBy.AndroidUIAutomator(uiautomatorText));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 AndroidUIAutomator 方式，查找元素集合
	 * 
	 * @param uiautomatorText uiautomator的定位信息
	 * 
	 * @return MyElements {@link MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByAndroidUIAutomator(String uiautomatorText) {
		return super.getLocators(MobileBy.AndroidUIAutomator(uiautomatorText));
	}

	/**
	 * 打开指定 appPackage 的 appActivity 
	 * 
	 * @param appPackage 应用的包名
	 * @param appActivity 应用的 Activity 页面
	 * 
	 */
	public void startActivity(String appPackage, String appActivity) {
		try {
			androidDriver.startActivity(new Activity(appPackage, appActivity));
			logger.info("在 [\"" + appPackage + "\"] 应用，启动 [\"" + appActivity +"\"] 页面");
		} catch (WebDriverException e) {
			logger.error("在 [\"" + appPackage + "\"] 应用，启动 [\"" + appActivity +"\"] 页面失败\n", e);
		}
	}
	
	/**
	 * 打开通知中心
	 * 
	 */
	public void openNotifications() {
		try {
			androidDriver.openNotifications();
			logger.info("打开通知中心");
		} catch (WebDriverException e) {
			logger.error("打开通知中心错误\n", e);
		}
	}
	
	/**
	 * 点击系统的返回按钮
	 * 
	 * 
	 */
	public void pressAndroidCodeBack() {
		try {
			androidDriver.pressKeyCode(AndroidKeyCode.BACK);
			logger.info("点击 Android 键盘的返回键");
		} catch (WebDriverException e) {
			logger.error("点击 Android 键盘的返回键出现错误\n", e);
		}
	}
	
	/**
	 * 点击系统的回车按钮
	 * 
	 */
	public void pressAndroidCodeEnter() {
		try {
			androidDriver.pressKeyCode(AndroidKeyCode.ENTER);
			logger.info("点击 Android 键盘的回车键");
		} catch (WebDriverException e) {
			logger.error("点击 Android 键盘的回车键出现错误\n", e);
		}
	}
	
	/**
	 * 点击Android 键盘上的按钮
	 * 
	 * @param key Android键盘键位
	 * 
	 */
	public void pressKeyCode(int key) {
		try {
			androidDriver.pressKeyCode(key);
			logger.info("点击 AndroidKeyCode:" + key);
		} catch (WebDriverException e) {
			logger.error("点击 AndroidKeyCode" + key + " 失败\n", e);
		}
	}
	
	/**
	 * 点击Android 键盘上的按钮
	 * 
	 * @param key Android键盘键位
	 * 
	 */
	public void longPressKeyCode(int key) {
		try {
			androidDriver.longPressKeyCode(key);
			logger.info("长按 AndroidKeyCode:" + key);
		} catch (WebDriverException e) {
			logger.error("长按 the AndroidKeyCode" + key + " 失败\n", e);
		}
	}
}

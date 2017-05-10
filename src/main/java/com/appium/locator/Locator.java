package com.appium.locator;

import java.util.List;

import com.appium.element.MyElement;
import com.appium.manager.DeviceInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * 定位类，用来封装 AppiumDriver，但只是一个接口
 * 
 * @version 1.0
 * 
 */

public interface Locator<T extends MobileElement> {

	int DEFAULT_ELEMENT_WAIT_SECONDS = 15;			//元素默认等待时间，单位：s
	int DEFAULT_LONG_TAP_MILLSECONDS = 3000;		//长按默认时间，单位：ms
	int DEFAULT_SHORT_TAP_MILLSECONDS = 200;		//短按默认时间，单位：ms
	int DEFAULT_SWIPE_MILLSECONDS = 300;			//滑动操作默认时间，单位：ms
	int DEFAULT_TAP_FINGERS = 1;					//点击时，默认手指数
	
	String NATIVE_APP = "NATIVE_APP";				//context处于客户端状态

	/**
	 * 获取当前 Locator 的设备信息
	 *
	 * @return 设备信息
	 */
	DeviceInfo getDeviceInfo();

	/**
	 * 设置元素等待时间
	 * 
	 * @param seconds 元素等待秒数
	 * 
	 */
	void setWaitTime(int seconds);
	
	/**
	 * 获取元素
	 * 
	 * @param by {@link org.openqa.selenium.By}
	 * 
	 * @return 返回元素，未封装
	 * 
	 */
	T getElement(By by);
	
	/**
	 * 定位元素
	 * 
	 * @param by {@link org.openqa.selenium.By}
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocator(By by);
	
	/**
	 * 使用 AccessibilityId 方式定位单一元素
	 * 
	 * @param accessibilityId 元素定位信息
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocatorByAccessibilityId(String accessibilityId);
	
	/**
	 * 使用 className 方式定位单一元素
	 * 
	 * @param className 元素定位信息
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocatorByClassName(String className);
	
	/**
	 * 使用 Xpath 方式定位单一元素
	 * 
	 * @param xpathExpression 元素定位信息
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocatorByXpath(String xpathExpression);
	
	/**
	 * 使用 Id 方式定位单一元素
	 * 
	 * @param id 元素定位信息
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocatorById(String id);
	
	/**
	 * 使用 LinkText 方式定位单一元素
	 * 
	 * @param linkText 元素定位信息
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocatorByLinkText(String linkText);
	
	/**
	 * 使用 CssSelector 方式定位单一元素
	 * 
	 * @param selector 元素定位信息
	 * 
	 * @return 返回元素
	 * 
	 */
	MyElement getLocatorByCssSelector(String selector);
	
	/**
	 * 定位元素集合
	 * 
	 * @param by {@link org.openqa.selenium.By}
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocators(By by);
	
	/**
	 * 使用 AccessibilityId 方式定位元素集合
	 * 
	 * @param accessibilityId 元素定位信息
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocatorsByAccessibilityId(String accessibilityId);
	
	/**
	 * 使用 className 方式定位元素集合
	 * 
	 * @param className 元素定位信息
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocatorsByClassName(String className);
	
	/**
	 * 使用 Xpath 方式定位元素集合
	 * 
	 * @param xpathExpression 元素定位信息
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocatorsByXpath(String xpathExpression);
	
	/**
	 * 使用 Id 方式定位元素集合
	 * 
	 * @param id 元素定位信息
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocatorsById(String id);
	
	/**
	 * 使用 LinkText 方式定位元素集合
	 * 
	 * @param linkText 元素定位信息
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocatorsByLinkText(String linkText);
	
	/**
	 * 使用 CssSelector 方式定位元素集合
	 * 
	 * @param selector 元素定位信息
	 * 
	 * @return 返回元素集合
	 * 
	 */
	List<MyElement> getLocatorsByCssSelector(String selector);
	
	/**
	 * 获取 driver，使用未封装的方法
	 * 
	 * @return AppiumDriver {@link io.appium.java_client.AppiumDriver}
	 * 
	 */
	AppiumDriver<T> getDriver();
	
	/**
	 * 测试 H5页面时，填写需要测试的网址
	 * 
	 * @param url 需要访问的地址
	 * 
	 */
	void get(String url);
	
	/**
	 * 等待元素出现的时间
	 * 
	 * @param seconds 元素等待的秒数
	 * 
	 */
	void waitElement(int seconds);
	
	/**
	 * 退出 driver
	 * 
	 */
	void quit();
	
	/**
	 * 获得当前页面的元素
	 * 
	 * @return 当前页面的元素信息，xml 格式
	 * 
	 */
	String getPageSource();
	
	/**
	 * 坐标点击
	 * 
	 * @param x X坐标
	 * @param y Y坐标
	 *
	 */
	void tap(int x, int y);

	/**
	 * 坐标滑动
	 * 
	 * @param startX 滑动开始 X 坐标
	 * @param startY 滑动开始 Y 坐标
	 * @param endX 滑动结束 X 坐标
	 * @param endY 滑动结束 Y 坐标
	 * @param duration 滑动操作时间，ms
	 * 
	 */
	void swipe(int startX, int startY, int endX, int endY, int duration);
	
	/**
	 * 判断元素是否存在
	 * 
	 * @param by {@link org.openqa.selenium.By}
	 * 
	 * @return true or false
	 * 
	 */
	boolean isElementExist(By by);
	
	/**
	 * 获取当前测试设备的屏幕大小
	 * 
	 * @return 屏幕分辨率(x, y)
	 * 
	 */
	Dimension getWindowSize();
	
	/**
	 * 获取设备的时间
	 *  
	 * @return 设备的时间
	 *  
	 */
	String getDeviceTime();
	
	/**
	 * 获取当前的Context状态
	 * 
	 * @return 客户端现在的Context状态
	 * 
	 */
	String getContext();
	
	/**
	 * 获取客户端的所有Context状态
	 * 
	 * @return 客户端的所有Context状态
	 * 
	 */
	List<String> getContextHandles();
	
	/**
	 * 连接到指定的 context 状态
	 * 
	 * @param text 客户端本地货页面类形，如：NATIVE_APP or WEBVIEW
	 * 
	 * @return Locator {@link com.appium.locator.Locator}
	 * 
	 */
	Locator<T> context(String text);

}

package com.appium.locator;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.appium.element.MyElement;
import com.appium.manager.ios.IOSDeviceInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;

import com.appium.element.Swipes;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

/**
 * iOS 的定位类，用来封装  IOSDriver，直接调用
 * 
 * @version 1.0
 * 
 */

public class IOSLocator<T extends MobileElement> extends AppLocator<T> {
	
	private IOSDriver<T> iosDriver;

	/**
	 * 构造函数，使用给定的 iosdriver，使用默认的元素等待时间
	 * 
	 * @param iosDriver {@link io.appium.java_client.ios.IOSDriver}
	 * 
	 */
	public IOSLocator(IOSDriver<T> iosDriver) {
		super(iosDriver);
		this.iosDriver = iosDriver;
	}

	/**
	 * 构造函数，使用给定的 iosdriver，给定的元素等待时间
	 * 
	 * @param  iosDriver {@link io.appium.java_client.ios.IOSDriver}
	 * @param element_wait_seconds 元素等待秒数
	 * 
	 */
	public IOSLocator(IOSDriver<T> iosDriver, int element_wait_seconds) {
		super(iosDriver, element_wait_seconds);
		this.iosDriver = iosDriver;
	}
	
	/**
	 * 获取 IOSLocator 的 driver，以便使用未封装的方法。
	 * 
	 */
	public IOSDriver<T> getDriver() {
		return iosDriver;
	}
	
	public IOSDeviceInfo getDeviceInfo() {
		return new IOSDeviceInfo(iosDriver.getCapabilities());
	}

	/**
	 * 封装 AppLocator.getLocator 方法，使用 IosUIAutomation 方式，查找单一元素
	 * 
	 * @param iOSAutomationText iOSAutomation的定位信息
	 * 
	 * @return MyElement {@link MyElement}
	 * 
	 */
	public MyElement getLocatorByIosUIAutomation(String iOSAutomationText) {
		return super.getLocator(MobileBy.IosUIAutomation(iOSAutomationText));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 IosUIAutomation 方式，查找元素集合
	 * 
	 * @param iOSAutomationText iOSAutomation的定位信息
	 * 
	 * @return MyElements {@link MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByIosUIAutomation(String iOSAutomationText) {
		return super.getLocators(MobileBy.IosUIAutomation(iOSAutomationText));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 iOSNsPredicate 方式，查找单一元素
	 * 
	 * @param iOSNsPredicateString iOSNsPredicate的谓词信息
	 * 
	 * @return MyElement {@link MyElement}
	 * 
	 */
	public MyElement getLocatorByIOSNsPredicate(String iOSNsPredicateString) {
		return super.getLocator(MobileBy.iOSNsPredicateString(iOSNsPredicateString));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 iOSNsPredicate 方式，查找元素集合
	 * 
	 * @param iOSNsPredicateString iOSNsPredicate的谓词信息
	 * 
	 * @return MyElement {@link MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByIOSNsPredicate(String iOSNsPredicateString) {
		return super.getLocators(MobileBy.iOSNsPredicateString(iOSNsPredicateString));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 iOSClassChain 方式，查找单一元素
	 * 
	 * @param iOSClassChainString iOSClassChain的谓词信息
	 * 
	 * @return MyElement {@link MyElement}
	 * 
	 */
	public MyElement getLocatorByIOSClassChain(String iOSClassChainString) {
		return super.getLocator(MobileBy.iOSClassChain(iOSClassChainString));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 iOSClassChain 方式，查找元素集合
	 * 
	 * @param iOSClassChainString iOSClassChain的谓词信息
	 * 
	 * @return MyElements {@link MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByIOSClassChain(String iOSClassChainString) {
		return super.getLocators(MobileBy.iOSClassChain(iOSClassChainString));
	}

	/**
	 * 封装 appium 中 iOS 的 lockDevice 方法
	 * 
	 * @param seconds 锁屏的秒数
	 * 
	 */
	public void lockDevice(int seconds) {
		try {
			iosDriver.lockDevice(Duration.ofSeconds(seconds));
			logger.info("锁定设备 " + seconds + " 秒");
		} catch (WebDriverException e) {
			logger.error("锁定设备发生错误\n", e);
		}
	}
	
	/**
	 * iOS 的 Swipe 方法与安卓不一致，需覆盖方法
	 * 
	 * @param startX 滑动开始 X 坐标
	 * @param startY 滑动开始 Y 坐标
	 * @param endX 滑动结束 X 坐标
	 * @param endY 滑动结束 Y 坐标
	 * @param duration 滑动操作时间，ms
	 * 
	 */
	@Override
	public void swipe(int startX, int startY, int endX, int endY, int duration) {
		super.swipe(startX, startY, endX - startX, endY - startY, duration);
	}
	
	/**
	 * 摇动设备，只针对 iOS 设备
	 * 
	 */
	public void shake() {
		try {
			iosDriver.shake();
			logger.info("摇动设备成功");
		} catch (WebDriverException e) {
			logger.error("摇动设备发生错误\n", e);
		}
	}
	
	/**
	 * 在信息警告弹窗中，点击接受按钮
	 * 
	 */
	public void alert_accept_click() {
		try {
			iosDriver.switchTo().alert().accept();
			logger.info("点击 iOS 权限弹窗的 \"允许\" 按钮");
		} catch (WebDriverException e) {
			logger.error("点击 iOS 权限弹窗的 \"允许\" 按钮发生错误\n", e);
		}
	}
	
	/**
	 * 在信息警告弹窗中，点击拒绝按钮
	 * 
	 */
	public void alert_dismiss_click() {
		try {
			iosDriver.switchTo().alert().dismiss();
			logger.info("点击 iOS 权限弹窗的 \"不允许\" 按钮");
		} catch (WebDriverException e) {
			logger.error("点击 iOS 权限弹窗的 \"不允许\" 按钮发生错误\n", e);
		}
	}
	
	
	/**
	 * 寻找指定的元素控件，并将其显示到屏幕中
	 * 
	 * @param by {@link org.openqa.selenium.By}
	 * 
	 */
	public void scrollTo(By by) {
		try {
			boolean isExist = isElementExist(by);
			//元素存在才查找位置
			if (isExist) {
				Swipes swipes = new Swipes(this);
				int windowsHeight = getWindowSize().height;
				Point elementCenter;
				int elementCenterY;
				do {
					elementCenter = iosDriver.findElement(by).getCenter();
					elementCenterY = elementCenter.y;
					if ( elementCenterY == 0 || elementCenterY >= windowsHeight ) {
						//当需要查找的元素位置在屏幕下方，且在之前的滑动操作中未出现，需要往上滑动，将其显示到屏幕中
						swipes.swipeToUp();
					} else if ( elementCenterY < 0 ) {
						//当需要查找的元素位置在屏幕上方，且在之前滑动操作中出现过，需往下滑动，将其显示在屏幕中
						swipes.swipeToDown();
					} else {
						//在屏幕中，退出查找循环
						break;
					}
					//当元素位置在屏幕中，可以退出循环
				} while ( !(elementCenterY > 0 && elementCenter.y < windowsHeight) );
				logger.info("滑动到元素 [" + by.toString() + "] 成功");
			} else {
				logger.info("元素 [" + by.toString() + "] 不存在，不能滑动");
			}
		} catch (WebDriverException e) {
			logger.error("滑动到元素 [" + by.toString() + "] 发生错误\n", e);
		}
	}
	
	/**
	 * 将指定文本的元素，滑动到屏幕中
	 * 
	 * @param text 元素的文本
	 * 
	 */
	public void scrollTo(String text) {
		this.scrollTo(MobileBy.AccessibilityId(text));
	}
	
	/**
	 * 默认系统语言对应的Strings.xml文件内的数据。iOS专用
	 * 
	 * @return map
	 * 
	 */
	public Map<String, String> getAppStringMap() {
		Map<String, String> appStringMap = null;
		try {
			appStringMap = iosDriver.getAppStringMap();
			logger.info("获取 appStringMap:[\"" + appStringMap + "\"]");
		} catch (WebDriverException e) {
			logger.error("获取 appStringMap 发生错误\n", e);
		}
		return appStringMap;
	}
}

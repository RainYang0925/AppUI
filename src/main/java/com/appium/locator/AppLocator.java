package com.appium.locator;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.appium.element.MyElement;
import com.framework.utils.SleepUtils;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.appium.keyword.Engine_Excel;
import com.appium.manager.DeviceInfo;
import com.appium.service.AppiumDriverWait;
import com.framework.utils.FileUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;

/**
 * 定位类，用来封装 AppiumDriver，替代 driver 使用，
 * 但不直接调用，只会调用子类 IOSLocator 和 AndroidLocator
 * 
 * @version 1.1
 * 
 */

public class AppLocator<T extends MobileElement> implements Locator<T> {

	protected int element_wait_seconds = DEFAULT_ELEMENT_WAIT_SECONDS;
	
	protected Logger logger = LogManager.getLogger(this.getClass());
	private AppiumDriver<T> driver;
	private AppiumDriverLocalService service;
	protected MyElement myElement;
	protected TouchAction touch;

	/**
	 * 构造函数，设置 appium driver
	 * 
	 * @param appiumDriver AppiumDriver{@link io.appium.java_client.AppiumDriver}
	 * 
	 */
	public AppLocator(AppiumDriver<T> appiumDriver) {
		this.driver = appiumDriver;
		this.element_wait_seconds = DEFAULT_ELEMENT_WAIT_SECONDS;
		touch = new TouchAction(driver);
	}

	/**
	 * 构造函数，设置 appium driver, 和元素等待时间
	 * 
	 * @param appiumDriver AppiumDriver{@link io.appium.java_client.AppiumDriver}
	 * @param element_wait_seconds 元素等待秒数
	 * 
	 */
	public AppLocator(AppiumDriver<T> appiumDriver, int element_wait_seconds) {
		logger.info("driver: " + appiumDriver.toString());
		this.driver = appiumDriver;
		this.element_wait_seconds = element_wait_seconds;
		touch = new TouchAction(driver);
	}

	/**
	 * 获取当前 Locator 的设备信息
	 * 
	 * @return 设备信息
	 */
	@Override
	public DeviceInfo getDeviceInfo() {
		return new DeviceInfo() {
			@Override
			public String getName() {
				return (String) driver.getCapabilities().getCapability(MobileCapabilityType.DEVICE_NAME);
			}

			@Override
			public String getDeviceID() {
				return (String) driver.getCapabilities().getCapability(MobileCapabilityType.UDID);
			}

			@Override
			public String getSystem() {
				return (String) driver.getCapabilities().getCapability(MobileCapabilityType.PLATFORM_NAME);
			}

			@Override
			public String getVersion() {
				return (String) driver.getCapabilities().getCapability(MobileCapabilityType.PLATFORM_VERSION);
			}

			@Override
			public String getAutomationName() {
				return (String) driver.getCapabilities().getCapability(MobileCapabilityType.AUTOMATION_NAME);
			}
		};
	}

	/**
	 * 设置当前元素
	 * 
	 * @param myElement MyElement{@link MyElement}
	 * 
	 */
	public void setElement(MyElement myElement) {
		logger.info("setElemnt: " + myElement.toString());
		this.myElement = myElement;
	}
	
	/**
	 * 设置元素等待时间
	 * 
	 * @param seconds 元素等待秒数
	 * 
	 */
	public void setWaitTime(int seconds) {
		this.element_wait_seconds = seconds;
		logger.info("元素的等待秒数为： " + seconds);
	}
	
	/**
	 * 查找指定元素是否存在
	 * 
	 * @param by By{@link org.openqa.selenium.By}
	 * 
	 * return MobileElement {@link io.appium.java_client.MobileElement}
	 * 
	 */
	public T getElement(final By by) {
		T element = null;
		try {
			element = (new AppiumDriverWait(driver, element_wait_seconds))
					.until(new ExpectedCondition<T>() {
						public T apply(WebDriver driver) {
							@SuppressWarnings("unchecked")
							T element = (T) driver.findElement(by);
							if (element.isDisplayed() == true) {
								return element;
							} else {
								return null;
							}
						}
					});
			logger.info("找到此元素:" + element.toString());
		} catch (WebDriverException e) {
			logger.error("找不到此元素:" + by.toString() + "\n", e);
			Engine_Excel.bResult = false;
		}
		return element;
	}

	/**
	 * 封装元素定位的方法
	 * 
	 * @param by By{@link org.openqa.selenium.By}
	 * 
	 * @return MyElement {@link MyElement}
	 * 
	 */
	public MyElement getLocator(final By by) {
		T element = (T)this.getElement(by);
		myElement = new MyElement();
		myElement.setElement(element);
		myElement.setAppLocator(this);
		return myElement;
	}

	/**
	 * 封装元素定位的方法
	 * 
	 * @param by By{@link org.openqa.selenium.By}
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 */
	public List<MyElement> getLocators(final By by) {
		List<MyElement> myElements = new ArrayList<MyElement>();
		this.waitElement(element_wait_seconds);
		List<T> elements = driver.findElements(by);
		for (T element : elements) {
			if (element.isDisplayed()) {
				MyElement myElement = new MyElement();
				myElement.setElement(element);
				myElement.setAppLocator(this);
				myElements.add(myElement);
			}
		}
		if (myElements.size() > 0) {
			logger.info("[" + by.toString() + "] 找到 " + myElements.size() + " 个元素");
		} else {
			logger.info("[" + by.toString() + "] 找不到元素");
		}
		return myElements;
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 AccessibilityId 方式，查找单一元素
	 * 
	 * @param accessibilityId 元素定位信息
	 * 
	 * @return MyElement {@link com.appium.element.MyElement}
	 * 
	 */
	public MyElement getLocatorByAccessibilityId(String accessibilityId) {
		return this.getLocator(MobileBy.AccessibilityId(accessibilityId));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 className 方式，查找单一元素
	 * 
	 * @param className 元素定位信息
	 * 
	 * @return MyElement {@link com.appium.element.MyElement}
	 * 
	 */
	public MyElement getLocatorByClassName(String className) {
		return this.getLocator(MobileBy.className(className));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 xpath 方式，查找单一元素
	 * 
	 * @param xpathExpression 元素定位信息
	 * 
	 * @return MyElement {@link com.appium.element.MyElement}
	 * 
	 */
	public MyElement getLocatorByXpath(String xpathExpression) {
		return this.getLocator(MobileBy.xpath(xpathExpression));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 id 方式，查找单一元素
	 * 
	 * @param id 元素定位信息
	 * 
	 * @return MyElement {@link com.appium.element.MyElement}
	 * 
	 */
	public MyElement getLocatorById(String id) {
		return this.getLocator(MobileBy.id(id));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 linkText 方式，查找单一元素
	 * 
	 * @param linkText 元素定位信息
	 * 
	 * @return MyElement {@link com.appium.element.MyElement}
	 * 
	 */
	public MyElement getLocatorByLinkText(String linkText) {
		return this.getLocator(MobileBy.linkText(linkText));
	}
	
	/**
	 * 封装 AppLocator.getLocator 方法，使用 cssSelector 方式，查找单一元素
	 * 
	 * @param selector 元素定位信息
	 * 
	 * @return MyElement {@link com.appium.element.MyElement}
	 * 
	 */
	public MyElement getLocatorByCssSelector(String selector) {
		return this.getLocator(MobileBy.cssSelector(selector));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 AccessibilityId 方式，查找元素集合
	 * 
	 * @param accessibilityId 元素定位信息
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByAccessibilityId(String accessibilityId) {
		return this.getLocators(MobileBy.AccessibilityId(accessibilityId));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 className 方式，查找元素集合
	 * 
	 * @param className 元素定位信息
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByClassName(String className) {
		return this.getLocators(MobileBy.className(className));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 xpath 方式，查找元素集合
	 * 
	 * @param xpathExpression 元素定位信息
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByXpath(String xpathExpression) {
		return this.getLocators(MobileBy.xpath(xpathExpression));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 id 方式，查找元素集合
	 * 
	 * @param id 元素定位信息
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsById(String id) {
		return this.getLocators(MobileBy.id(id));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 linkText 方式，查找元素集合
	 * 
	 * @param linkText 元素定位信息
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByLinkText(String linkText) {
		return this.getLocators(MobileBy.linkText(linkText));
	}
	
	/**
	 * 封装 AppLocator.getLocators 方法，使用 cssSelector 方式，查找元素集合
	 * 
	 * @param selector 元素定位信息
	 * 
	 * @return MyElements {@link com.appium.element.MyElement}
	 * 
	 */
	public List<MyElement> getLocatorsByCssSelector(String selector) {
		return this.getLocators(MobileBy.cssSelector(selector));
	}
	
	/**
	 * 等待元素出现的时间
	 * 
	 * @param seconds 秒数
	 * 
	 */
	public void waitElement(int seconds) {
		try {
			logger.info("等待元素出现秒数：" + seconds);
			driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		} catch (WebDriverException e) {
			logger.error("等待元素时，发生错误\n", e);
		}
	}

	public void setAppiumDriverLocalService(AppiumDriverLocalService service) {
		this.service = service;
	}

	/**
	 * 封装 Selenium 的 quit 方法
	 * 
	 */
	public void quit() {
		try {
			if (DriverMethods.isNotEmpty(driver)) {
				logger.info("退出 driver: " + driver.toString());
				driver.quit();
				//如果使用关键字驱动测试的话，才会输出测试报告
				Engine_Excel.getExtentReports().flush();
			}
			service.stop();
		} catch (WebDriverException e) {
			logger.error("退出 driver：" + driver.toString() + " 发生错误\n", e);
		}
	}
	
	/**
	 * 获取 AppLocator 的 driver，以便使用未封装的方法。
	 * 
	 */
	public AppiumDriver<T> getDriver() {
		return driver;
	}
	
	/**
	 * 封装 Selenium 的 getPageSource 方法，打印当前页面所有的元素
	 * 
	 * @return 页面元素信息
	 * 
	 */
	public String getPageSource() {
		String pageSource = null;
		try {
			SleepUtils.threadSleepBySeconds(2);
			pageSource = driver.getPageSource();
			logger.info("输出整个页面元素信息成功");
			return pageSource;
		} catch (WebDriverException e) {
			logger.error("输出整个页面元素信息发生错误\n", e);
			return null;
		}
	}

	/**
	 * 测试 H5页面时，填写需要测试的网址
	 * 
	 * @param url 网址
	 * 
	 */
	public void get(String url) {
		try {
			driver.get(url);
			logger.info("进入网址：" + url);
		} catch (WebDriverException e) {
			logger.error("进入网址发生错误\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的点按坐标的 tap 方法
	 * 
	 * @param x X坐标
	 * @param y Y坐标
	 *
	 */
	public void tap(int x, int y) {
		try {
			touch.press(x, y).release().perform();
			logger.info("点击坐标 (" + x + ", " + y + ")");
		} catch (WebDriverException e) {
			logger.error("点击坐标 (" + x + ", " + y + ")，发生错误\n", e);
		}
	}

	/**
	 * 封装 Appium 的点按坐标的 tap 方法，加入3秒的时间，作为长按
	 * 
	 * @param x X坐标
	 * @param y Y坐标
	 * 
	 */
	public void longTap(int x, int y) {
		touch.longPress(x, y).release().perform();
	}
	

	/**
	 * 封装 Appium 的点按元素的 tap 方法，点击元素的中心点
	 * 
	 * @param element MyElement{@link MyElement}
	 *
	 */
	public void tap(MyElement element) {
			try {
			touch.press(element.getElement());
			logger.info("点击元素 (" + element.toString() + ")");
		} catch (WebDriverException e) {
			logger.error("点击元素 (" + element.toString() + ")，发生错误\n", e);
		}
	}

	/**
	 * 封装 Appium 的点按元素的 tap 方法，加入3秒的时间，作为长按
	 * 
	 * @param element MyElement{@link MyElement}
	 * 
	 */
	public void longTap(MyElement element) {
		touch.longPress(element.getElement()).release().perform();
	}

	/**
	 * 简单封装 appium 的 swipe 方法，但注意 Android 和 iOS 参数不一样，需分开
	 * 
	 * @param startX 滑动开始 X 坐标
	 * @param startY 滑动开始 Y 坐标
	 * @param endX 滑动结束 X 坐标
	 * @param endY 滑动结束 Y 坐标
	 * 
	 */
	public void swipe(int startX, int startY, int endX, int endY) {
		swipe(startX, startY, endX, endY, DEFAULT_SWIPE_MILLSECONDS);
	}
	
	/**
	 * 简单封装 appium 的 swipe 方法，但注意 Android 和 iOS 参数不一样，需分开
	 * 
	 * @param startX 滑动开始 X 坐标
	 * @param startY 滑动开始 Y 坐标
	 * @param endX 滑动结束 X 坐标
	 * @param endY 滑动结束 Y 坐标
	 * @param duration 滑动操作时间，ms
	 * 
	 */
	public void swipe(int startX, int startY, int endX, int endY, int duration) {
		try {
			TouchAction touch = new TouchAction(driver);
			touch.press(startX, startY).waitAction(Duration.ofMillis(duration)).moveTo(endX, endY).release().perform();
			logger.info("从 (" + startX + ", " + startY + ") 滑动到 (" + endX + ", " + endY + ") 成功，时间 (ms):" + duration);
		} catch (WebDriverException e) {
			logger.error("从 (" + startX + ", " + startY + ") 滑动到 (" + endX + ", " + endY + ") 发生错误，时间 (ms):" + duration + "\n", e);
		}
	}
	
	/**
	 * 判断指定的元素是否存在
	 * 
	 * @param by {@link org.openqa.selenium.By}
	 * 
	 * @return true or false
	 * 
	 */
	public boolean isElementExist(By by) {
		logger.info("判断 [" + by.toString() + "] 元素是否存在");
		boolean isElementExist = false;
		try {
			this.waitElement(element_wait_seconds);
			T element = this.driver.findElement(by);
			//如果存在该元素，且显示在该页面，则为找到该元素
			if (element != null && element.isDisplayed()) {
				isElementExist = true;
				logger.info(element.toString() + " 元素存在");
			}
		} catch (WebDriverException e) {
			logger.warn("\"" + by.toString() + "\" 元素不存在");
			isElementExist = false;
		}
		return isElementExist;
	}
	
	/**
	 * 获取设备屏幕的大小
	 * 
	 * @return 屏幕分辨率的大小（x, y）
	 * 
	 */
	public Dimension getWindowSize() {
		Dimension windowSize = null;
		try {
			windowSize = driver.manage().window().getSize();
			logger.info("获取设备的屏幕大小: " + windowSize);
		} catch (WebDriverException e) {
			logger.error("获取设备的屏幕大小发生错误\n", e);
		}
		return windowSize;
	}
	
	/**
	 * 获取设备的时间
	 * 
	 * @return 时间的字符串
	 * 
	 */
	public String getDeviceTime() {
		String time = null;
		try {
			time = driver.getDeviceTime();
			logger.info("设备时间: " + time);
		} catch (WebDriverException e) {
			logger.error("设备时间发生错误\n", e);
		}
		return time;
	}
	
	/**
	 * 判断应用是否已经安装
	 * 
	 * @param bundleId 应用包名
	 * 
	 * @return true, false
	 * 
	 */
	public boolean isAppInstalled(String bundleId) {
		boolean isInstalled = false;
		try {
			isInstalled = driver.isAppInstalled(bundleId);
			logger.info("获取应用 [" + bundleId + "] 是否已安装："+ isInstalled);
		} catch (WebDriverException e) {
			logger.error("获取应用 [" + bundleId + "] 是否已安装发生错误\n", e);
		}
		return isInstalled;
	}
	
	/**
	 * 安装指定路径的应用
	 * 
	 * @param appPath app的绝对路径
	 * 
	 */
	public void installApp(String appPath) {
		try {
			driver.installApp(appPath);
			logger.info("安装应用成功, 路径: " + appPath);
		} catch (WebDriverException e) {
			logger.error("安装应用发生错误, 路径: " + appPath + "\n", e);
		}
	}
	
	/**
	 * 移除指定包名的应用
	 * 
	 * @param bundleId 应用包名
	 * 
	 */
	public void removeApp(String bundleId) {
		try {
			driver.removeApp(bundleId);
			logger.info("卸载应用 [" + bundleId + "] 成功");
		} catch (WebDriverException e) {
			logger.error("卸载应用 [" + bundleId + " 发生错误\n", e);
		}
	}
	
	/**
	 * 启动被测应用
	 * 
	 */
	public void launchApp() {
		try {
			driver.launchApp();
			logger.info("启动应用成功");
		} catch (WebDriverException e) {
			logger.error("启动应用发生错误\n", e);
		}
	}
	
	/**
	 * 关闭被测应用
	 * 
	 */
	public void closeApp() {
		try {
			driver.closeApp();
			logger.info("关闭应用成功");
		} catch (WebDriverException e) {
			logger.error("关闭应用发生错误\n", e);
		}
	}

	/**
	 * 将测试应用后台运行，达到指定秒数后，自动重新启动
	 *
	 * @param seconds 客户端后台的秒数
	 *
	 */
	public void runAppInBackground(int seconds) {
		try {
			driver.runAppInBackground(Duration.ofSeconds(seconds));
			logger.info("后台运行应用成功");
		} catch (WebDriverException e) {
			logger.error("后台运行应用发生错误\n", e);
		}
	}

	/**
	 * 重置被测应用
	 * 1.Android 是直接清除数据；
	 * 2.iOS 是先卸载再重新安装
	 * 
	 */
	public void resetApp() {
		try {
			driver.resetApp();
			logger.info("重启应用成功");
		} catch (WebDriverException e) {
			logger.error("重启应用发生错误\n",e);
		}
	}
	
	/**
	 * 获取当前的Context状态，有 NATIVE_APP 和 WEBVIEW_packageName 两种；
	 * 		测客户端中的 H5 页面时使用此方法
	 * 
	 * @return context NATIVE_APP or WEBVIEW_packageName
	 * 
	 */
	public String getContext() {
		String context = null;
		try {
			context = driver.getContext();
			logger.info("获取 context:[\"" + context + "\"]");
		} catch (WebDriverException e) {
			logger.error("获取 context 发生错误\n", e);
		}
		return context;
	}

	/**
	 * 获取当前Context的所有状态，有 NATIVE_APP 和 WEBVIEW_packageName 两种；
	 * 		测客户端中的 H5 页面时使用此方法
	 * 
	 * @return contextHandles，a set string
	 * 
	 */
	public List<String> getContextHandles() {
		ArrayList<String> contexts = new ArrayList<String>();
		try {
			String logInfo = "可以使用的 contexts: [\"";
			Iterator<String> it = driver.getContextHandles().iterator();
			while (it.hasNext()) {
				String contextHandle = (String) it.next();
				contexts.add(contextHandle);
				logInfo =logInfo + contextHandle + "\"";
				if (it.hasNext()) {
					logInfo = logInfo + ", \"";
				} else {
					logInfo = logInfo + "]";
				}
			}
			logger.info(logInfo);
		} catch (WebDriverException e) {
			logger.error("获取 contextHandles 发生错误\n", e);
		}
		return contexts;
	}
	
	/**
	 * 连接到指定的 context 状态，有 NATIVE_APP 和 WEBVIEW_packageName 两种；
	 * 		测客户端中的 H5 页面时使用次方法
	 * 
	 * @param text Context文本
	 * 
	 * @return AppLocator {@link com.appium.locator.AppLocator}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public AppLocator<T> context(String text) {
		AppiumDriver<T> wd = null;
		try {
			wd = (AppiumDriver<T>)driver.context(text);
			logger.info("连接到 context:[\"" + text + "\"]");
		} catch (WebDriverException e) {
			logger.error("连接到 context:[\"" + text + "\"] 发生错误\n", e);
		}
		return new AppLocator<T>(wd);
	}
	
	/**
	 * 将客户端的 context 状态切换到 webview 状态
	 * 
	 */
	public  void toWebView() {
		List<String> contexts = this.getContextHandles();
		if (contexts.size() > 1 && this.getContext().equalsIgnoreCase(Locator.NATIVE_APP)) {
			this.context(contexts.get(1));
		}
	}

	/**
	 * 获取此次 session 的所有信息
	 * 
	 * @return a map of session
	 * 
	 */
	public Map<String, Object> getSessionDetails() {
		Map<String, Object> sessionDetails = null;
		try {
			sessionDetails = driver.getSessionDetails();
			logger.info("the session details:[" + sessionDetails + "]");
		} catch (WebDriverException e) {
			logger.error("get the session details failed. \n", e);
		}
		return sessionDetails;
	}
	
	/**
	 * 对当前设备的操作界面进行截图，使用指定的路径
	 * 
	 * @param screenshotFilePath 截图的路径，包括文件名
	 * 
	 */
	public void getScreenShot(String screenshotFilePath) {
		logger.info("截图保存路径：" + screenshotFilePath);
	    try {
	    	//调用 Selenium 的 API进行截图
	    	File srcfile = driver.getScreenshotAs(OutputType.FILE);
	    	//先创建一个空白文件
	    	FileUtils.createFile(screenshotFilePath);
	    	//将截图产生的文件，复制到指定路径中
	    	FileUtils.copyFile(srcfile, new File(screenshotFilePath));
	    	logger.info("保存屏幕截图成功。");
	    } catch (IOException e) {
	    	logger.error("保存屏幕截图失败\n", e);
	    }
	}
}

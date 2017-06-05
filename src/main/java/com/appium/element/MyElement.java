package com.appium.element;

import com.appium.keyword.Engine_Excel;
import com.appium.locator.AppLocator;
import com.appium.locator.Locator;
import com.framework.utils.SleepUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装有关元素的操作方法
 * 
 * @version 1.1
 * 
 */

public class MyElement {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	private MobileElement element = null;
	private AppLocator<? extends MobileElement> locator;

	/**
	 * 设置需要操作的 element
	 * 
	 * @param element 元素
	 * 
	 */
	public void setElement(MobileElement element) {
		this.element = element;
	}

	/**
	 * 设置定位器 AppLocator
	 *
	 * @param locator 定位器
	 */
	public void setAppLocator(AppLocator<? extends MobileElement> locator) {
		this.locator = locator;
	}

	/**
	 * 获得需要操作的元素
	 *
	 * @return MobileElement {@link io.appium.java_client.MobileElement}
	 */
	public MobileElement getElement() {
		return element;
	}

	/**
	 * 返回elements的字符串形式
	 * 
	 * @return element 字符串
	 */
	public String toString() {
		if (element != null) {
			return element.toString();
		} else {
			Engine_Excel.bResult = false;
			return null;
		}
	}

	/**
	 * 元素中查找子元素的
	 *
	 * @param by By{@link org.openqa.selenium.By}
	 *
	 * @return MyElement {@link MyElement}
	 */
	public MyElement getLocator(final By by) {
		MobileElement elementinElement = element.findElement(by);
		MyElement myElement = new MyElement();
		myElement.setElement(elementinElement);
		myElement.setAppLocator(locator);
		return myElement;
	}

	/**
	 * 元素中查找元素集合
	 *
	 * @param by By{@link org.openqa.selenium.By}
	 *
	 * @return MyElements {@link com.appium.element.MyElement}
	 */
	public List<MyElement> getLocators(final By by) {
		List<MyElement> myElements = new ArrayList<>();
		List<MobileElement> elements = element.findElements(by);
		for (MobileElement inElement : elements) {
			if (inElement.isDisplayed()) {
				MyElement myElement;
				myElement = new MyElement();
				myElement.setElement(inElement);
				myElement.setAppLocator(locator);
				myElements.add(myElement);
			}
		}
		return myElements;
	}

	/**
	 * 判断 元素是否显示
	 *
	 * @return true or false
	 */
	public boolean isDisplayed() {
		boolean result;
		try {
			result = element.isDisplayed();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 判断元素是否已选择
	 *
	 * @return true or false
	 */
	public boolean isSelected() {
		boolean result;
		try {
			result = element.isSelected();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public boolean isEnabled() {
		boolean result;
		try {
			result = element.isEnabled();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 封装 Appium 的 click 方法
	 * 
	 */
	public void click() {
		try {
			element.click();
			logger.info(this.toString() + " 进行点击操作");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 点击时，发生错误\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的文本框 clear 方法
	 * 
	 */
	public void clear() {
		try {
			element.clear();
			logger.info(this.toString() + " 清除文本框文字");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 清除文本框文字时，发生错误\n", e);
		}
	}
	
	/**
	 * 封装 Selenium 的文本框 sendKeys 方法
	 * 
	 * @param text 输入内容
	 * 
	 */
	public void sendKeys(String text) {
		try {
			clear();
			element.sendKeys(text);
			logger.info(this.toString() + " 输入文本： " + text);
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 输入文本时，发生错误. 文本：" + text + "\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的文本框 setValue 方法
	 * 
	 * @param text 输入内容
	 * 
	 */
	public void setValue(String text) {
		try {
			clear();
			element.setValue(text);
			logger.info(this.toString() + " 输入文本：" + text);
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 输入文本时，发生错误，文本:" + text + "\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的点按元素的 tap 方法，加入100毫秒的时间，作为短按
	 * 
	 */
	public void shortTap() {
		try {
			tap(Locator.DEFAULT_SHORT_TAP_MILLSECONDS);
			logger.info(this.toString() + " 短按");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 短按时，发生错误\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的点按元素的 tap 方法，加入3秒的时间，作为长按
	 * 
	 */
	public void longTap() {
		try {
			tap(Locator.DEFAULT_LONG_TAP_MILLSECONDS);
			logger.info(this.toString() + " 长按");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 长按时，发生错误\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的点按元素的 tap 方法，加入双击
	 * 
	 */
	public void doubleTap() {
		try {
			tap(Locator.DEFAULT_SHORT_TAP_MILLSECONDS);
			SleepUtils.threadSleepByMillis(100);
			tap(Locator.DEFAULT_SHORT_TAP_MILLSECONDS);
			logger.info(this.toString() + " 双击");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 双击时，发生错误\n", e);
		}
	}
	
	/**
	 * 封装 Appium 的点按元素的 tap 方法
	 * 
	 * @param duration 点按的时间（单位：ms）
	 * 
	 */
	public void tap(int duration) {
		try {
			TouchAction touchAction = new TouchAction(locator.getDriver());
			touchAction.tap(element).waitAction(Duration.ofSeconds(duration)).release().perform();
			logger.info("点按 " + toString() + ", 时间（ms）：" + duration);
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.info("点按 " + toString() + " 发生错误, 时间（ms）：" + duration +"\n", e);
		}
	}
	
	/**
	 * 封装 Selenium 的元素获取文本方法
	 * 
	 * @return 元素的文本
	 */
	public String getText() {
		String text = null;
		try {
			text = element.getText();
			logger.info(this.toString() + " 获取文本: \"" + text + "\"");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 获取文本时，发生错误\n", e);
		}
		return text;
	}
	
	/**
	 * 封装 Appium 的 getCenter() 方法，获取元素的中心位置
	 * 
	 * @return 元素的中心坐标（x，y）
	 */
	public Point getCenter() {
		Point point = null;
		try {
			point = element.getCenter();
			logger.info(this.toString() + " 获取元素中心点：" + point);
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 获取元素中心点时，发生错误\n", e);
		}
		return point;
	}
	
	/**
	 * 封装 Selenium 的 getLocation() 方法，获取元素开始位置
	 * 
	 * @return 元素开始坐标（x，y）
	 * 
	 */
	public Point getLocation() {
		Point location = null;
		try {
			location = element.getLocation();
			logger.info(this.toString() + " 获取起始位置：" + location);
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 获取起始位置时，发生错误\n", e);
		}
		return location;
	}
	
	/**
	 * 封装 Selenium 的 getSize() 方法，获取元素的大小
	 * 
	 * @return 元素的大小（x，y）
	 * 
	 */
	public Dimension getSize() {
		Dimension size = null;
		try {
			size = element.getSize();
			logger.info(this.toString() + " 获取元素大小：" + size);
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 获取元素大小时，发生错误 n", e);
		}
		return size;
	}
	
	/**
	 * 改造 Appium 的 zoom() 方法，将元素放大，使用 MultiTouchAction 实现
	 * 
	 */
	@Deprecated
	public void zoom() {
		try {
			int centerX = this.getCenter().x;
			int centerY = this.getCenter().y;
			AppiumDriver<? extends MobileElement> driver = locator.getDriver();
			MultiTouchAction multiTouch = new MultiTouchAction(driver);
			multiTouch.add(new TouchAction(driver).press(centerX, centerY).waitAction(300).moveTo(getLocation().x + getSize().getWidth() - 5, getLocation().y + 5).release());
			multiTouch.add(new TouchAction(driver).press(centerX, centerY).waitAction(300).moveTo(getLocation().x + 5, getLocation().y + getSize().height - 5).release());
			multiTouch.perform();

			logger.info(this.toString() + " 放大");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 缩放时，发生错误\n", e);
		}
	}
	
	/**
	 * 改造 Appium 的 pinch() 方法，将元素缩小，使用 MultiTouchAction 实现
	 * 
	 */
	@Deprecated
	public void pinch() {
		try {
			int centerX = this.getCenter().x;
			int centerY = this.getCenter().y;
			AppiumDriver<? extends MobileElement> driver = locator.getDriver();
			MultiTouchAction multiTouch = new MultiTouchAction(driver);
			multiTouch.add(new TouchAction(driver).press(getLocation().x + getSize().getWidth() - 5, getLocation().y + 5).waitAction(300).moveTo(centerX, centerY).release());
			multiTouch.add(new TouchAction(driver).press(getLocation().x + 5, getLocation().y + getSize().height - 5).waitAction(300).moveTo(centerX, centerY).release());
			multiTouch.perform();

			logger.info(this.toString() + " 缩小");
		} catch (WebDriverException e) {
			Engine_Excel.bResult = false;
			logger.error(this.toString() + " 捏时，发生错误\n", e);
		}
	}

	/**
	 * iOS 元素的滑动操作
	 *
	 * @return iOS 元素操作类
	 */
	public SwipeIOS swipeIOS() {
		return new SwipeIOS();
	}

	/**
	 * 普通元素的滑动操作
	 *
	 * @return 普通元素操作类
	 */
	public Swipe swipe() {
		return new Swipe();
	}

	public void swipe(SwipeElementDirection direction, int offset1, int offset2, int duration) {
		direction.swipe(locator, this, offset1, offset2, duration);
	}

	/**
	 * 内部类，用于测试 app 时，元素四个方向的滑动
	 * 适用：Android
	 *
	 * @version 1.1
	 *
	 */
	class Swipe implements SwipeElement {

		/**
		 * 元素向左滑动
		 *
		 */
		public void swipeElementLeft() {
			try {
				swipe(SwipeElementDirection.LEFT, 3, 3, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + " 滑动到左边");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到左边时，发生错误\n", e);
			}
		}

		/**
		 * 元素向右滑动
		 *
		 */
		public void swipeElementRight() {
			try {
				swipe(SwipeElementDirection.RIGHT, 3, 3, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + " 滑动到右边");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到右边时，发生错误\n", e);
			}
		}

		/**
		 * 元素向上滑动
		 *
		 */
		public void swipeElementUp() {
			try {
				swipe(SwipeElementDirection.UP, 3, 3, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + " 滑动到上方");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到上方时，发生错误\n", e);
			}
		}

		/**
		 * 元素向下滑动
		 *
		 */
		public void swipeElementDown() {
			try {
				swipe(SwipeElementDirection.DOWN, 3, 3, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + "  滑动到下面");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到下面时，发生错误\n", e);
			}
		}
	}

	/**
	 * 内部类，用于测试 IOS 时，元素四个方向的滑动
	 *
	 * @version 1.0
	 *
	 */
	class SwipeIOS implements SwipeElement {

		private Point location;
		private Point center;
		private Dimension size;

		public SwipeIOS() {
			location = getLocation();
			center = getCenter();
			size = getSize();
		}

		/**
		 * 元素向左滑动
		 *
		 */
		public void swipeElementLeft() {
			try {
				int startx = location.getX() + size.getWidth() - 5;
				int starty = center.getY();
				int endx = location.getX() + 5;
				int endy = starty;
				//与 ios.swipe 的 endy 减掉 starty，这里加回，不影响左滑的 y坐标
				locator.swipe(startx, starty, endx, endy + starty, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + " 滑动到左边");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到左边时，发生错误\n", e);
			}
		}

		/**
		 * 元素向右滑动
		 *
		 */
		public void swipeElementRight() {
			try {
				int startx = location.getX() + 5;
				int starty = center.getY();
				int endx = location.getX() + size.getWidth() - 5;
				int endy = starty;
				//与 ios.swipe 的 endy 减掉 starty，这里加回，不影响右滑的 y坐标
				locator.swipe(startx, starty, endx, endy + starty, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + " 滑动到右边");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到右边时，发生错误\n", e);
			}
		}

		/**
		 * 元素向上滑动
		 *
		 */
		public void swipeElementUp() {
			try {
				int startx = center.getX();
				int starty = location.getY() + size.getHeight() - 5;
				int endx = startx;
				int endy = location.getY() + 5;
				//与 ios.swipe 的 endx 减掉 startx，这里加回，不影响上滑的 x 坐标
				locator.swipe(startx, starty, endx + startx, endy, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + " 滑动到上方");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到上方时，发生错误\n", e);
			}
		}

		/**
		 * 元素向下滑动
		 *
		 */
		public void swipeElementDown() {
			try {
				int startx = center.getX();
				int starty = location.getY() + 5;
				int endx = startx;
				int endy = location.getY() + size.getHeight() - 5;
				//与 ios.swipe 的 endx 减掉 startx，这里加回，不影响下滑的 x 坐标
				locator.swipe(startx, starty, endx + startx, endy, Locator.DEFAULT_SWIPE_MILLSECONDS);
				logger.info(this.toString() + "  滑动到下面");
			} catch (WebDriverException e) {
				Engine_Excel.bResult = false;
				logger.error(this.toString() + " 滑动到下面时，发生错误\n", e);
			}
		}
	}
}

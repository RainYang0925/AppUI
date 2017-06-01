package com.appium.pageFactory;

import com.appium.locator.IOSLocator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

/**
 * Android 的 PageFactory 基类，在测试时，每个页面测试时，都需要继承此类
 * 
 * @version 1.0
 * 
 */
@Deprecated
public abstract class IOSPageFactory implements AppPageFactory {

	protected IOSLocator<MobileElement> locator;

	protected IOSPageFactory(IOSLocator<MobileElement> locator, int waitSeco) {
		this.locator = locator;
		setPageFactory(locator, waitSeco);
	}

	/**
	 * 构造函数接口
	 * 
	 * @param locator IOSLocator{@link com.appium.locator.IOSLocator}
	 * @param element_wait_time 元素等待时间，单位：s
	 * 
	 */
	private void setPageFactory(IOSLocator<MobileElement> locator, int element_wait_time) {
		PageFactory.initElements(new AppiumFieldDecorator(locator.getDriver(), element_wait_time, TimeUnit.SECONDS), this);
	}

	public String getPageSource() {
		return locator.getPageSource();
	}

}

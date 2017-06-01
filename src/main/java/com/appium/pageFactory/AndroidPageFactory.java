package com.appium.pageFactory;

import com.appium.locator.AndroidLocator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

/**
 * Android 的 PageFactory 基类，在测试时，每个页面测试时，都需要继承此类
 * 
 * @version 1.1
 * 
 */
@Deprecated
public abstract class AndroidPageFactory implements AppPageFactory {

	protected AndroidLocator<MobileElement> locator;
	
	protected AndroidPageFactory(AndroidLocator<MobileElement> locator, int waitSeco) {
		this.locator = locator;
		PageFactory.initElements(new AppiumFieldDecorator(locator.getDriver(), waitSeco, TimeUnit.SECONDS), this);
	}
	
	public String getPageSource() {
		return locator.getPageSource();
	}
}

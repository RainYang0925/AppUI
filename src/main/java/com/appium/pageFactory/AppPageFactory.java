package com.appium.pageFactory;

/**
 * PageFactory 工厂类的接口
 * 
 * @version 1.0
 * 
 */

public interface AppPageFactory {
	
	/**
	 * 获取当前页面的源代码，格式为 xml
	 * 
	 * @return 页面的元素信息，java.lang.String 类型
	 * 
	 */
	String getPageSource();
	
	/**
	 * 获取或设置当前页面的标题
	 * 
	 * @return 页面标题，java.lang.String 类型
	 * 
	 */
	String getTitle();
}

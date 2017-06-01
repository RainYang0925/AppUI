package com.appium.keyword;

import com.appium.element.MyElement;
import com.appium.keyword.data.AppElement;
import com.appium.locator.AppLocator;
import io.appium.java_client.MobileElement;

import java.util.List;

/**
 * 关键字驱动接口
 * 
 * @version 1.1
 * 
 */

public interface KeyWord {
	
	String SUITESHEET = "测试场景";
	
	/**
	 * 获取元素
	 * 
	 * @return 元素
	 * 
	 */
	AppElement getAppElement();
	
	/**
	 * 获取当前操作的元素
	 * 
	 * @return MyElement{@link com.appium.element.MyElement}
	 * 
	 */
	MyElement getElement();
	
	/**
	 * 获取当前操作的元素集合
	 * 
	 * @return MyElements{@link com.appium.element.MyElement}
	 * 
	 */
	List<MyElement> getElements();
	
	/**
	 * 获取测试页面
	 * 
	 * @return Excel中测试的页面
	 * 
	 */
	String getCaseSheet();
	
	/**
	 * 设置测试页面
	 * 
	 * @param caseSheet Excel中测试的页面
	 * 
	 */
	void setCaseSheet(String caseSheet);
	
	/**
	 * 设置测试用例数据
	 * 
	 * @param element 测试用例数据
	 * 
	 */
	void setAppElement(AppElement element);
	
	/**
	 * 设置当前元素等待的时间
	 * 
	 * @param seconds 元素等待秒数
	 * 
	 */
	void setWaitTime(int seconds);
	
	/**
	 * 获取 AppLocator{@link com.appium.locator.AppLocator}
	 * 
	 * @return AppLocator{@link com.appium.locator.AppLocator}
	 * 
	 */
	AppLocator<MobileElement> getLocator();
	
	/**
	 * 元素点击
	 * 
	 */
	void click();
	
	/**
	 * 元素输入数据
	 * 
	 */
	void setValue();
	
	/**
	 * 元素输入数据
	 * 
	 */
	void sendKeys();
	
	/**
	 * 长按操作，需要给定时间（s）
	 * 
	 */
	void longPress();
	
	/**
	 * 长按操作，时间为3s
	 * 
	 */
	void longTap();
}

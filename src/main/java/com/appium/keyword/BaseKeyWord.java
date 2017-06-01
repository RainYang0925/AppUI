package com.appium.keyword;

import com.appium.element.MyElement;
import com.appium.keyword.data.AppElement;
import com.appium.locator.AppLocator;
import com.framework.utils.SleepUtils;
import io.appium.java_client.MobileElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.util.List;

/**
 * 关键字驱动的抽象类，实现一些基本的公共方法
 * 
 * @version 1.2
 * 
 */

public abstract class BaseKeyWord implements KeyWord {

	protected Logger logger = LogManager.getLogger();
	
	protected AppLocator<MobileElement> locator;
	
	protected String caseSheet;
	protected AppElement appElement;
	protected By elementBy;
	protected String testData;

	public BaseKeyWord() {
		SleepUtils.threadSleepBySeconds(2);
	}
	
	@Override
	public AppLocator<MobileElement> getLocator() {
		return locator;
	}
	
	@Override
	public void setWaitTime(int seconds) {
		locator.setWaitTime(seconds);
	}

	public AppElement getAppElement() {
		return appElement;
	}
	
	/**
	 * 设置测试的元素信息
	 * 
	 * @param element 测试元素
	 * 
	 */
	public void setAppElement(AppElement element) {
		this.appElement = element;
		elementBy = element.getElementBy().getBy();
		element.getWaitSeco();
	}
	
	/**
	 * 设置当前用例输入的数据
	 * @param testData 测试输入数据
	 * 
	 */
	public void setTestData(String testData) {
		this.testData = testData;
	}
	
	/**
	 * 获取当前操作的元素
	 * 
	 * return MyElement {@link MyElement}
	 * 
	 */
	@Override
	public MyElement getElement() {
		return locator.getLocator(elementBy);
	}
	
	/**
	 * 获取当前操作的元素集合
	 * 
	 * return MyElements {@link MyElement}
	 * 
	 */
	@Override
	public List<MyElement> getElements() {
		return locator.getLocators(elementBy);
	}

	/**
	 * 获取测试页面
	 * 
	 * @return Excel文档测试的 sheet 页
	 * 
	 */
	@Override
	public String getCaseSheet() {
		logger.info("获取测试用例的 CaseSheet： " + caseSheet);
		return caseSheet;
	}
	
	/**
	 * 设置测试页面
	 * 
	 * @param caseSheet Excel文档测试的 sheet 页
	 * 
	 */
	@Override
	public void setCaseSheet(String caseSheet) {
		logger.info("设置测试用例的 CaseSheet： " + caseSheet);
		this.caseSheet = caseSheet;
	}
	
	//元素点击
	@Override
	public void click() {
		this.getElement().click();
	}
	
	//元素输入数据
	@Override
	public void setValue() {
		this.getElement().setValue(testData);
	}
	
	//元素输入数据
	@Override
	public void sendKeys() {
		this.getElement().sendKeys(testData);
	}
	
	//指定指定时间的长按方法
	@Override
	public void longPress() {
		this.getElement().tap(Integer.parseInt(testData) * 1000);
	}
	
	//默认时间的长按方法
	@Override
	public void longTap() {
		this.getElement().longTap();
	}
}

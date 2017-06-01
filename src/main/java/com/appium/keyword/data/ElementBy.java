package com.appium.keyword.data;

import com.appium.locator.LocatorType;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

/**
 * 元素的定位信息类：包含定位类型、定位内容
 * 
 */

public class ElementBy {

	private final String byType;
	private final String byDesc;
	private By by;
	
	/**
	 * 构造函数，根据定位信息和内容，创建元素的 by 信息
	 * 
	 * @param byType 定位类型
	 * @param byDesc 定位内容
	 * 
	 */
	public ElementBy(String byType, String byDesc) {
		this.byType = checkParam(byType);
		this.byDesc = checkParam(byDesc);
		setBy();
	}

	/**
	 * 检查参数是否为空
	 *
	 * @param info 信息
	 * @return 不为空的信息
	 */
	private String checkParam(String info) {
		if (info == null) {
			info = "";	//不是 null，不然会造成空指针错误
		}
		return info;
	}

	/**
	 * 设置元素的 MobileBy {@link io.appium.java_client.MobileBy} 信息
	 *
	 */
	private void setBy() {
		switch (byType) {
			case LocatorType.xpath:
				by = MobileBy.xpath(byDesc);
				break;
			case LocatorType.className:
				by = MobileBy.className(byDesc);
				break;
			case LocatorType.id:
				by = MobileBy.id(byDesc);
				break;
			case LocatorType.AccessibilityId:
				by = MobileBy.AccessibilityId(byDesc);
				break;
			case LocatorType.AndroidUIAutomator:
				by = MobileBy.AndroidUIAutomator(byDesc);
				break;
			case LocatorType.IosUIAutomation:
				by = MobileBy.IosUIAutomation(byDesc);
				break;
			case LocatorType.iOSNsPredicate:
				by = MobileBy.iOSNsPredicateString(byDesc);
				break;
			case LocatorType.iOSClassChain:
				by = MobileBy.iOSClassChain(byDesc);
				break;
			case LocatorType.linkText:
				by = MobileBy.linkText(byDesc);
				break;
			case LocatorType.cssSelector:
				by = MobileBy.cssSelector(byDesc);
				break;
			case LocatorType.name:
				by = MobileBy.name(byDesc);
				break;
			default:
				by = MobileBy.xpath(byDesc);
				break;
		}
	}

	public String getByType() {
		return byType;
	}

	public String getByDesc() {
		return byDesc;
	}
	
	public By getBy() {
		return by;
	}
	
	@Override
	public String toString() {
		return by.toString();
	}
}

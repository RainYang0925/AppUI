package com.appium.keyword.data;

/**
 * 元素的位置信息，包含：元素所处的页面名称、元素名称
 * 
 */

public class ElementLocation {

	private final String pageName;
	private final String elementName;
	
	/**
	 * 构造函数，创建元素的位置信息
	 * 
	 * @param pageName 页面名称
	 * @param elementName 元素名称
	 */
	public ElementLocation(String pageName, String elementName) {
		this.pageName = pageName;
		this.elementName = elementName;
	}
	
	public String getPageName() {
		return pageName;
	}

	public String getElementName() {
		return elementName;
	}
	
	@Override
	public String toString() {
		return "[" + pageName + "] 页面 - [" + elementName + "] 元素";
	}
	
}

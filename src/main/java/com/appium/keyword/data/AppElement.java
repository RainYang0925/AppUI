package com.appium.keyword.data;

import com.appium.locator.Locator;

/**
 * 元素的信息类，包含元素的位置、定位信息、等待时间，和操作方式（关键字）
 * 
 */

public class AppElement {

	private ElementLocation location;
	private ElementBy by;
	private int waitSeco;
	private String keyWord;
	
	/**
	 * 构造函数：元素需包含位置和定位信息
	 * 
	 * @param location 元素位置信息
	 * @param by 元素定位信息
	 */
	public AppElement(ElementLocation location, ElementBy by) {
		this.location = location;
		this.by = by;
	}

	/**
	 * 获取元素定位信息
	 *
	 * @return ElementBy {@link ElementBy}
	 */
	public ElementBy getElementBy() {
		return by;
	}

	/**
	 * 获取元素的位置页面信息
	 *
	 * @return ElementLocation {@link ElementLocation}
	 */
	public ElementLocation getElementLocation() {
		return location;
	}

	/**
	 * 设置元素操作关键字
	 *
	 * @param keyWord 操作方法关键字
	 * @return AppElement {@link AppElement}
	 */
	public AppElement setKeyWord(String keyWord) {
		this.keyWord = keyWord;
		return this;
	}

	/**
	 * 获取元素操作关键字
	 *
	 * @return 元素操作关键字
	 */
	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * 获取元素等待时间
	 *
	 * @return 元素等待时间
	 */
	public int getWaitSeco() {
		return waitSeco;
	}

	/**
	 * 设置元素等待时间
	 *
	 * @param waitSeco 元素等待时间（单位：s）
	 * @return AppElement {@link AppElement}
	 */
	public AppElement setWaitSeco(String waitSeco) {
		if (waitSeco != null && waitSeco != "") {
			this.waitSeco = Integer.parseInt(waitSeco);
		} else {
			this.waitSeco = Locator.DEFAULT_ELEMENT_WAIT_SECONDS;//默认元素等待时间
		}
		return this;
	}

	@Override
	public String toString() {
		return location.toString() + ", " + by.toString() + ", "
				+ "等待时间(s): [" + waitSeco 
				+ "], 关键字：[" + keyWord + "]";
	}
	
	
}

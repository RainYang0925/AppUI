package com.appium.keyword.data;

/**
 * 测试用例描述信息
 *
 */

public class CaseInfo {

	private String suiteID;
	private String testDesc;

	/**
	 * 构造函数
	 *
	 * @param suiteID 测试集合 id
	 * @param testDesc 测试用例描述
	 */
	public CaseInfo(String suiteID, String testDesc) {
		this.suiteID = suiteID;
		this.testDesc = testDesc;
	}

	public String getSuiteID() {
		return suiteID;
	}

	public String getTestDesc() {
		return testDesc;
	}

	@Override
	public String toString() {
		return suiteID + " - " + testDesc;
	}
	
}

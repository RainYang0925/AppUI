package com.appium.keyword.data;

/**
 * 从 Excel 测试文档读取的用例数据
 *
 */

public class CaseDatas {

	private CaseInfo caseInfo;
	private AppElement element;
	private String testInputData;

	/**
	 * 构造函数，需测试用例描述信息、元素信息构建
	 *
	 * @param caseInfo 测试用例描述信息
	 * @param element 元素信息
	 */
	public CaseDatas(CaseInfo caseInfo, AppElement element) {
		this.caseInfo = caseInfo;
		this.element = element;
	}

	public CaseInfo getTestCaseInfo() {
		return caseInfo;
	}

	public AppElement getAppElement() {
		return element;
	}

	public String getTestInputData() {
		return testInputData;
	}

	public CaseDatas setTestInputData(String testInputData) {
		this.testInputData = testInputData;
		return this;
	}

	@Override
	public String toString() {
		return caseInfo.toString() + " - " + element.toString() 
			+ ", 输入内容：[" + testInputData + "]";
	}
	
	
}

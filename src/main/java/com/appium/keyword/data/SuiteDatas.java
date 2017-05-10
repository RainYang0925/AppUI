package com.appium.keyword.data;

/**
 * 测试集合页的数据结构（Excel 文档中测试场景 Sheet）
 *
 */

public class SuiteDatas {

	private String id;
	private String suiteDesc;
	private String caseSheet;
	private String runMode;

	/**
	 *  构造函数：测试集合的数据
	 *
	 * @param id 测试集合id
	 * @param caseSheet 测试集合的 sheet 页
	 * @param runMode 是否运行测试集合
	 */
	public SuiteDatas(String id, String caseSheet, String runMode) {
		this.id = id;
		this.caseSheet = caseSheet;
		this.runMode = runMode;
	}

	public SuiteDatas setSuiteDesc(String suiteDesc) {
		this.suiteDesc = suiteDesc;
		return this;
	}
	
	public String getId() {
		return id;
	}

	public String getSuiteDesc() {
		return suiteDesc;
	}

	public String getCaseSheet() {
		return caseSheet;
	}

	public String getRunMode() {
		return runMode;
	}
	
}

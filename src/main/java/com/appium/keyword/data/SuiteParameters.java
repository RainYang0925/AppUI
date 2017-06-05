package com.appium.keyword.data;

/**
 * 测试集合的数据展示
 *
 */
public interface SuiteParameters {
	//测试集合页的表头设置
	String SuiteSheetName = "测试场景";		//测试集合sheet的名称
	int SUITE_CONFIG_CELL_NUMS = 4;
	
	int TestSuiteIDNum = 0;					//测试集合id
	int TestSuiteDescNum = 1;				//测试场景描述
	int TestCaseSheetNum = 2;				//需测试的 sheet
	int SuiteRunModeNum = 3;				//是否运行列号
	int SuiteResultNum = 4;					//测试集合结果列号
	
}

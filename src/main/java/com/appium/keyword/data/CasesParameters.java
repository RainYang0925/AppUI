package com.appium.keyword.data;

/**
 * 针对测试数据的 Excel 表，添加表头名称，和表头指定列数
 * 
 * @version 1.1
 * 
 * */

public interface CasesParameters {

	//设置测试用例一行的最大列数，也就是说每一行需要读取多少个数据
	int MAX_CASE_PARAMETERS = 11;		//无需读取测试结果
	
	//测试用例步骤页的表头设置
	String test_suite_ID = "测试集合ID";
	int test_suite_ID_cell_num = 0;
	
	String test_scene_desc = "测试场景描述";
	int test_scene_desc_cell_num = 1;
	
	String test_step = "测试步骤ID";
	int test_step_cell_num = 2;

	String test_describe = "测试步骤描述";
	int test_describe_cell_num = 3;
	
	String test_page = "测试页面";
	int test_page_cell_num = 4;
	
	String element_name = "元素名称";
	int element_name_cell_num = 5;
	
	String byType = "定位方式";
	int byType_cell_num = 6;
	
	String element_by_desc = "元素描述";
	int element_by_desc_cell_num = 7;
	
	String wait_seconds = "等待时间（单位：s）";
	int wait_seconds_cell_num = 8;
	
	String test_data = "测试数据";
	int test_data_cell_num = 9;
	
	String element_keyWord = "关键字";
	int element_keyWord_cell_num = 10;
	
	String test_result = "测试结果";
	int test_result_cell_num = 11;
	
}

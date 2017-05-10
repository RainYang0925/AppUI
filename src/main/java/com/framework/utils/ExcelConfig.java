package com.framework.utils;

/**
 * 测试配置数据的 Excel 表，指定列数
 * 
 * @version 1.0
 * 
 */

public interface ExcelConfig {

	//配置 sheet
	String ConfigSheet = "配置";			//配置 sheet 的名称
	
	String Test_ID = "测试编号";
	int Test_ID_Cell_Num = 0;
	
	String Device_Name = "手机名称";
	int Device_Name_Cell_Num = 1;
	
	String Platform_Name = "手机系统";
	int Platform_Name_Cell_Num = 2;
	
	String Platform_Version = "手机系统版本";
	int Platform_Version_Cell_Num = 3;
	
	String UDID = "设备标识符";
	int UDID_Cell_Num = 4;
	
	String APP = "安装包路径";
	int APP_Cell_Num = 5;
	
	String IsInstallAPP = "是否安装应用";
	int IsInstallAPP_Cell_Num = 6;
	
	String BundleId = "测试应用";
	int BundleId_Cell_Num = 7;
	
	String AppPackage = "测试应用";
	int AppPackage_Cell_Num = 7;
	
	String StartActivity = "测试启动页面";
	int StartActivity_Cell_Num = 8;
	
	String AutomationName = "测试引擎";
	int AutomationName_Cell_Num = 9;
	
	String LocalIP = "本地IP";
	int LocalIP_Cell_Num = 10;
	
	String Port = "端口号";
	int Port_Cell_Num = 11;
	
	String LogPath = "日志输出路径";
	int LogPath_Cell_Num = 12;
}

package com.framework.utils;

import com.appium.keyword.data.*;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读写 Excel 中不同 sheet 的表数据
 * 
 * @version 1.4
 * 
 */

public class ExcelUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	public final static String XLS = "xls";
	public final static String XLSX = "xlsx";
	public final static String CSV = "csv";
	
	private String filePath;
	private static String fileFullPath;
	private String fileName;
	
	private static Workbook workbook = null;
	
	/**
	 * 读取 excel 的构造函数，需传进文件路径和文件名称
	 * 
	 * @param filePath Excel文件路径
	 * @param fileName Excel文件名
	 * 
	 */
	public ExcelUtils(String filePath, String fileName) {
		logger.info("Excel 所在的文件路径：" + filePath);
		this.filePath = filePath;
		logger.info("Excel 文件名称：" + fileName);
		this.fileName = fileName;
		fileFullPath = filePath + File.separator + fileName;
		logger.info("Excel 文件绝对路径：" + fileFullPath);
		this.getExcelFile(fileFullPath);
	}
	
	/**
	 * 读取 excel 的构造函数，传进完成的文件路径，包含文件名称
	 * 
	 * @param fileFullPath 包含文件名称的完整路径
	 * 
	 */
	public ExcelUtils(String fileFullPath) {
		ExcelUtils.fileFullPath = fileFullPath;
		logger.info("Excel 文件绝对路径：" + fileFullPath);
		String[] breakupString = fileFullPath.split(File.separator);
		fileName = breakupString[breakupString.length - 1];
		logger.info("Excel 文件名称：" + fileName);
		filePath = "";
		for (int i = 0; i < breakupString.length - 1; i ++) {
			filePath = filePath + breakupString[i] + File.separator;
		}
		logger.info("Excel 所在的文件路径：" + filePath);
		this.getExcelFile(fileFullPath);
	}
	
	/**
	 * 获取 Excel 文件的类型，如果不是 Excel 文件，返回空指针
	 * 
	 * @return Excel 文件类型，如：xls、xlsx等
	 */
	public String getExcelType() {
		if (fileName.toLowerCase().endsWith(XLSX)) {
			logger.info("Excel 文件类型：" + XLSX);
			return XLSX;
		} else if (fileName.toLowerCase().endsWith(XLS)) {
			logger.info("Excel 文件类型：" + XLS);
			return XLS;
		} else {
			logger.error("所读文件不是为 Excel 文件", new FileNotFoundException("This file isn't excel."));
			return null;
		}
	}
	
	/**
	 * 读取整个 Excel 文件，所有表的数据
	 * 
	 * @return 整个 Excel 文件的数据内容
	 */
	public Map<String, List<HashMap<String, String>>> readExcelByAllSheets() {
		String excelType = getExcelType();
		String[] sheetNames = getSheetNames(excelType);
		return readExcelBysheets(sheetNames, excelType);
	}
	
	/**
	 * 读取 Excel 文件中指定 sheetNames[] 的所有数据
	 * 
	 * @param sheetNames 存放 sheetNames 的数组
	 * 
	 * @return 整个 Excel 文件的数据内容
	 */
	public Map<String, List<HashMap<String, String>>> readExcelBysheets
	(String[] sheetNames) {
		
		return readExcelBysheets(sheetNames, getExcelType());
	}
	
	/**
	 * 读取整个 Excel 表的所有数据
	 * 
	 * @param sheetNames 一个 Excel 的 sheetName 数组
	 * @param excelType Excel 文件的类型
	 * 
	 * @return 整个 Excel 文件的数据内容
	 */
	public HashMap<String, List<HashMap<String, String>>> readExcelBysheets
	(String[] sheetNames, String excelType) {
		HashMap<String, List<HashMap<String, String>>> mapListMap
						= new HashMap<String, List<HashMap<String, String>>>();
		for (int i = 0; i < sheetNames.length; i++) {
			mapListMap.put(sheetNames[i], readSheet(sheetNames[i], excelType));
		}
		logger.info("读取 Excel 文件成功");
		return mapListMap;
	}
	
	/**
	 * 获得 Excel 文件的所有 sheet 名字
	 * 
	 * @return Excel 的 sheetName 数组
	 */
	public String[] getSheetNames() {
		return getSheetNames(getExcelType());
	}
	
	/**
	 * 获得 Excel 文件的所有 sheet 名字
	 * 
	 * @param excelType Excel文件的类型
	 * 
	 * @return Excel 的 sheetName 数组
	 */
	public String[] getSheetNames(String excelType) {
		//用一个字符串数组，存储 excel 的 sheet 名字
        String[] sheetNames = null;
        int sheetNumbers = workbook.getNumberOfSheets();
        logger.info("这个文件有 " + sheetNumbers + " 个表");
    	sheetNames = new String[sheetNumbers];
    	String sheetNameLog = "表名：\"";
    	for (int i = 0; i < sheetNumbers; i++) {
			sheetNames[i] = workbook.getSheetName(i);
			//log描述
			sheetNameLog = sheetNameLog + sheetNames[i] + "\"";
			if (i < sheetNumbers - 1) {
				sheetNameLog = sheetNameLog + ", ";
			} else {
				sheetNameLog = sheetNameLog + " ";
			}
		}
    	logger.info(sheetNameLog);
		return sheetNames;
	}

	/**
	 * 判断 sheet 是否存在
	 *
	 * @param sheetName sheet 名
	 * @return 指定的 sheet 是否存在
	 */
	public boolean isSheetExist(String sheetName) {
		String[] sheetNames = getSheetNames();
		for (int i = 0; i < sheetNames.length; i++) {
			if (sheetNames[i].equalsIgnoreCase(sheetName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 读取 Excel的指定 sheet 名称的数据
	 * 
	 * @param sheetName Excel文件的表名
	 * 
	 * @return 单个 sheet 表的内容
	 */
	public List<HashMap<String, String>> readSheet(String sheetName) {
		return readSheet(sheetName, getExcelType());
	}
	
	/**
	 * 读取 Excel的一个 sheet 表数据,需传进 Excel文件的文件类型，xls 或者 xlsx 等
	 * 
	 * @param sheetName Excel文件的表名
	 * @param excelType Excel文件的类型
	 * 
	 * @return 单个 sheet 表的内容
	 */
	public List<HashMap<String, String>> readSheet(String sheetName, String excelType) {
        Sheet sheet = null;
        Row row;
        Cell cell;
        sheet = workbook.getSheet(sheetName);
        logger.info("正在读取的表名：" + sheetName);
		//以 excel 文件一个表的行和列，定义一个二维数组
		int rowNum = sheet.getLastRowNum() + 1;						//获取有元素的行数
		int cellNum = sheet.getRow(0).getPhysicalNumberOfCells();	//获取第一行有元素的列数，第一行为标题，应为最长
		String[][] sheetData = new String[rowNum][cellNum];			//用来存储表数据的二维数组
		logger.info("行数：" + rowNum);
		logger.info("列数：" + cellNum);
		//遍历整个 sheet 表的数据
		for (int i = 0; i <rowNum; i++) {								//遍历每一行数据
			row = sheet.getRow(i);
			for (int j = 0; j < cellNum; j++) {							//遍历每一列数据
				cell = row.getCell(j);
				if (cell != null) {										//如果单元格元素不为空，以字符串形式读取
					cell.setCellType(CellType.STRING);
					sheetData[i][j] = cell.getStringCellValue();
				} else {												//如果单元格元素为空，则为空
					sheetData[i][j] = null;
				}
			}
			
		}
        //创建一个 List<HashMap> 对象，用来存储从 excel 文件读取到的内容
        List<HashMap<String, String>> listMap = changeSheetTextBecomeListMap(sheetData);
		logger.info("读取表数据成功");
        return listMap;
	}
	
	/**
	 * 打开 Excel 文件，并返回一个 FileInputStream 对象
	 * 
	 * @param fileFullPath Excel文件的绝对路径
	 * 
	 * @return FileInputStream
	 */
	public FileInputStream getExcelFile(String fileFullPath) {
		FileInputStream fis = new FileUtils().readFile(fileFullPath);
        try {
			if (this.getExcelType().equalsIgnoreCase(XLS)) {
				workbook = new HSSFWorkbook(fis); 						//读取excel文件
			} else if (this.getExcelType().equalsIgnoreCase(XLSX)) {
				workbook = new XSSFWorkbook(fis);
			} 
		} catch (IOException e) {
			logger.error("打开文件时\"" + fileFullPath + "\"出现错误。\n", e);
		}
		return fis;
	}
	
	/**
	 * 将输入流关闭
	 * 
	 * @param fis 输入流
	 */
	public void closeExcelFile(InputStream fis) {
		try {
			fis.close();
		} catch (IOException e) {
			logger.error("关闭文件出现错误。\n", e);
		}
	}
	
	/**
	 * 将从 sheet 表里提取到的数据（以二维数组存放），转化为以 List<Map>形式存放
	 * 
	 * @param sheetData 存放 sheet 表数据的二维数组
	 * 
	 * @return 单个 sheet 表的内容
	 */
	private List<HashMap<String, String>> changeSheetTextBecomeListMap(String[][] sheetData) {
		List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		//将从表数据获得的二维数组，存到 List<Map> 中
		for (int i = 1; i < sheetData.length; i ++) {
			HashMap<String, String> map = new HashMap<String, String>();
			//讲每一行的测试数据，以“标题-测试数据”的形式存到 Map 中
    		for (int j = 0; j < sheetData[0].length; j++) {
    			map.put(sheetData[0][j], sheetData[i][j]);
			}
    		listMap.add(map);
		}
		logger.info("成功将表的数据转化为List<Map>类型");
		return listMap;
	}
	
	/**
	 * 获取一行的测试数据
	 * 
	 * @param sheetName 表名
	 * @param rowNum 行数
	 * @param cells 需要读取的列数目
	 * 
	 * @return 一行的测试用例数据
	 */
	public String[] readTestDatas(String sheetName, int rowNum, int cells) {
        Sheet sheet = null;
        Row row;
        Cell cell;
        sheet = workbook.getSheet(sheetName);
        logger.info("正在读取的表名：" + sheetName);
        
		//以 excel 文件一个表的行和列，定义一个二维数组
		String[] rowDatas = new String[cells];					//用来存储表数据的二维数组
		logger.info("正在读取第 " + rowNum + " 行");
		logger.info("需要读取的列数：" + cells);
		
		row = sheet.getRow(rowNum);
		for (int j = 0; j < cells; j++) {							//遍历每一列数据
			cell = row.getCell(j);
			if (cell != null) {										//如果单元格元素不为空，以字符串形式读取
				cell.setCellType(CellType.STRING);
				rowDatas[j] = cell.getStringCellValue();
			} else {												//如果单元格元素为空，则为空
				rowDatas[j] = null;
			}
		}
		return rowDatas;
	}

	/**
	 * 读取 Android 测试设备号、测试应用包名、启动页面等信息
	 * 
	 * @param rowNum 行号，从1开始
	 *
	 * @return 设备测试配置数据
	 */
	public Map<String, String> getAndroidDeviceTestConfig(int rowNum) {
		int lastCellNum = 5;
		String[] deviceData = readTestDatas("配置", rowNum, lastCellNum);
		Map<String, String> map = new HashMap<>();
		map.put("id", deviceData[0]);
		map.put(MobileCapabilityType.UDID, deviceData[1]);
		map.put(AndroidMobileCapabilityType.APP_PACKAGE, deviceData[2]);
		map.put(AndroidMobileCapabilityType.APP_ACTIVITY, deviceData[3]);
		map.put(MobileCapabilityType.APP, deviceData[4]);
		return map;
	}
	
	/**
	 * 读取 ios 测试设备号、测试应用包名、启动页面等信息
	 *
	 * @param rowNum 行号，从1开始
	 *
	 * @return 设备测试配置数据
	 */
	public Map<String, String> getIOSDeviceTestConfig(int rowNum) {
		int lastCellNum = 4;
		String[] deviceData = readTestDatas("配置", rowNum, lastCellNum);
		Map<String, String> map = new HashMap<>();
		map.put("id", deviceData[0]);
		map.put(MobileCapabilityType.UDID, deviceData[1]);
		map.put(IOSMobileCapabilityType.BUNDLE_ID, deviceData[2]);
		map.put(MobileCapabilityType.APP, deviceData[3]);
		return map;
	}

	/**
	 * 获取测试场景的集合测试用例数据
	 *
	 * @param sheetName 表名
	 * @param rowNum 行号
	 *
	 * @return 测试场景集合的数据
	 */
	public SuiteDatas getTestSuite(String sheetName, int rowNum) {
		String[] sdatas = readTestDatas(sheetName, rowNum, SuiteParameters.SUITE_CONFIG_CELL_NUMS);
		SuiteDatas suiteDatas = new SuiteDatas(
				sdatas[SuiteParameters.TestSuiteIDNum],
				sdatas[SuiteParameters.TestCaseSheetNum],
				sdatas[SuiteParameters.SuiteRunModeNum])
				.setSuiteDesc(sdatas[SuiteParameters.TestSuiteDescNum]);
		return suiteDatas;
	}

	/**
	 * 获取一行的测试用例数据
	 * 
	 * @param sheetName 表名
	 * @param rowNum 行数
	 * 
	 * @return 一行的测试用例数据
	 */
	public CaseDatas getTestCase(String sheetName, int rowNum) {
		String[] datas = readTestDatas(sheetName, rowNum, CasesParameters.MAX_CASE_PARAMETERS);
		//设置一个测试用例所需要的基本元素
		CaseInfo caseInfo = new CaseInfo(
				datas[CasesParameters.test_suite_ID_cell_num], 
				datas[CasesParameters.test_scene_desc_cell_num]);
		//元素的位置信息
		ElementLocation elementLocation = new ElementLocation(
				datas[CasesParameters.test_page_cell_num], datas[CasesParameters.element_name_cell_num]);
		//元素的 by 信息
		ElementBy elementBy = new ElementBy(datas[CasesParameters.byType_cell_num], 
				datas[CasesParameters.element_by_desc_cell_num]);
		//设置一个元素所需要的基本信息
		AppElement appElement = new AppElement(elementLocation, elementBy)
				.setWaitSeco(datas[CasesParameters.wait_seconds_cell_num])
				.setKeyWord(datas[CasesParameters.element_keyWord_cell_num]);
		//创造一个测试用例需要的元素
		return new CaseDatas(caseInfo, appElement)
				.setTestInputData(datas[CasesParameters.test_data_cell_num]);
	}
	
	/**
	 * 写入一个数据到 excel 指定的单元格中（由行、列数标记），行、列数，从0开始
	 * 
	 * @param sheetName 表的名称
	 * @param rowNum 行数
	 * @param cellNum 列数
	 * 
	 * @return 单元格内容
	 */
	public String getCellData(String sheetName, int rowNum, int cellNum) {
		String cellData = null;
		try {
			Sheet sheet = workbook.getSheet(sheetName);
			
			Row row = sheet.getRow(rowNum);							//获取到相应的行数
			Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);						//获取到相应的列数
			if (cell != null) {										//如果单元格元素不为空，以字符串形式读取
				cell.setCellType(CellType.STRING);
				cellData = cell.getStringCellValue();
			} else {												//如果单元格元素为空，则为空
				cellData = null;
			}
			logger.info("在 sheet: [" + sheetName + "] 的第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，读取数据：\"" + cellData + "\"");
		} catch (Exception e) {
			logger.error("在 sheet: [" + sheetName + "] 的第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，读取数据失败。");
		}
		return cellData;
	}
	
	/**
	 * 写入一个数据到 excel 指定的单元格中（由行、列数标记），行、列数，从0开始
	 * 
	 * @param sheetName 表的名称
	 * @param rowNum 行数
	 * @param cellNum 列数
	 * @param testRsult 测试结果
	 * 
	 */
	public static void setCellData(String sheetName, int rowNum, int cellNum, String testRsult) {
        
        Sheet sheet = null;
        sheet = workbook.getSheet(sheetName);														//读取相应sheet
        
        Row row = sheet.getRow(rowNum);																//获取到相应的行数
        Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell == null) {
            cell = row.createCell(cellNum);
        }
        cell.setCellValue(testRsult);																//向指定的单元格输入内容
        logger.info("在 sheet: [" + sheetName + "] 的第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，写入数据：\"" + testRsult + "\"");
        
        try {
			FileOutputStream out = new FileOutputStream(fileFullPath); //向指定的excel文件中写数据
			workbook.write(out);
			out.flush();
			out.close();
			logger.info("对 Excel文件写入数据成功。");
		} catch (IOException e) {
			logger.error("写入数据失败。\n", e);
		}
	}
	
    /**
     * 获取到 sheet 页最后一行
     * 
     * @param sheetName Excel 文件的页名
     * 
     * @return sheet 页最后一行的行号
     */
	public int getLastRowNums(String sheetName) {
		return workbook.getSheet(sheetName).getLastRowNum();
	}
}

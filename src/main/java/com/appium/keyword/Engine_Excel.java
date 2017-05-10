package com.appium.keyword;

import com.framework.utils.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.appium.keyword.data.CaseDatas;
import com.appium.keyword.data.CasesParameters;
import com.appium.keyword.data.SuiteDatas;
import com.appium.keyword.data.SuiteParameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.framework.report.ExtentManager;

/**
 * 关键字驱动，以 Excel 文件为数据源，进行处理，如更改 Excel 文件表格的格式，需重新更改或新增与 Excel 文件对应的类
 * 
 * @version 1.1
 * 
 */

public class Engine_Excel {

	private Logger logger = LogManager.getLogger();
	private static ExtentReports extent =
			ExtentManager.createInstance(ConfigManager.getReportPath() + "ibeiliao-client-report-" + DateTimeUtils.getFileDateTime().replaceAll("\\W", "_") + ".html");
    private static ThreadLocal<ExtentTest> suiteTest = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> pageTest = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> elementTest = new ThreadLocal<>();

    ConfigManager configManager;
	private ExcelUtils excelUtils;
	
	public static boolean bResult;
	
	private final String suiteSheet = KeyWord.SUITESHEET;
	public String caseSheet;
	public boolean suiteIsWrited;
	
	/**
	 * 使用默认模块
	 * 
	 */
	public Engine_Excel() {
		configManager = ConfigManager.getInstance();
	}
	
	/**
	 * 使用指定的 Excel 文件路径
	 * 
	 * @param fullPath Excel文件路径
	 * 
	 */
	public Engine_Excel(String fullPath) {
		configManager = ConfigManager.getInstance();
		setExcelName(fullPath);
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public void setExcelName(String excelPath) {
		excelUtils = new ExcelUtils(excelPath);
	}

	/**
	 * 测试页面的调用方法
	 * 
	 * @param actionKeyWords {@link com.appium.keyword.BaseKeyWord}
	 * 
	 */
	public synchronized void runTest(BaseKeyWord actionKeyWords) {
		bResult = true;
		suiteIsWrited = false;
		SuiteDatas suiteDatas;
		//循环读取suite Sheet里面的值，找出运行的场景
		for(int i = 1; i <= excelUtils.getLastRowNums(suiteSheet); i ++) {//从1开始，忽略第一行的表头
			//先获取测试集合的数据
			suiteDatas = excelUtils.getTestSuite(suiteSheet, i);
			String Runmode = suiteDatas.getRunMode();
			
			//先确定该测试场景是否运行，且不能为空
			if (StringUtils.isNotEmpty(Runmode) && Runmode.equalsIgnoreCase("YES")) {
				//获取测试的sheet
				caseSheet = suiteDatas.getCaseSheet();
				
				//测试场景为运行状态，且找到相应的 sheet，才执行测试
				if (excelUtils.isSheetExist(caseSheet) && actionKeyWords.getCaseSheet().equals(caseSheet)) {
					String suiteDesc = suiteDatas.getSuiteDesc();
					
					//每个场景测试，在测试报告都显示在第一重（测试报告代码）
					ExtentTest suiteExtent = extent.createTest(suiteDesc);
					suiteTest.set(suiteExtent);
					logger.info("正在测试：" + suiteDesc);
					
					String suiteId = suiteDatas.getId();
					//每个 casesheet测试，在测试报告显示在第二重（测试报告代码）
					ExtentTest caseExtentTest = suiteTest.get().createNode(caseSheet);
					pageTest.set(caseExtentTest);
					
					int caseRowNum;
					CaseDatas caseDatas;
					//根据 stepTestSuiteId 在 caseSheet 中循环查找相对应的执行步骤
					for(caseRowNum = 1; caseRowNum <= excelUtils.getLastRowNums(caseSheet); caseRowNum ++) {//从1开始，忽略第一行的表头
						bResult = true;
						caseDatas = excelUtils.getTestCase(caseSheet, caseRowNum);
						//获取到测试集合 id
						String testSuiteId = caseDatas.getTestCaseInfo().getSuiteID();
						//case的测试集合id和 suite 的一致，执行测试
						
						if(StringUtils.isNotEmpty(testSuiteId) && testSuiteId.trim().contains(suiteId)) {
							//获取 Excel 数据源测试数据
							String keyword = caseDatas.getAppElement().getKeyWord();
							//设置当前正在测试的测试用例数据
							actionKeyWords.setAppElement(caseDatas.getAppElement());
							
							//如果关键字为空，直接跳过运行方法
							if (StringUtils.isNotEmpty(keyword)) {
								//元素的测试，在测试报告显示在第三重（测试报告代码）
								ExtentTest elementExtentTest = pageTest.get().createNode(caseDatas.getAppElement().getElementLocation().toString());
								elementTest.set(elementExtentTest);
								
								actionKeyWords.setTestData(caseDatas.getTestInputData());
								this.action(actionKeyWords, caseRowNum);
							} else {
								ExcelUtils.setCellData(caseSheet, caseRowNum, CasesParameters.test_result_cell_num, TestResult.PASS);
							}
							
							//输出测试集合的结果，失败只需要写一次就可以
							if(bResult == false && suiteIsWrited == false) {
								ExcelUtils.setCellData(suiteSheet, i, SuiteParameters.SuiteResultNum, TestResult.FAIL);
								suiteIsWrited = true;
							}
						}
					}
					
					//输出测试集合的结果，每个按钮操作成功后，都要对测试结果进行填写
					if(bResult == true && suiteIsWrited == false) {
						ExcelUtils.setCellData(suiteSheet, i, SuiteParameters.SuiteResultNum, TestResult.PASS);
					}
					//一个测试场景测完之后，在下一个测试场景之前将 “是否已填写测试结果” 的值变为 false
					suiteIsWrited = false;
				} else {
					//如果测试用例需要执行，但找不到相应的 sheet 页，在测试场景sheet，返回失败的测试结果
					//ExcelUtils.setCellData(suiteSheet, i, ExcelData.SuiteResultNum, TestResult.FAIL);
				}
			} else {
				logger.info("没有执行的测试用例。");
				continue;
			}
		}		
	}
	
	/**
	 * 获取未输出的测试报告
	 * 
	 * @return ExtentReports{@link com.aventstack.extentreports.ExtentReports}
	 * 
	 */
	public static synchronized ExtentReports getExtentReports() {
		return extent;
	}
	
	/**
	 * 指定关键字中的方法
	 * 
	 * @param actionKeyWord {@link com.appium.keyword.BaseKeyWord}
	 * @param rowNum Excel行号
	 * 
	 */
	private void action(BaseKeyWord actionKeyWord, int rowNum) {
		//java反射拿到对应的所有方法
		Method[] methods = actionKeyWord.getClass().getMethods();
		for(int i = 0; i < methods.length; i ++) {
			String keyword = actionKeyWord.getAppElement().getKeyWord();
			if(methods[i].getName().trim().equals(keyword)) {
				String pageName = actionKeyWord.getAppElement().getElementLocation().getPageName();
				String elementName = actionKeyWord.getAppElement().getElementLocation().getElementName();
				try {
					logger.info("正在 \"" + pageName + "\" 页面，测试 \"" + elementName + "\"");
					logger.info("在 \"" + actionKeyWord.getClass().getName() + "\" 运行 \"" + methods[i].getName() + "\" 方法。");
					//调用指定关键字的方法
					methods[i].invoke(actionKeyWord);
					elementTest.get().info(elementName + " 测试通过");
					//填写测试结果
					ExcelUtils.setCellData(actionKeyWord.getCaseSheet(), rowNum, CasesParameters.test_result_cell_num, TestResult.PASS);
				} catch (Exception e) {
					//当方法调用出错时，也默认测试失败，可能会发生在这几种情况：关键字查找方法失败、appium server 启动成功，但找不到测试设备 等
					logger.error("调用\"" + keyword + "\"方法，出现错误。\n", e);
					//确认好完整的截图名称
					String screenShotName = ConfigManager.getScreenshotPath()
							+ pageName + "-" + elementName
							+ "_" + TestResult.FAIL
							+ "_" + DateTimeUtils.getFileDateTime()
							+ ".png";
					actionKeyWord.getLocator().getScreenShot(screenShotName);
					try {
						Engine_Excel.elementTest.get()
								.fail(elementName + "测试失败。\n")
								.fail(e, MediaEntityBuilder.createScreenCaptureFromPath(screenShotName).build());
					} catch (IOException e1) {
						logger.error("填写测试结果失败\n", e);
					}
					ExcelUtils.setCellData(actionKeyWord.getCaseSheet(), rowNum, CasesParameters.test_result_cell_num, TestResult.FAIL);
					
				}
				break;
			} /*else {
				logger.warn("在 \"" + actionKeyWords.getClass().getName() + "\" 查找不到 \"" + keywords + "\" 方法");
			}*/
		}
	}
}

package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 读取 properties 配置文件
 * 
 * */

public class ConfigManager {

	public static final String APPIUM_JS_PATH = "APPIUM_JS_PATH";
    public static final String XCODE_SETTING_PATH = "XCODE_SETTING_PATH";
	public static final String EXCEL_PATH = "EXCEL_PATH";
    public static final String ANDROID_EXCEL_NAME = "ANDROID_EXCEL_NAME";
    public static final String IOS_EXCEL_NAME = "IOS_EXCEL_NAME";
    public static final String RESULT_PATH = "RESULT_PATH";
    public static final String LOG_PATH = "LOG_PATH";
	public static final String REPORT_PATH = "REPORT_PATH";
	public static final String SCREENSHOT_PATH = "SCREENSHOT_PATH";
	public static final String BROWSER_TYPE = "BROWSER_TYPE";
	public static final String TEST_FRAMEWORK = "TEST_FRAMEWORK";
	public static final String RUNMODE = "RUNMODE";
    public static final String COMMAND_TIME_OUT = "COMMAND_TIME_OUT";

	private Properties prop = new Properties();
	private static ConfigManager instance;
	static Logger logger = LogManager.getLogger();
	
    private ConfigManager(String configFile) {
        try {
            FileInputStream inputStream = new FileInputStream(configFile);
            prop.load(inputStream);
        }catch(IOException e) {
            logger.catching(e);
        }
    }
    
    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            String configFile = "src/main/resources/config.properties";
            //使用系统变量的 config
            if (System.getenv().containsKey("CONFIG_FILE")) {
                configFile = System.getenv().get("CONFIG_FILE");
                System.out.println("Using config file from " + configFile);
                logger.info("从 [" + configFile + "] 使用配置文件");
            }
            instance = new ConfigManager(configFile);
        }
        return instance;
    }

    public boolean containsKey(String key) {
        return prop.containsKey(key);
    }

    public static String getXcodeSettingPath() {
        String xcodeSettingPath = ConfigManager.getInstance().getProperty(XCODE_SETTING_PATH);
        if (xcodeSettingPath.startsWith("src" + File.separator)) {
            xcodeSettingPath = System.getProperty("user.dir") + xcodeSettingPath;
        }
        return xcodeSettingPath;
    }

    public static String getAppiumJsPath() {
        return ConfigManager.getInstance().getProperty(APPIUM_JS_PATH);
    }

    public static String getExcelPath() {
        String excelPath = ConfigManager.getInstance().getProperty(EXCEL_PATH);
        if (excelPath.startsWith(File.separator + "test-datas" + File.separator)) {
            excelPath = System.getProperty("user.dir") + excelPath;
        }
        System.err.println(excelPath);
        return excelPath;
    }

    public static String getAndroidExcelName() {
        ConfigManager configManager = ConfigManager.getInstance();
        return getExcelPath() + configManager.getProperty(ANDROID_EXCEL_NAME);
    }

    public static String getIOSExcelName() {
        ConfigManager configManager = ConfigManager.getInstance();
        return getExcelPath() + configManager.getProperty(IOS_EXCEL_NAME);
    }

    public static String getResultPath() {
        String resultPath = ConfigManager.getInstance().getProperty(RESULT_PATH);
        if (resultPath.startsWith(File.separator + "test-result" + File.separator)) {
            resultPath = System.getProperty("user.dir") + resultPath;
        }
        return resultPath;
    }

    public static String getLogPath() {
        return getResultPath() + ConfigManager.getInstance().getProperty(LOG_PATH);
    }

    public static String getReportPath() {
        return getResultPath() + ConfigManager.getInstance().getProperty(REPORT_PATH);
    }

    public static String getScreenshotPath() {
        return getResultPath() + ConfigManager.getInstance().getProperty(SCREENSHOT_PATH);
    }

}

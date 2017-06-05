package com.appium.locator;

import com.appium.manager.android.AndroidConfig;
import com.appium.manager.android.AndroidDeviceInfo;
import com.appium.manager.ios.IOSConfig;
import com.appium.manager.ios.IOSDeviceInfo;
import com.appium.service.AppiumManager;
import com.framework.utils.ConfigManager;
import com.framework.utils.FileUtils;
import com.framework.utils.HttpUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.*;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * 封装获取 driver 的方法，参数都是默认从参数类获取
 * 
 * @version 1.1
 * 
 */

public class GetLocator {

	private Logger logger = LogManager.getLogger(GetLocator.class);
	private AppiumManager appiumManager;
	private ConfigManager config;
	private AppiumDriverLocalService appiumDriverLocalService;

	public GetLocator() throws IOException {
		config = ConfigManager.getInstance();
		appiumManager = new AppiumManager();
	}

	/**
	 * 测试 Android 时，需要获取的 AndroidDriver
	 *
	 * @param deviceConfig 设备和测试信息
	 *
	 * @return AndroidLocator {@link com.appium.locator.AndroidLocator}
	 *
	 * @throws IOException 异常
	 *
	 */
	public AndroidLocator<MobileElement> getAndroidLocator(Map<String, String> deviceConfig) throws IOException {
		AndroidConfig android = new AndroidConfig();
		String deviceID = deviceConfig.get(MobileCapabilityType.UDID);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//Android设备号
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, android.getDeviceName(deviceID));
		//可以不需要填写系统平台，因为已采用了 AndroidDriver 默认使用 Android 平台
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		//Android版本号
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, android.getDeviceOSVersion(deviceID));
		//支持中文输入
		capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, Boolean.TRUE);
		capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, Boolean.TRUE);
		//设置app的主包名和主类名
		String app_package = deviceConfig.get(AndroidMobileCapabilityType.APP_PACKAGE);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, app_package);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, deviceConfig.get(AndroidMobileCapabilityType.APP_ACTIVITY));
		//android 的自动化测试引擎
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, android.getAutomatorName(deviceID));
		if (android.getAutomatorName(deviceID).equals(AutomationName.ANDROID_UIAUTOMATOR2)) {
			//指定UIAutomator2的端口号
			capabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, new HttpUtils().getPort());
			//使用 adb 命令解锁手机，跳过 Appium
			android.unlockDevice(deviceID);
			//跳过解锁的进程
			capabilities.setCapability("skipUnlock", Boolean.TRUE);
		}
		//设置命令启动超时时间
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Integer.parseInt(config.getProperty(ConfigManager.COMMAND_TIME_OUT)));
		//指定设备
		capabilities.setCapability(MobileCapabilityType.UDID, deviceID);
		//指定 apk 路径
		capabilities.setCapability(MobileCapabilityType.APP, FileUtils.getAbsolutePath(deviceConfig.get(MobileCapabilityType.APP)));
		//确认是否需要重新安装被测应用
		if (android.checkAppIsInstalled(deviceID, app_package)) {
			//android.clearAppData(deviceID, deviceConfig.get(AndroidMobileCapabilityType.APP_PACKAGE));
		} else {
			android.installApp(deviceID, app_package);
		}
		capabilities.setCapability(MobileCapabilityType.NO_RESET, Boolean.TRUE);

		logger.info("the capability: " + capabilities.toString());

		//初始化设备信息，等待 Locator 初始化完后设置，防止获取的默认输入法错误
		AndroidDeviceInfo deviceInfo = new AndroidDeviceInfo(capabilities);
		AndroidDriver<MobileElement> driver;
		AndroidLocator<MobileElement> locator = null;
		//启动 Appium 服务
		appiumDriverLocalService = appiumManager.startAppiumServerForAndroid(capabilities);

		try {
			logger.info("开始创建 AndroidDriver.");
			driver = new AndroidDriver<>(new URL(appiumManager.getAppiumUrl().toString()), capabilities);
			locator = new AndroidLocator<>(driver);
			locator.setAppiumDriverLocalService(appiumDriverLocalService);
			locator.setDeviceInfo(deviceInfo);
			logger.info(" AndroidDriver 创建成功");
			//成功初始化后，使用 adb 命令解锁，防止屏幕已熄灭
			android.unlockDevice(deviceID);
		} catch (MalformedURLException e) {
			logger.error(" AndroidDriver 创建失败\n", e);
		} catch (WebDriverException e) {
			logger.error(" AppiumService 启动失败 \n", e);
		}

		return locator;
	}

	/**
	 * 测试 iOS真机 时，需要获取的 IOSDriver
	 *
	 * @param deviceConfig 设备和测试配置信息
	 *
	 * @return IOSLocator {@link com.appium.locator.IOSLocator}
	 *
	 * @throws IOException 异常
	 *
	 */
	public IOSLocator<MobileElement> getIOSLocator(Map<String, String> deviceConfig) throws IOException {
		IOSConfig ios = new IOSConfig();
		String udid = deviceConfig.get(MobileCapabilityType.UDID);
		// 设置自动化参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//iOS 设备名
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ios.getDeviceName(udid));
		//可以不需要填写系统平台，因为已采用了 iOSdriver 默认使用 iOS 平台
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		// iOS 系统版本号
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, ios.getDeviceOSVersion(udid));
		// ***.app编译后的绝对路径,或 ipa 文件绝对路径。PS:模拟器需 app 绝对路径，真机可使用 ipa 路径
		capabilities.setCapability(MobileCapabilityType.APP, FileUtils.getAbsolutePath(deviceConfig.get(MobileCapabilityType.APP)));
		//被测应用的包名
		capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, deviceConfig.get(IOSMobileCapabilityType.BUNDLE_ID));
		//使用真机
		capabilities.setCapability(MobileCapabilityType.UDID, udid);
		//确认是否需要重新安装被测应用
		if (ios.checkAppIsInstalled(udid, deviceConfig.get(IOSMobileCapabilityType.BUNDLE_ID))) {
			//需要重新安装应用
			//capabilities.setCapability(MobileCapabilityType.FULL_RESET, Boolean.TRUE);
		} else {
			//不需要重新安装
			capabilities.setCapability(MobileCapabilityType.NO_RESET, Boolean.TRUE);
		}
		//iOS 的自动化测试引擎
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, ios.getAutomatorName(udid));
		//如果测试引擎为XCUI，就需要设置以下参数
		if (AutomationName.IOS_XCUI_TEST.equalsIgnoreCase(ios.getAutomatorName(udid))) {
			capabilities.setCapability(IOSMobileCapabilityType.XCODE_CONFIG_FILE, config.getProperty(ConfigManager.XCODE_SETTING_PATH));
			capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
		}
		//appium 命令启动超时时间
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Integer.parseInt(config.getProperty(ConfigManager.COMMAND_TIME_OUT)));

		logger.info("the capability: " + capabilities.toString());

		IOSDeviceInfo deviceInfo = new IOSDeviceInfo(capabilities);
		IOSDriver<MobileElement> driver;
		IOSLocator<MobileElement> locator = null;
		//启动 Appium 服务
		appiumDriverLocalService = appiumManager.startAppiumServerForIOS(capabilities);
		//创建 IOSDriver
		try {
			logger.info("开始创建 IOSDriver");
			driver = new IOSDriver<>(new URL(appiumManager.getAppiumUrl().toString()), capabilities);
			locator = new IOSLocator<>(driver);
			locator.setAppiumDriverLocalService(appiumDriverLocalService);
			locator.setDeviceInfo(deviceInfo);
			logger.info(" IOSDriver 创建成功");
		} catch (WebDriverException e) {
			logger.error(" AppiumService 启动失败\n", e);
		} catch (MalformedURLException e) {
			logger.error(" IOSDriver 创建失败\n", e);
		}
		return locator;
	}

	/**
	 * 测试 iOS 模拟器时，需要获取的 IOSDriver。
	 * 此方法紧为了调试而设
	 *
	 * @return IOSLocator {@link com.appium.locator.IOSLocator}
	 *
	 * @throws IOException 异常
	 *
	 */
	public IOSLocator<MobileElement> getIOSSimLocator() throws IOException {
		// 设置自动化参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//iOS 设备名
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6s");
		//可以不需要填写系统平台，因为已采用了 iOSdriver 默认使用 iOS 平台
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		// iOS 系统版本号
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10.3");
		// ***.app编译后的绝对路径,或 ipa 文件绝对路径。PS:模拟器需 app 绝对路径，真机可使用 ipa 路径
		capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir") + "/src/test/resources/UICatalog.app.zip");
		//被测应用的包名
		capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID,"com.example.apple-samplecode.UICatalog");
		//使用模拟器，设置模拟器语言，但会重启1-2次模拟器，启动时间稍长
		capabilities.setCapability(MobileCapabilityType.LANGUAGE, "zh-CN");
		capabilities.setCapability(MobileCapabilityType.UDID, "");
		//每次运行需重新安装应用
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, Boolean.TRUE);
		//iOS 的自动化测试引擎
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		//如果测试引擎为XCUI，就需要设置以下参数
		capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, Boolean.TRUE);
		//appium 命令启动超时时间
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Integer.parseInt(config.getProperty(ConfigManager.COMMAND_TIME_OUT)));

		logger.info("the capability: " + capabilities.toString());
		IOSDriver<MobileElement> driver;
		IOSLocator<MobileElement> locator = null;
		//启动 Appium 服务
		appiumDriverLocalService = appiumManager.startAppiumServerForIOS(capabilities);
		//创建 IOSDriver
		try {
			logger.info("开始创建 IOSDriver");
			driver = new IOSDriver<>(new URL(appiumManager.getAppiumUrl().toString()), capabilities);
			locator = new IOSLocator<>(driver);
			locator.setAppiumDriverLocalService(appiumDriverLocalService);
			logger.info("IOSDriver 创建成功");
		} catch (WebDriverException e) {
			logger.error("AppiumService 启动失败\n", e);
		} catch (MalformedURLException e) {
			logger.error("IOSDriver 创建失败\n", e);
		}
		return locator;
	}

	/**
	 * 获取当前运行的 Appium 服务
	 *
	 * @return AppiumDriverLocalService {@link io.appium.java_client.service.local.AppiumDriverLocalService}
	 */
	public AppiumDriverLocalService getAppiumDriverLocalService() {
		return appiumDriverLocalService;
	}
}

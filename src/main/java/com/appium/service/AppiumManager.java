package com.appium.service;

import com.framework.utils.HttpUtils;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.framework.utils.ConfigManager;
import com.framework.utils.DateTimeUtils;
import com.framework.utils.FileUtils;

/**
 * 用于启动和关闭 Appium 服务:
 * 为了可以正常启动 Appium服务，请在终端上使用命令 ‘open /Applications/Eclipse.app’启动 Eclipse
 * 
 */

public class AppiumManager {

	Logger logger = LogManager.getLogger(this.getClass());
    private HttpUtils ap = new HttpUtils();
    public AppiumDriverLocalService appiumDriverLocalService;
    public AppiumServiceBuilder builder = new AppiumServiceBuilder();
    private ConfigManager config;


    public AppiumManager() throws IOException {
        config = ConfigManager.getInstance();
    }

    /**
     * 使用自动生成的可用端口号开启 Appium 服务,测试安卓设备
     * 
     * @param capabilities {@link org.openqa.selenium.remote.DesiredCapabilities}
     * 
     * @return AppiumServiceBuilder {@link io.appium.java_client.service.local.AppiumServiceBuilder}
     *
     * @throws IOException io 异常
     *
     */
    public AppiumDriverLocalService startAppiumServerForAndroid(DesiredCapabilities capabilities)
    		throws IOException {
    	int port = ap.getPort();
        int chromePort = ap.getPort();
        int selendroidPort = ap.getPort();
        String deviceID = (String)capabilities.getCapability(MobileCapabilityType.UDID);
        String deviceName = (String)capabilities.getCapability(MobileCapabilityType.DEVICE_NAME);
        String logName = ConfigManager.getLogPath() + deviceName.replaceAll("\\W", "_") + "-"
        		+ deviceID.replaceAll("\\W", "_") + "__" + DateTimeUtils.getFileDateTime().replaceAll("\\W", "_") + ".txt";
        FileUtils.createFile(logName);
        
        AppiumServiceBuilder builder =
            new AppiumServiceBuilder().withAppiumJS(new File(config.getProperty(ConfigManager.APPIUM_JS_PATH)))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withLogFile(new File(logName))
                .withArgument(AndroidServerFlag.CHROME_DRIVER_PORT, Integer.toString(chromePort))
                .withArgument(AndroidServerFlag.SELENDROID_PORT, Integer.toString(selendroidPort))
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(AndroidServerFlag.SUPPRESS_ADB_KILL_SERVER)
                .withCapabilities(capabilities)
                .withIPAddress(ap.getLocalHost())
                .usingPort(port);
        /* 可以继续添加其他参数 */

        appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        
        logger.info("启动 Appium 服务用于测试安卓设备：" + deviceName + "-" + deviceID);
        return appiumDriverLocalService;
    }

    /**
     * start appium with auto generated ports : appium port, chrome port,
     * bootstrap port and device UDID
     */
    ServerArgument webKitProxy = new ServerArgument() {
        @Override
        public String getArgument() {
            return "--webkit-debug-proxy-port";
        }
    };

    /**
     * 使用自动生成的可用端口号开启 Appium 服务，测试 iOS 设备
     * 
     * @param capabilities {@link org.openqa.selenium.remote.DesiredCapabilities}
     *
     * @return {@link io.appium.java_client.service.local.AppiumServiceBuilder}
     *
     * @throws IOException IO异常
     */
    public AppiumDriverLocalService startAppiumServerForIOS
            (DesiredCapabilities capabilities) throws IOException {
    	String udid = (String)capabilities.getCapability(MobileCapabilityType.UDID);
    	String deviceName = (String)capabilities.getCapability(MobileCapabilityType.DEVICE_NAME);
        int port = ap.getPort();

        String logName = ConfigManager.getLogPath() + deviceName.replaceAll("\\W", "_") + "-"
        		+ udid.replaceAll("\\W", "_") + "__" + DateTimeUtils.getFileDateTime().replaceAll("\\W", "_") + ".txt";
        FileUtils.createFile(logName);
        
        AppiumServiceBuilder builder =
            new AppiumServiceBuilder().withAppiumJS(new File(config.getProperty(ConfigManager.APPIUM_JS_PATH)))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withLogFile(new File(logName))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "debug")
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE)
                .withCapabilities(capabilities)
                .withIPAddress(ap.getLocalHost())
                .usingPort(port);
        appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        
        logger.info("启动 Appium 服务用于测试iOS设备：" + deviceName + "-" + udid);
        return appiumDriverLocalService;
    }

    public URL getAppiumUrl() {
        return appiumDriverLocalService.getUrl();
    }

    /**
     * 停止 AppiumServer 服务
     *
     */
    public void stopAppiumServer() {
        appiumDriverLocalService.stop();
        if (appiumDriverLocalService.isRunning()) {
            logger.warn("Appium 服务暂未关闭成功，正在尝试重新关闭");
            appiumDriverLocalService.stop();
        }
    }
}

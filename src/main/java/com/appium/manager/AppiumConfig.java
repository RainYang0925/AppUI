package com.appium.manager;

import java.util.List;
import java.util.Map;

/**
 * Appium 总体配置文件，但不可生成实例
 *
 * @version 1.0
 *
 */
public interface AppiumConfig {

    Map<String, String> getDevicesInfo();

    List<String> getDeviceUDID();

    String getDeviceName(String udid);

    String getPlatformName();

    String getDeviceOSVersion(String udid);

    String getAutomatorName(String udid);

    boolean checkDeviceIsConnected(String udid);

    void installApp(String udid, String appPath);

    void uninstallApp(String udid, String bundleID);

    boolean checkAppIsInstalled(String udid, String bundleID);
    
    void rebootDevice(String udid);
    
    String getDeviceLog(String udid);
}

package com.appium.manager.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.appium.manager.AppiumConfig;
import com.framework.executor.ProcessCommand;

/**
 * Android adb 操作类，可通过 adb 命令进行操作
 * 
 * @version 1.1
 * @since 2016.12.26
 * 
 * */

public class AndroidConfig implements AppiumConfig {

	Logger logger = LogManager.getLogger(this.getClass());
	ProcessCommand cmd = new ProcessCommand();
	private String platformName = MobilePlatform.ANDROID;

	/**
	 * 开启 adb 服务
	 * 
	 * */
    public void startADB() {
        String output = null;
		try {
			output = cmd.runCommand("adb start-server");
			logger.info("开启 adb 服务");
		} catch (IOException e) {
			logger.catching(e);
		}
        String[] lines = output.split("\n");
        if (lines[0].contains("internal or external command")) {
        	logger.error("请在系统设置 ANDROID_HOME 环境变量");
        }
    }
	
	/**
	 * 关闭 adb 服务
	 * 
	 * */
    public void stopADB() {
        try {
			cmd.runCommand("adb kill-server");
			logger.info("关闭 adb 服务");
		} catch (IOException e) {
			 logger.catching(e);
		}
    }
    
	/**
	 * 获取设备信息：设备名、设备ID等
	 * 
	 * @return 设备信息
	 * 
	 * */
	@Override
    public Map<String, String> getDevicesInfo() {
    	Map<String, String> devices = new HashMap<String, String>();
        this.startADB(); // 开启 adb 服务
        List<String> deviceUDIDs = getDeviceUDID();
        if (deviceUDIDs.size() == 0) {
			logger.warn("暂无连接设备");
			this.stopADB();
		} else {
            for (int i = 0; i < deviceUDIDs.size(); i++) {
                String deviceID = deviceUDIDs.get(i);
                devices.put("deviceID" + i, deviceID);
                //设备名称
                devices.put("deviceName" + i, this.getDeviceName(deviceID));
                //设备系统版本号
                devices.put("osVersion" + i, this.getDeviceOSVersion(deviceID));
                //SDK版本号
                devices.put(deviceID, this.getDeviceSDKVersion(deviceID));
            }
            logger.info("当前已连接的设备信息：" + devices.toString());
		}
        return devices;
    }
    
	/**
	 * 获取设备号(udid)
	 * 
	 * @return 设备号
	 * 
	 * */
	@Override
    public List<String> getDeviceUDID() {
    	ArrayList<String> deviceSerail = new ArrayList<String>();
        this.startADB();
        String output;
		try {
			output = cmd.runCommand("adb devices");
	        String[] lines = output.split("\n");
	        
	        if (lines.length <= 1) {
	        	logger.warn("暂无连接设备");
	            return null;
	        } else {
	        	String udidForLog = "";
	        	int linesLength = lines.length;
	            for (int i = 1; i < linesLength; i++) {
	                lines[i] = lines[i].replaceAll("\\s+", "");

	                if (lines[i].contains("device")) {
	                    lines[i] = lines[i].replaceAll("device", "");
	                    String deviceID = lines[i];
	                    deviceSerail.add(deviceID);
	                    udidForLog = udidForLog + "[" + lines[i] + "]";
	                    if (i != linesLength - 1) {
	                    	udidForLog = udidForLog + ", ";
						}
	                } else if (lines[i].contains("unauthorized")) {
	                    lines[i] = lines[i].replaceAll("unauthorized", "");
	                    String deviceID = lines[i];
	                } else if (lines[i].contains("offline")) {
	                    lines[i] = lines[i].replaceAll("offline", "");
	                    String deviceID = lines[i];
	                }
	            }
	            logger.info("获取到已连接的设备：" + udidForLog);
	        }
		} catch (IOException e) {
			logger.catching(e);
		}
		return deviceSerail;
    }

    /**
     * 根据系统平台和系统版本获取 Appium 测试引擎
     *
     * @return 测试引擎名称
     *
     * */
    @Override
    public String getAutomatorName(String deviceID) {
        String platformVersion = this.getDeviceOSVersion(deviceID);
        String automatorName;
        double version = Double.parseDouble(platformVersion.substring(0, 3));
        if (version < 4.2) {
            automatorName = AutomationName.SELENDROID;
        } else if (version < 5.0) {
            automatorName = AutomationName.APPIUM;
        } else {
            automatorName = AutomationName.ANDROID_UIAUTOMATOR2;
        }
        return automatorName;
    }

	/**
	 * 获取指定设备的名称
	 * 
	 * @param deviceID 设备号
	 * 
	 * @return 设备名称
	 * 
	 * */
    @Override
    public String getDeviceName(String deviceID) {
    	String deviceName = null;
        try {
			//设备型号
			String model = cmd.runCommand("adb -s " + deviceID + " shell getprop ro.product.model")
					.replaceAll("\\s+", "");
			//设备品牌
			String brand = cmd.runCommand("adb -s " + deviceID + " shell getprop ro.product.brand")
					.replaceAll("\\s+", "");
			deviceName = brand + "_" + model;
			logger.info("获取到设备 [" + deviceID + "] 的名称：[" + deviceName + "]");
		} catch (IOException e) {
			logger.catching(e);
		}
		return deviceName;
    }

    /**
     * 获取系统平台名称
     *
     * @return 系统平台名称:iOS
     *
     * */
    @Override
    public String getPlatformName() {
        return platformName;
    }

    /**
     * 获取设备的系统版本号
     * 
     * @param deviceID 设备号
     * 
     * @return 设备系统版本号
     * 
     * */
    @Override
    public String getDeviceOSVersion(String deviceID) {
    	String deviceOSVersion = null;
    	try {
    		String command = "adb -s " + deviceID + " shell getprop ro.build.version.release";
			deviceOSVersion = cmd.runCommand(command).replaceAll("\\s+", "");
			logger.info("获取到设备 [" + deviceID + "] 的系统版本号为 [" + deviceOSVersion + "]");
		} catch (IOException e) {
			logger.catching(e);
		}
    	return deviceOSVersion;
    }
    
    /**
     * 获取设备的SDK版本号
     * 
     * @param deviceID 设备号
     * 
     * @return 设备SDK版本号
     * 
     * */
    public String getDeviceSDKVersion(String deviceID) {
    	String deviceSDKVersion = null;
    	try {
    		String command = "adb -s " + deviceID + " shell getprop ro.build.version.sdk";
			deviceSDKVersion = cmd.runCommand(command).replaceAll("\n", "");
			logger.info("获取到设备 [" + deviceID + "] 的SDK版本号为 [" + deviceSDKVersion + "]");
		} catch (IOException e) {
			logger.catching(e);
		}
    	return deviceSDKVersion;
    }

    /**
     * 检查指定的设备是否已连接
     *
     * @param deviceID 设备号
     *
     * @return true or false
     *
     * */
    @Override
    public boolean checkDeviceIsConnected(String deviceID) {
        for (String id : this.getDeviceUDID()) {
            if (deviceID.equalsIgnoreCase(id)) {
                logger.info("设备 [" + deviceID + "] 已连接");
                return true;
            }
        }
        logger.info("设备 [" + deviceID + "] 尚未连接");
        return false;
    }

    /**
     * 安装指定应用
     * 
     * @param deviceID 设备号
     * @param appPath 应用绝对路径
     * 
     * */
    @Override
    public void installApp(String deviceID, String appPath) {
    	String command = "adb -s " + deviceID + " install " + appPath;
    	String[] path = appPath.split(File.separator);
    	String app = path[path.length - 1];
        try {
			cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 安装 [" + app + "] 应用成功");
		} catch (IOException e) {
			logger.catching(e);
		}
    }
    
    /**
     * 检查应用是否已经安装
     * 
     * @param deviceID 设备号
     * @param app_package 指定应用
     * 
     * @return true or false
     * 
     * */
    @Override
    public boolean checkAppIsInstalled(String deviceID, String app_package) {
    	String command = "adb -s " + deviceID + " shell pm list package";
    	boolean isInstalled = false;
        try {
        	
			String packages = cmd.runCommand(command);
			if (packages.contains(app_package)) {
				isInstalled = true;
				logger.info("在设备 [" + deviceID + "] 上，[" + app_package + "] 应用已安装");
			} else {
				isInstalled = false;
				logger.info("在设备 [" + deviceID + "] 上，[" + app_package + "] 应用尚未安装");
			}
		} catch (IOException e) {
			logger.catching(e);
			return false;
		}
        return isInstalled;
    }
    
    /**
     * 关闭运行的 app
     * 
     * @param deviceID 设备号
     * @param app_package 指定应用
     * 
     * */
    public void closeApp(String deviceID, String app_package) {
    	String command = "adb -s " + deviceID + " shell am force-stop " + app_package;
        try {
			cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 关闭 [" + app_package + "] 应用成功");
		} catch (IOException e) {
			logger.catching(e);
		}
    }

    /**
	 * 启动指定应用的指定页面
	 *
	 * @param deviceID 设备号
	 * @param app_package 指定应用
	 * @param start_Activity 指定页面
	 *
	 * */
    public void launchApp(String deviceID, String app_package, String start_Activity) {
    	String command = "adb -s " + deviceID + " shell am start -n " + app_package + "/" + start_Activity;
    	try {
    		cmd.runCommand(command);
    		logger.info("在设备 [" + deviceID + "] 启动 [" + app_package + "] 应用的 [" + start_Activity + "] 页面成功");
		} catch (IOException e) {
    		logger.catching(e);
		}
	}

    /**
     * 清除指定应用的数据
     * 
     * @param deviceID 设备号
     * @param app_package 指定应用
     * 
     * */
    public void clearAppData(String deviceID, String app_package) {
    	String command = "adb -s " + deviceID + " shell pm clear " + app_package;
        try {
			cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 清除 [" + app_package + "] 应用数据成功");
		} catch (IOException e) {
			logger.catching(e);
		}
    }

    /**
     * 卸载指定应用
     * 
     * @param deviceID 设备号
     * @param app_package 指定应用
     * 
     * */
    @Override
    public void uninstallApp(String deviceID, String app_package) {
    	String command = "adb -s " + deviceID + " uninstall " + app_package;
        try {
			cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 卸载 [" + app_package + "] 应用成功");
		} catch (IOException e) {
			logger.catching(e);
		}
    }
    
	/**
	 * 获取设备的输入法
	 * 
	 * @param deviceID 设备号
	 * 
	 * @return Android 设备默认输入法
	 * 
	 * */
	public String getDefaultInputMethod(String deviceID) {
		String default_input_method = null;
		String command = "adb -s " + deviceID + " shell settings get secure default_input_method";
		try {
			default_input_method = cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 获取到默认输入法：" + default_input_method);
		} catch (IOException e) {
			logger.error("在设备 [" + deviceID + "] 获取输入法出现错误\n", e);
		}
		return default_input_method;
	}
	
	/**
	 * 设置设备的输入法
	 * 
	 * @param deviceID 设备号
	 * @param input_method 需要设置的输入法名称
	 * 
	 * */
	public void setInputMethod(String deviceID, String input_method) {
		String command = "adb -s " + deviceID + " shell settings put secure default_input_method " + input_method;
		try {
			cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 设置 " + input_method + " 为默认输入法");
		} catch (IOException e) {
			logger.error("在设备 [" + deviceID + "] 设置默认输入法出现错误\n", e);
		}
	}
	
	/**
	 * 获取指定设备的日志
	 * 
	 * @param deviceID 设备号
	 *
	 * @return 日志详情
	 */
	@Override
	public String getDeviceLog(String deviceID) {
		String command = "adb -s " + deviceID + " logcat -d";
		String log = null;
		try {
			log = cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 获取日志成功");
		} catch (Exception e) {
			logger.error("在设备 [" + deviceID + "] 获取日志出现错误\n", e);
		}
		return log;
	}
	
	/**
	 * 重启指定设备
	 * 
	 * @param deviceID 设备号
	 * 
	 */
	@Override
	@Deprecated
	public void rebootDevice(String deviceID) {
		String command = "adb -s " + deviceID + " reboot";
		try {
			cmd.runCommand(command);
			logger.info("在设备 [" + deviceID + "] 重启成功");
		} catch (IOException e) {
			logger.error("在设备 [" + deviceID + "] 重启失败\n", e);
		}
	}
}

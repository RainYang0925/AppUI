package com.appium.manager.ios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.framework.utils.ConfigManager;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.appium.manager.AppiumConfig;
import com.framework.executor.ProcessCommand;
import com.framework.utils.StringUtils;

/**
 * ios 操作类，可通过 ideviceinstaller 或 ios-deploy 命令进行操作
 * 
 * @version 1.0
 */

public class IOSConfig implements AppiumConfig {

	Logger logger = LogManager.getLogger(this.getClass());
	ProcessCommand cmd = new ProcessCommand();
	private String platformName = MobilePlatform.IOS;
	private final ConfigManager config;
	public HashMap<String, String> deviceMap = new HashMap <>();
	public Process p;
	public Process p1;

	public static ConcurrentHashMap<Long, Integer> appiumServerProcess = new ConcurrentHashMap<>();


	public IOSConfig() {
		config = ConfigManager.getInstance();
	}

	/**
	 * 获取当前已连接的设备 udid
	 * 
	 * @return 设备标识符的集合
	 */
    @Override
    public List<String> getDeviceUDID() {
    	List<String> devices = new ArrayList<>();
        try {
            String getIOSDeviceID = cmd.runCommand("ios-deploy -c --no-wifi");
            if (StringUtils.isEmpty(getIOSDeviceID)) {
            	logger.warn("暂无连接设备");
                return null;
            } else {
                String[] lines = getIOSDeviceID.split("\n");
                int linesLength = lines.length;
                String udidForLog = "";
                for (int i = 0; i < lines.length; i++) {
                	if (lines[i].contains("connected")) {
						String[] step1 = lines[i].split("\\(");
						String[] step2 = step1[step1.length - 1].split("\\)");
						String id = step2[0];
						devices.add(id);
						udidForLog = udidForLog + "[" + id + "]";
					}
                    if (i != linesLength - 1) {
                    	udidForLog = udidForLog + ", ";
					}
                }
                logger.info("获取到已连接的设备：" + udidForLog);
                return devices;
            }
        } catch (IOException e) {
            logger.catching(e);
            return null;
        }
    }

	/**
	 * did 来获取设备信息（name，版本，id 等）
	 *
	 * @return 设备信息
	 */
	@Override
	public Map<String, String> getDevicesInfo() {
		Map<String, String> devices = new HashMap<>();
		List<String> udids = this.getDeviceUDID();
		if (udids.size() > 0) {
			for (int i = 0; i < udids.size(); i++) {
				String deviceID = udids.get(i);
				devices.put("deviceID" + i, deviceID);
				devices.put("deviceName" + i, this.getDeviceName(deviceID));
				devices.put("osVersion" + i, this.getDeviceOSVersion(deviceID));
				devices.put(deviceID, this.getDeviceProductType(deviceID));
			}
			logger.info("当前已连接的设备信息：" + devices.toString());
		} else {
			logger.warn("当前无设备连接");
		}
		return devices;
	}
	
	/**
	 * 获取指定设备的名称
	 * 
	 * @param udid 设备标识符
	 * 
	 * @return 设备名称
	 */
	@Override
    public String getDeviceName(String udid) {
		//这个命令也可以 "ideviceinfo --udid yourUDID -k DeviceName"
        String deviceName = "iPhone";//默认名字不为空
		try {
			deviceName = cmd.runCommand("idevicename --udid " + udid).replace("\\W", "_").replaceAll("\n", "");
			logger.info("获取到设备 [" + udid + "] 的名称：[" + deviceName + "]");
		} catch (IOException e) {
			logger.catching(e);
		}
        return deviceName;
    }

	/**
	 * 获取系统平台名称
	 *
	 * @return 系统平台名称:iOS
	 */
	@Override
    public String getPlatformName() {
		return platformName;
	}

	/**
     * 获取设备的系统版本号
     * 
     * @param udid 设备标识符
     * 
     * @return 设备版本号
     */
    public String getDeviceOSVersion(String udid) {
		//这个命令也可以 "ideviceinfo --udid yourUDID -k ProductVersion"
    	String command = "ideviceinfo --udid " + udid + " | grep ProductVersion";
    	String deviceOSVersion = null;
    	try {
			deviceOSVersion = cmd.runCommandThruProcessBuilder(command);
			logger.info("获取到设备 [" + udid + "] 的系统版本号为 [" + deviceOSVersion + "]");
		} catch (IOException e) {
			logger.catching(e);
		}
    	return deviceOSVersion;
    }
    
    /**
     * 获取设备的产品类型，如：iPhone6,2 --(iPhone 5s)
     * 
     * @param udid 设备标识符
     * 
     * @return 设备版本号*
     */
    public String getDeviceProductType(String udid) {
    	//这个命令也可以 "ideviceinfo --udid yourUDID -k ProductType"
    	String command = "ideviceinfo --udid " + udid + " | grep ProductType";
    	String deviceProductType = null;
    	try {
    		deviceProductType = cmd.runCommandThruProcessBuilder(command);
			logger.info("获取到设备 [" + udid + "] 的产品类型为 [" + deviceProductType + "]");
		} catch (IOException e) {
			logger.catching(e);
		}
    	return deviceProductType;
	}

	/**
	 * 根据系统平台和系统版本获取 Appium 测试引擎
	 *
	 * @return 测试引擎名称
	 */
	@Override
    public String getAutomatorName(String udid) {
		String automatorName;
		String platformVersion = this.getDeviceOSVersion(udid);
		double version = Double.parseDouble(platformVersion.substring(0, 3));
		if (version < 10) {		//10以下的系统都默认使用 UIAutomation
			automatorName = AutomationName.APPIUM;
		} else {
			automatorName = AutomationName.IOS_XCUI_TEST;
		}
		return automatorName;
	}

    /**
     * 检查指定的设备是否已连接
     * 
     * @param udid 设备标识符
     * 
     * @return true or false
     */
    @Override
    public boolean checkDeviceIsConnected(String udid) {
        for (String id : this.getDeviceUDID()) {
            if (udid.equalsIgnoreCase(id)) {
                logger.info("设备 [" + udid + "] 已连接");
                return true;
            }
        }
        logger.info("设备 [" + udid + "] 尚未连接");
        return false;
    }
	
	/**
	 * 获取指定设备的日志
	 * 
	 * @param udid 设备号
	 *
	 * @return 日志详情
	 */
    @Override
	public void getDeviceLog(String udid, String logFile) {
		String command = "idevicesyslog -u " + udid;
		String log = null;
		try {
			log = cmd.runCommand(command);
			logger.info("在设备 [" + udid + "] 获取日志成功");
		} catch (Exception e) {
			logger.error("在设备 [" + udid + "] 获取日志出现错误\n", e);
		}
	}
    
	/**
	 * 重启指定设备
	 * 
	 * @param udid 设备号
	 */
    @Override
	public void rebootDevice(String udid) {
		String command = "idevicediagnostics --udid " + udid + " restart";
		try {
			cmd.runCommand(command);
			logger.info("在设备 [" + udid + "] 重启成功");
		} catch (IOException e) {
			logger.error("在设备 [" + udid + "] 重启失败\n", e);
		}
	}
    
    /**
     * 安装应用
     * 
     * @param udid 设备标识符
     * @param appPath .ipa 安装包路径
     * 
     */
    @Override
    public void installApp(String udid, String appPath) {
    	String command = "ideviceinstaller --udid " + udid + " --install " + appPath;
    	String[] path = appPath.split(File.separator);
    	String app = path[path.length - 1];
        try {
			cmd.runCommand(command);
			logger.info("在设备 [" + udid +"] 安装 [" + app + "] 应用成功");
		} catch (IOException e) {
			logger.catching(e);
		}
    }

    /**
     * 卸载应用
     * 
     * @param udid 设备标识符
     * @param bundleID .ipa 安装包的 Bundle ID（可以看作包名）
     * 
     */
    @Override
    public void uninstallApp(String udid, String bundleID) {
    	String command = "ios-deploy --no-wifi --id " + udid + " --uninstall_only --bundle_id " + bundleID;
        try {
			cmd.runCommand(command);
			logger.info("在设备 [" + udid +"] 卸载 [" + bundleID + "] 应用成功");
		} catch (IOException e) {
			logger.catching(e);
		}
    }
    
    /**
     * 检查应用是否已经安装
     * 
     * @param bundleID 应用包名
     * 
     * @return true or false
     */
    @Override
    public boolean checkAppIsInstalled(String udid, String bundleID) {
        boolean appAlreadyExists = false;
		try {
			if (cmd.runCommand("ios-deploy --no-wifi --id " + udid + " --exists --bundle_id " + bundleID).contains("true")) {
				appAlreadyExists = true;
				logger.info("[" + bundleID + "] 应用已安装");
			} else {
				appAlreadyExists = false;
				logger.info("[" + bundleID + "] 应用尚未安装");
			}
		} catch (IOException e) {
			logger.catching(e);
		}
        return appAlreadyExists;
    }
/*
	public HashMap<String, String> setIOSWebKitProxyPorts(String udid) throws Exception {
		try {
			int webkitproxyport = new HttpUtils().getPort();
			deviceMap.put(udid, Integer.toString(webkitproxyport));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deviceMap;
	}

	public String startIOSWebKit(String udid) throws IOException, InterruptedException {
		String serverPath = config.getProperty(ConfigManager.APPIUM_JS_PATH);
		File file = new File(serverPath);
		File curentPath = new File(file.getParent());
		System.out.println(curentPath);
		file = new File(curentPath + "/.." + "/..");
		String ios_web_lit_proxy_runner =
				file.getCanonicalPath() + "/bin/ios-webkit-debug-proxy-launcher.js";
		String webkitRunner =
				ios_web_lit_proxy_runner + " -c " + udid + ":" + deviceMap.get(udid) + " -d";
		System.out.println(webkitRunner);
		p1 = Runtime.getRuntime().exec(webkitRunner);
		System.out.println(
				"WebKit Proxy is started on device " + udid + " and with port number " + deviceMap
						.get(udid) + " and in thread " + Thread.currentThread().getId());
		//Add the Process ID to hashMap, which would be needed to kill IOSwebProxywhen required
		appiumServerProcess.put(Thread.currentThread().getId(), getPid(p1));
		System.out.println("Process ID's:" + appiumServerProcess);
		Thread.sleep(1000);
		return deviceMap.get(udid);
	}

	public long getPidOfProcess(Process p) {
		long pid = -1;

		try {
			if (p1.getClass().getName().equals("java.lang.UNIXProcess")) {
				Field f = p1.getClass().getDeclaredField("pid");
				f.setAccessible(true);
				pid = f.getLong(p1);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			pid = -1;
		}
		return pid;
	}

	public int getPid(Process process) {

		try {
			Class<?> cProcessImpl = process.getClass();
			Field fPid = cProcessImpl.getDeclaredField("pid");
			if (!fPid.isAccessible()) {
				fPid.setAccessible(true);
			}
			return fPid.getInt(process);
		} catch (Exception e) {
			return -1;
		}
	}

	public void destroyIOSWebKitProxy() throws IOException, InterruptedException {
		Thread.sleep(3000);
		if (appiumServerProcess.get(Thread.currentThread().getId()) != -1) {
			String process = "pgrep -P " + appiumServerProcess.get(Thread.currentThread().getId());
			Process p2 = Runtime.getRuntime().exec(process);
			BufferedReader r = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String command = "kill -9 " + r.readLine();
			System.out.println("Kills webkit proxy");
			System.out.println("******************" + command);
			Runtime.getRuntime().exec(command);
		}
	}

	public void checkExecutePermissionForIOSDebugProxyLauncher() throws IOException {
		String serverPath = config.getProperty(ConfigManager.APPIUM_JS_PATH);
		File file = new File(serverPath);
		File curentPath = new File(file.getParent());
		System.out.println(curentPath);
		file = new File(curentPath + "/.." + "/..");
		File executePermission =
				new File(file.getCanonicalPath() + "/bin/ios-webkit-debug-proxy-launcher.js");
		if (executePermission.exists()) {
			if (executePermission.canExecute() == false) {
				executePermission.setExecutable(true);
				System.out.println("Access Granted for iOSWebKitProxyLauncher");
			} else {
				System.out.println("iOSWebKitProxyLauncher File already has access to execute");
			}
		}
	}*/

}

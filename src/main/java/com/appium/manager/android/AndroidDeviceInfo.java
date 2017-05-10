package com.appium.manager.android;

import com.appium.manager.DeviceInfo;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;

/**
 * Android 设备信息
 * 
 * @version 1.0
 */
public class AndroidDeviceInfo implements DeviceInfo {

	private String name;
	private String deviceID;
	private String system;
	private String version;
	private String packageName;
	private String startActivity;
	private String automationName;
	
	public AndroidDeviceInfo(Capabilities capabilities) {
		this.deviceID = (String)capabilities.getCapability(MobileCapabilityType.UDID);
		this.system = (String)capabilities.getCapability(MobileCapabilityType.PLATFORM_NAME);
		this.name = (String)capabilities.getCapability(MobileCapabilityType.DEVICE_NAME);
		this.version = (String)capabilities.getCapability(MobileCapabilityType.PLATFORM_VERSION);
		this.automationName = (String)capabilities.getCapability(MobileCapabilityType.AUTOMATION_NAME);
		this.packageName = (String)capabilities.getCapability(AndroidMobileCapabilityType.APP_PACKAGE);
		this.startActivity = (String)capabilities.getCapability(AndroidMobileCapabilityType.APP_ACTIVITY);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDeviceID() {
		return deviceID;
	}

	@Override
	public String getSystem() {
		return system;
	}

	@Override
	public String getVersion() {
		return version;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public String getStartActivity() {
		return startActivity;
	}

	@Override
	public String getAutomationName() {
		return automationName;
	}
	
	@Override
	public String toString() {
		return "deviceID-" + deviceID + ",\n" +
				"deviceName-" + name + ",\n" +
				"platformName-" + system + ",\n" +
				"OSVersion-" + version + ",\n" +
				"default_input_method-" + packageName + ",\n" +
				"automationName-" + automationName;
	}
	
}

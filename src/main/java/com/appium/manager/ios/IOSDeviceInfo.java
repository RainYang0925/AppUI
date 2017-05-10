package com.appium.manager.ios;

import com.appium.manager.DeviceInfo;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;

/**
 * IOS设备信息
 *
 * @version 1.0
 *
 */

public class IOSDeviceInfo implements DeviceInfo {
	
	private String name;
	private String udid;
	private String system;
	private String version;
	private String automationName;
	private String bundleId;
	
	public IOSDeviceInfo(Capabilities capabilities) {
		this.udid = (String)capabilities.getCapability(MobileCapabilityType.UDID);
		this.system = (String)capabilities.getCapability(MobileCapabilityType.PLATFORM_NAME);
		this.name = (String)capabilities.getCapability(MobileCapabilityType.DEVICE_NAME);
		this.version = (String)capabilities.getCapability(MobileCapabilityType.PLATFORM_VERSION);
		this.bundleId = (String)capabilities.getCapability(IOSMobileCapabilityType.BUNDLE_ID);
		this.automationName = (String)capabilities.getCapability(MobileCapabilityType.AUTOMATION_NAME);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDeviceID() {
		return udid;
	}

	@Override
	public String getSystem() {
		return system;
	}

	@Override
	public String getVersion() {
		return version;
	}

	public String getBundleId() {
		return bundleId;
	}

	@Override
	public String getAutomationName() {
		return automationName;
	}
	
	@Override
	public String toString() {
		return "udid-" + udid + ",\n" +
				"deviceName-" + name + ",\n" +
				"platformName-" + system + ",\n" +
				"OSVersion-" + version + ",\n" +
				"automationName-" + automationName;
	}
	
}

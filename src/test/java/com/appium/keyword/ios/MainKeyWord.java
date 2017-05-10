package com.appium.keyword.ios;

import com.appium.keyword.IOSKeyWord;
import com.appium.locator.AppLocator;

import io.appium.java_client.MobileElement;

public class MainKeyWord extends IOSKeyWord {

	public MainKeyWord(AppLocator<MobileElement> locator) {
		super.locator = locator;
		caseSheet = "主流程";
	}

}

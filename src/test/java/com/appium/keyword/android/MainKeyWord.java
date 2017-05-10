package com.appium.keyword.android;

import com.appium.keyword.AndroidKeyWord;
import com.appium.locator.AndroidLocator;
import com.appium.locator.Locator;
import io.appium.java_client.MobileElement;

public class MainKeyWord extends AndroidKeyWord {
	
	public MainKeyWord(Locator<MobileElement> locator) {
		this.locator = (AndroidLocator<MobileElement>)locator;
		caseSheet = "主页";
	}
	
}

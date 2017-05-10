package com.appium.keyword;

import com.appium.element.SwipeElement;
import com.appium.element.Swipes;
import com.appium.locator.IOSLocator;
import com.framework.utils.SleepUtils;
import io.appium.java_client.MobileElement;

/**
 * iOS App关键字框架基类，实现一些常用的操作
 * 
 * @version 1.1
 * 
 */

public abstract class IOSKeyWord extends BaseKeyWord {

    /**
     * 将元素向左滑动
     *
     */
	public void swipeELementLeft() {
        SwipeElement swipeIOS = super.getElement().swipeIOS();
        swipeIOS.swipeElementLeft();
    }

    /**
     * 页面进行一次往上的滑动
     *
     */
    public void swipeToUp() {
        Swipes swipes = new Swipes(locator);
        swipes.swipeToUp();
    }

    /**
     * 页面进行一次往下的滑动
     *
     */
    public void swipeToDown() {
        Swipes swipes = new Swipes(locator);
        swipes.swipeToDown();
    }

    //在权限弹窗中，点击允许按钮
    public void alertAccept() {
        IOSLocator<MobileElement> iosLocator = (IOSLocator<MobileElement>)locator;
        iosLocator.alert_accept_click();
    }

    //在权限弹窗中，点击不允许按钮
    public void alertDismiss() {
        IOSLocator<MobileElement> iosLocator = (IOSLocator<MobileElement>)locator;
        iosLocator.alert_dismiss_click();
    }

    //搜索框输入
    public void searchInput() {
        SleepUtils.threadSleepBySeconds(2);
        locator.getDriver().findElement(elementBy).setValue(testData);
    }
}

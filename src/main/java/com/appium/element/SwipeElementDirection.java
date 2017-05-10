package com.appium.element;

import com.appium.locator.AppLocator;
import io.appium.java_client.IllegalCoordinatesException;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/**
 * 元素四个方向的滑动方法
 *
 */
public enum SwipeElementDirection {

    UP {
        @Override
        int getStartX(Point center, Point location, Dimension size, int offSet) {
            return center.getX();
        }

        @Override
        int getStartY(Point center, Point location, Dimension size, int offSet) {
            int startY = location.getY() + size.getHeight() - offSet;
            checkYCoordinate(startY, location, size, offSet);
            return startY;
        }

        @Override
        int getEndX(Point center, Point location, Dimension size, int offSet) {
            return center.getX();
        }

        @Override
        int getEndY(Point center, Point location, Dimension size, int offSet) {
            int endY = location.getY() + offSet;
            checkYCoordinate(endY, location, size, offSet);
            return endY;
        }

        @Override
        void checkDirection(int x1, int y1, int x2, int y2) {
            if (y1 < y2) {
                throw new IllegalCoordinatesException("向上滑动的起点 [" + y1 + "] 小于结束点 [" + y2 + "]。这看起来是执行向下滑动。");
            }
        }
    },

    DOWN{
        @Override
        int getStartX(Point center, Point location, Dimension size, int offSet) {
            return center.getX();
        }

        @Override
        int getStartY(Point center, Point location, Dimension size, int offSet) {
            int startY = location.getY() + offSet;
            checkYCoordinate(startY, location, size, offSet);
            return startY;
        }

        @Override
        int getEndX(Point center, Point location, Dimension size, int offSet) {
            return center.getX();
        }

        @Override
        int getEndY(Point center, Point location, Dimension size, int offSet) {
            int endY = location.getY() + size.getHeight() - offSet;
            checkYCoordinate(endY, location, size, offSet);
            return endY;
        }

        @Override
        void checkDirection(int x1, int y1, int x2, int y2) {
            if (y1 > y2) {
                throw new IllegalCoordinatesException("向下滑动的起点 [" + y1 + "] 大于结束点 [" + y2 + "]。这看起来是执行向上滑动。");
            }
        }
    },

    LEFT{
        @Override
        int getStartX(Point center, Point location, Dimension size, int offSet) {
            int startX = location.getX() + size.getWidth() - offSet;
            checkXCoordinate(startX, location, size, offSet);
            return startX;
        }

        @Override
        int getStartY(Point center, Point location, Dimension size, int offSet) {
            return center.getY();
        }

        @Override
        int getEndX(Point center, Point location, Dimension size, int offSet) {
            int endX = location.getX() + offSet;
            checkXCoordinate(endX, location, size ,offSet);
            return endX;
        }

        @Override
        int getEndY(Point center, Point location, Dimension size, int offSet) {
            return center.getY();
        }

        @Override
        void checkDirection(int x1, int y1, int x2, int y2) {
            if (x1 < x2) {
                throw new IllegalCoordinatesException("向左滑动的起点 [" + x1 + "] 小于结束点 [" + x2 + "]。这看起来是执行向有滑动。");
            }
        }
    },

    RIGHT{
        @Override
        int getStartX(Point center, Point location, Dimension size, int offSet) {
            int startX = location.getX() + offSet;
            checkXCoordinate(startX, location, size ,offSet);
            return startX;
        }

        @Override
        int getStartY(Point center, Point location, Dimension size, int offSet) {
            return center.getY();
        }

        @Override
        int getEndX(Point center, Point location, Dimension size, int offSet) {
            int endX = location.getX() + size.getWidth() - offSet;
            checkXCoordinate(endX, location, size, offSet);
            return endX;
        }

        @Override
        int getEndY(Point center, Point location, Dimension size, int offSet) {
            return center.getY();
        }

        @Override
        void checkDirection(int x1, int y1, int x2, int y2) {
            if (x1 > x2) {
                throw new IllegalCoordinatesException("向右滑动的起点 [" + x1 + "] 小于结束点 [" + x2 + "]。这看起来是执行向左滑动。");
            }
        }
    };

    static void checkYCoordinate(int y, Point location, Dimension size, int offSet)
            throws IllegalCoordinatesException {
        int bottom = location.getY() + size.getHeight();
        int top = location.getY();
        //y 低于底部，报错
        if (y > bottom) {
            throw new IllegalCoordinatesException(
                    "Y点 [" + y + "] 低于元素的底部 [" + bottom + "]");
        }
        //y高于顶部，报错
        if (y < top) {
            throw new IllegalCoordinatesException(
                    "Y点 [" + y + "] 高于元素的顶部 [" + top + "]");
        }

    }

    static void checkXCoordinate(int x, Point location, Dimension size, int offSet)
            throws IllegalCoordinatesException {
        int right = location.getX() + size.getWidth();
        int left = location.getX();
        //x 大于右边，报错
        if (x > right) {
            throw new IllegalCoordinatesException(
                    "X点 [" + x + "] 大于元素的右边 [" + right + "]");
        }
        //x 小于左边，报错
        if (x < left) {
            throw new IllegalCoordinatesException(
                    "X点 [" + x + "] 小于元素的右边 [" + left + "]");
        }
    }

    abstract int getStartX(Point center, Point location, Dimension size, int offSet);

    abstract int getStartY(Point center, Point location, Dimension size, int offSet);

    abstract int getEndX(Point center, Point location, Dimension size, int offSet);

    abstract int getEndY(Point center, Point location, Dimension size, int offSet);

    abstract void checkDirection(int x1, int y1, int x2, int y2);

    /**
     * 增加滑动方法，在 MyElement{@link MyElement} 调用<br>
     * 支持：Android
     * 不支持：iOS(与 IOSLocator 的 swipe 方法坐标冲突)
     * @param locator AppiumDriver{@link com.appium.locator.AppLocator}
     * @param element 滑动的元素
     * @param offset1 元素开始滑动时，距离边缘的间隔
     * @param offset2 元素结束滑动时，距离边缘的间隔
     * @param duration 滑动时间，单位：ms
     *
     * @throws IllegalCoordinatesException 起止的坐标超过元素的边缘，就会报错
     */
    public void swipe(AppLocator<? extends MobileElement> locator, MyElement element, int offset1, int offset2,
                      int duration) throws IllegalCoordinatesException {
        Point center = element.getCenter();
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int startX = getStartX(center, location, size, offset1);
        int startY = getStartY(center, location, size, offset1);
        int endX = getEndX(center, location, size, offset2);
        int endY = getEndY(center, location, size, offset2);
        checkDirection(startX, startY, endX, endY);

        locator.swipe(startX, startY, endX, endY, duration);
    }

}
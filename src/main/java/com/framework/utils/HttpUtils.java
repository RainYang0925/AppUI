package com.framework.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * 网络工厂类
 *
 */
public class HttpUtils {

	/**
	 * 随机获取当前系统可用的端口号，用于启动 appium 服务
	 * 
	 * @return 端口号
	 *
     * @throws IOException IO异常
	 */
    public int getPort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        socket.setReuseAddress(true);
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }

    /**
     * 获取本机地址
     *
     * @return 本机地址
     *
     * @throws UnknownHostException 未知地址异常
     */
    public String getLocalHost() throws UnknownHostException {
        InetAddress ia = InetAddress.getLocalHost();
        return ia.getHostAddress();
    }
}

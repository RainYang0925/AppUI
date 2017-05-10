package com.framework.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令工厂类
 *
 * @version 1.0
 *
 */

public class ProcessCommand {

    static Process p;
    ProcessBuilder builder;

    /**
     * 在终端上运行命令，用于 Android 和 iOS 获取信息
     *
     * @param command 运行命令
     *
     * @return 执行命令后，所产生的日志
     *
     * @throws IOException IO异常
     */
    public String runCommand(String command) throws IOException {
        p = Runtime.getRuntime().exec(command);
        //命令执行后，获得输出日志
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        String allLine = "";
        while ((line = r.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
            if (line.contains("Console LogLevel: debug") && line.contains("Complete")) {
                break;
            }
        }
        return allLine;
    }

    /**
     * 终端命令执行后，产生的日志
     *
     * @param command 执行的命令
     *
     * @return 命令产生的日志
     *
     * @throws IOException IO 异常
     */
    public String runCommandThruProcessBuilder(String command) throws IOException {
        BufferedReader br = getBufferedReader(command);
        String line;
        String allLine = "";
        while ((line = br.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
            //System.out.println(allLine);
        }
        return allLine.split(":")[1].replace("\n", "").trim();
    }

    /**
     * 终端获取设备 ID
     *
     * @param command 命令
     *
     * @return 获取的设备 ID 信息
     *
     * @throws IOException IO 异常
     */
    public String runProcessCommandToGetDeviceID(String command) throws IOException {
        BufferedReader br = getBufferedReader(command);
        String line;
        String allLine = "";
        while ((line = br.readLine()) != null) {
            allLine = allLine.trim() + "" + line.trim() + "\n";
            //System.out.println(allLine);
        }
        return allLine.trim();
    }

    public BufferedReader getBufferedReader(String command) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(command);
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.environment();

        final Process process = builder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    public void runCommandThruProcess(String command) throws IOException {
        BufferedReader br = getBufferedReader(command);
        String line;
        String allLine = "";
        while ((line = br.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
            //System.out.println(allLine);
        }
    }

    /**
     * 将 Appium 的日志以字符串的形式输出
     *
     * @param command 终端命令
     *
     * @return Appium 服务产生的日志
     *
     * @throws IOException IO异常
     */
    public String runProcessCommandToRunAppium(String command) throws IOException {
        BufferedReader br = getBufferedReader(command);
        String line = null;
        String allLine = "";
        while ((line = br.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
            //System.err.println(allLine);
        }
        return allLine;
	}
}

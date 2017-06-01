package com.framework.utils;

import com.framework.executor.ProcessCommand;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 获取版本操作类
 *
 * @version 1.0
 *
 */

public class EnvironmentVersions {

	/**
	 * 使用命令获取 node 版本号
	 *
	 * @return node 版本号
	 */
	public String getNodeVersion() {
		ProcessCommand cmd = new ProcessCommand();
		try {
			String nodeVersion = cmd.runCommand("node -v");
			return nodeVersion.replaceAll("v", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用命令获取 Appium server 版本号
	 *
	 * @return Appium server 版本号
	 */
	public String getAppiumVersion() {
		ProcessCommand cmd = new ProcessCommand();
		try {
			return cmd.runCommand("appium -v");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据依赖库的组织名，在 pom.xml，获取其版本号
	 *
	 * @param groupID 依赖库的组织名
	 *
	 * @return 依赖库的版本
	 */
	public String getVersionForPOM(String groupID) {
		//创建SAXReader对象
		SAXReader reader = new SAXReader();
		//读取文件 转换成Document
		Document document = null;
		try {
			document = reader.read(new File("pom.xml"));
		} catch (DocumentException e) {
			return null;
		}
		//获取根节点元素对象
		Element root = document.getRootElement();
		//获取 pom.xml 文件中依赖库所有的信息
		List<Element> libs = root.element("dependencies").elements("dependency");
		String version = null;
		for (int i = 0; i < libs.size(); i ++) {
			if (libs.get(i).element("groupId").getText().contains(groupID)) {
				version = libs.get(i).element("version").getText();
			}
		}
		return version;
	}
}

AppUI for Appium 环境配置
===
以下环境配置均基于 Mac OS 系统配置


### 1、安装 Java-Mac OS X 最新版本
先安装 Apple 提供的 Java 6 runtime 版本，才能驱动从Oracle 下载的Java 7，Java 8 <br>
Apple 官方的 Java 最新版本可到这里下载：https://support.apple.com/kb/DL1572 <br>
Oracle 的 Java 最新版本可到这里下载：http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

以上2步均成功安装后，在配置文件添加 Java_home，在终端使用 `vim ~/.bash_profile`，即可打开该用户当前的配置文件。
填写
`JAVA_HOME=$(/usr/libexec/java_home)
export JAVA_HOME
PATH=$PATH:$JAVA_HOME/bin
export PATH`
即可完成配置，可不用受到以后升级 java 版本，而重新配置 java-home 的困扰。


### 2、安装 android-sdk
推荐使用命令`brew install android-sdk`安装，直接安装到`/usr/local/Cellar/android-sdk`,且环境配置也自动安装好。

常见问题：
brew 命令出现错误，解决方法，重新安装即可。<br>
在终端执行此命令即可：`/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`


###3、安装 IDE 开发环境
可使用 Eclipse 和 IDEA，看个人喜好<br>
  Eclipse需另外安装 Android 插件（ADT23.0.7）,点击这里下载：https://dl.google.com/android/ADT-23.0.7.zip <br>
  IDEA在安装过程中，勾选 Android 插件，即可完成安装，无须另外再安装。<br>


###4、Maven
使用以上 IDE 集成的 Maven 版本即可，可不用另外再次安装


###5、安装 node.JS
终端，执行命令`brew install node`安装即可，如需安装指定版本，将 node 替换成 node@6或 node@4即可
检查 node 是否安装完成，指定命令，`node -v`,输出结果有版本信息，则 node 安装正常


###6、安装 iOS 测试环境
为了可以正常操作设备，需安装`libimobiledevice`库<br>
`brew install libimobiledevice --HEAD  # 最新的更新版本`<br>
`brew install ideviceinstaller         # 仅支持 iOS 9，如需支持 iOS 10，使用第一个命令`

由于Appium 测试 ios，使用的是 facebook 的 WebDriverAgent 框架，需安装以下依赖库<br>
`brew install carthage`

由于`ideviceinstaller`未支持 iOS 10，需安装 ios-deploy<br>
`npm install -g ios-deploy`

测试真机时，需要从 xcode 获取更新的日志信息，需安装<br>
`gem install xcpretty`


###7、安装 appium-doctor
终端，执行命令`npm install -g appium-doctor`安装即可，终端上，输入`appium-doctor`检查环境配置情况，如全部打勾，即为通过。


###8、安装 Appium
安装使用`npm install -g appium --no-shrinkwrap`，安装最新版本的 Appium，各模块也是最新的，ios 测试使用 usb 协议，可正常运行。
安装完成后，执行命令`appium -v`，输出版本信息，即安装成功。


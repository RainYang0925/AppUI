开始测试App
===

### Excel 文档
1、在你的脚本 maven 项目根目录中，新建一个 test-datas 目录放置 Excel 测试文档；<br/>
2、根据本项目中 test-datas 目录中的 Excel 模板样式，新建测试 Excel 文档；<br/>

### KeyWord代码的编写
1、新建一个类，继承 AppUI 中的 AndroidKeyWord 类；<br/>
2、其他页面的 KeyWord 类，继承上述所新建的 KeyWord 类；<br/>
3、在 Excel 文档中，填写测试流程中元素的信息、测试页面等；<br/>
4、新建测试类，测试你的KeyWord 类即可。<br/>
PS: 详细情况，请参考项目的测试例子。<br/>

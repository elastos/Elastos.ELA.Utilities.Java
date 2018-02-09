#ela_tool 签名程序(java)

## 打包步骤
File -> Project Structure -> Artifacts -> + -> JAR -> From modules with 
1、选择执行的类路径 Main Class
2、选择 copy to the output directory and link via manifest
3、选择 META-INF创建的路径 （C:\DNA\src\ela_tool\src\main\resources）
4、ok -> 选择 Include in project build -> Apply ->ok

在JAR包的目录执行以下命令，避免出现无效的签名文件异常信息
zip -d ela_tool.jar 'META-INF/*.SF' 'META-INF/*.RSA' 'META-INF/*SF'


## 运行TestCase之前
1、需要确定 config.py 、deploy.sh 、wallet.sh 和 JAR包在同一级目录下 
2、配置 deploy.sh ： MAIN_PATH=$GOPATH/src/ela_tool/src/main/java 的路径，此路径是 config.py和wallet.sh的路径


## web服务
web服务的类路径 ：org\elastos\elaweb\HttpServer.java
启动命令 : java -cp ela_tool.jar  org.elastos.elaweb.HttpServer

## TestCase
发送RawTransaction类路径 ：org\elastos\framework\testCase\SendRawTransaction.java
启动命令 : java -cp ela_tool.jar  org.elastos.framework.testCase.SendRawTransaction

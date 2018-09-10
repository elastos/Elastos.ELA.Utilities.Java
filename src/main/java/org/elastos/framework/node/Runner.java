package org.elastos.framework.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author: DongLei.Tan
 * @contact: tandonglei@elastos.org
 * @time: 2018/1/22
 */
public class Runner  {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static final List<HashMap> nodeList = new ArrayList<HashMap>();

    public static final String GOPATH                = System.getenv("GOPATH");
    public static final String MAINPATH              = GOPATH + "/src/ela_tool/";
    public static final String NODESPATH             = MAINPATH + "src/SendRawTx_newrpc/java/nodes/";



    //部署或重置节点
    public static void runNodes()  throws Exception{

        LOGGER.info("[ >>>>> Start nodes <<<<< ]");
        String[] cmd = {MAINPATH + "src/main/java/deploy.sh", "run"};
        Runtime.getRuntime().exec(cmd);
    }

    //停止节点
    public static void stopNodes() throws Exception{

        LOGGER.info("[ >>>>> Stop nodes <<<<< ]");
        String[] cmd = {MAINPATH + "src/main/java/deploy.sh", "stop"};
        Process process = Runtime.getRuntime().exec(cmd);
        //取得命令结果的输出流
        InputStream inputStream = process.getInputStream();
        //用一个读输出流类去读
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        //用缓冲器读行
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null ;
        while((line = bufferedReader.readLine())!=null)
        {
            System.out.println(line);
        }
        int status = process.waitFor();
        if (status != 0) {
            LOGGER.error("Failed to call shell's command and the return status's is: {}",status);
        }
    }

    //停止节点
    public static void cleanChain() throws Exception{

        LOGGER.info("[ >>>>> cleanChain <<<<< ]");

        String[] cmd = {MAINPATH + "src/main/java/deploy.sh", "clean"};
        Process process = Runtime.getRuntime().exec(cmd);
        //取得命令结果的输出流
        InputStream inputStream = process.getInputStream();
        //用一个读输出流类去读
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        //用缓冲器读行
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null ;
        while((line = bufferedReader.readLine())!=null)
        {
            System.out.println(line);
        }
        int status = process.waitFor();
        if (status != 0) {
            LOGGER.error("Failed to call shell's command and the return status's is: {}",status);
        }
    }

    //重启节点
    public static void restartNodes()throws Exception{

        LOGGER.info("[ >>>>> Restart nodes <<<<< ]");

        //TODO
        //ResetWallets()
        String[] cmd = {MAINPATH + "src/main/java/deploy.sh", "restart"};
        Process process = Runtime.getRuntime().exec(cmd);
        //取得命令结果的输出流
        InputStream inputStream = process.getInputStream();
        //用一个读输出流类去读
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        //用缓冲器读行
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null ;
        while((line = bufferedReader.readLine())!=null)
        {
            System.out.println(line);
        }

        int status = process.waitFor();
        if (status != 0) {
            LOGGER.error("Failed to call shell's command and the return status's is: {}",status);
        }
    }
}

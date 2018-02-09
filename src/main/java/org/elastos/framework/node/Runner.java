package org.elastos.framework.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mdj17 on 2018/1/23.
 */
public class Runner  {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    private static final HashMap<String, String> httpUrlMap = new HashMap<String, String>();
    public static final List<HashMap> nodeList = new ArrayList<HashMap>();

    public static final String NET_TYPE         = "net_type";         //网络类型
    public static final String MINING           = "mining" ;          //是否开启挖矿
    public static final String CLEAN_BEFORE_RUN = "clean_before_run"; //执行测试用例之前清除数据

    public static final String GOPATH                = System.getenv("GOPATH");
    public static final String MAINPATH              = GOPATH + "/src/ela_tool/";
    public static final String NODESPATH             = MAINPATH + "src/SendRawTransaction/java/nodes/";
    public static final String ADDRESSESPATH         = NODESPATH + "addresses.bak";
    public static final String PUBLICKEYSPATH        = NODESPATH + "public_keys.bak";



    public static void setupCase(int nodeNum) throws Exception{
        for (int i = 1 ; i <= nodeNum ; i++){
            httpUrlMap.put("nodePath" + i , Node.getNodePath(i));
            httpUrlMap.put("rpc" + i ,Node.getRPCAddress(i));
            httpUrlMap.put("restful" + i,Node.getRestfulAddress(i));
            nodeList.add(httpUrlMap);

        }
        deployOrReset(nodeNum);
    }

    //部署或重置节点
    public static void deployOrReset(int nodeNum)  throws Exception{
        LOGGER.info("[ >>>>> Start nodes <<<<< ]");

        String[] cmd = {"./deploy.sh", "run", String.valueOf(nodeNum)};
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
    public static void stopNodes() throws Exception{
        LOGGER.info("[ >>>>> Stop nodes <<<<< ]");

        String[] cmd = {"./deploy.sh", "stop"};
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

    public static void restartNodes()throws Exception{
        LOGGER.info("[ >>>>> Restart nodes <<<<< ]");

        //TODO
        //ResetWallets()
        String[] cmd = {"./deploy.sh", "restart"};
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

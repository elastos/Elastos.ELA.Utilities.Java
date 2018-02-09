package org.elastos.framework.wallet;

import org.elastos.framework.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mdj17 on 2018/2/7.
 */
public class Wallet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wallet.class);
    public static final String WALLET_PASSWORD         = "elatest";


    //重置钱包
    public static void resetWallet(int index) throws Exception {
        for (int i = 1; i <= index; i++) {
            LOGGER.info("[ >>>>>  Reset Wallet node{} <<<<< ]" ,i);

            String[] cmd = {"./wallet.sh", "resetwallet", Node.getNodePath(index), WALLET_PASSWORD};
            Process process = Runtime.getRuntime().exec(cmd);
            //取得命令结果的输出流
            InputStream inputStream = process.getInputStream();
            //用一个读输出流类去读
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            //用缓冲器读行
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            int status = process.waitFor();
            if (status != 0) {
                LOGGER.error("Failed to call shell's command and the return status's is: {}", status);
            }
        }
    }
}

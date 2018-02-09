package org.elastos.framework.testCase;

import org.elastos.ela.Ela;
import org.elastos.ela.RawTx;
import org.elastos.ela.TxOutput;
import org.elastos.ela.UTXOTxInput;
import org.elastos.elaweb.ElaController;
import org.elastos.framework.node.Runner;
import org.elastos.framework.restful.Restful;
import org.elastos.framework.rpc.Rpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 1、启动1个节点
 * 2、第一个节点挖矿，并给生成的地址转账
 * 3、查询生成地址余额是否正确
 *
 * Created by mdj17 on 2018/1/22.
 */
public class SendRawTransaction {
    public static final String TXURL = "http://127.0.0.1:40101/api/v1/transaction";
    private static final Logger LOGGER = LoggerFactory.getLogger(SendRawTransaction.class);
    private static final  int nodeNumber = 1 ;

    public static void main(String[] args) throws Exception{
        //启动节点，指定启动几个节点
        Runner.setupCase(nodeNumber);
        LOGGER.info("sleep 2 秒");
        Thread.sleep(2 * 1000);

        //挖矿,指定挖矿数量
        for (int j = 0 ; j < nodeNumber ; j++) {
            String rpcUrl = (String) Runner.nodeList.get(j).get("rpc1");
            String blockHash = Rpc.generateBlock(200, rpcUrl);
            LOGGER.info("Mining Url = {} , Block Hash = {}",rpcUrl , blockHash);
        }

        LOGGER.info("sleep 5 秒");
        Thread.sleep(5 * 1000);

        Integer height = Restful.getBestHeight(1);
        LOGGER.info("Height = {}" ,height);

        String url = (String) Runner.nodeList.get(0).get("rpc1");

        int count = 10 ;
        LinkedList<Map> linkedList = new LinkedList<Map>();
        for (int k = 0 ;k < count ; k++ ) {
            Map<String ,String> map = new HashMap<String, String>();
            String privateKey = Ela.getPrivateKey();
            String address = Ela.getAddressFromPrivate(privateKey);

            String Txid = Rpc.sendTransaction(address, "0.001", "0.00001", url);
            LOGGER.info("Create Transaction , Address = {} , TxId = {} ",address, Txid);

            map.put("privateKey",privateKey);
            map.put("address",address);
            map.put("Txid",Txid);
            linkedList.add(map);

            Rpc.generateBlock(1, url);
            Thread.sleep(1 * 1000);
        }

        //创建RawTranscation
        //创建inputs
        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < count ; i++){
            String privateKey = (String) linkedList.get(i).get("privateKey");
            String address = (String) linkedList.get(i).get("address");
            String Txid = (String) linkedList.get(i).get("Txid");
            inputList.add(new UTXOTxInput(Txid,0,privateKey,address));
        }
        //创建outputs
        List<TxOutput>  outputList = new LinkedList<TxOutput>();
        for (int j = 0 ; j < count ; j ++){
            String outputAddress = Ela.getAddressFromPrivate(Ela.getPrivateKey());
            outputList.add(new TxOutput(outputAddress,1));
        }
        RawTx rawTx = Ela.makeAndSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]),outputList.toArray(new TxOutput[outputList.size()]));

        //发送RawTransaction
        String TxHash = ElaController.sendRawTransaction(rawTx.getRawTxString(),TXURL);
        LOGGER.info("SendRawTransaction : {} " , TxHash);

        //关闭节点
        Runner.stopNodes();
    }
}
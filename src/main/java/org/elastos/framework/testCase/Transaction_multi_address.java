package org.elastos.framework.testCase;

import net.sf.json.JSONObject;
import org.elastos.api.SingleSignTransaction;
import org.elastos.ela.*;
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
 * 2、第一个节点挖矿，并转账 count 次，生成count 数的TXId（多个地址的一个uxto）
 * 3、创建 count 数量的input和 output,进行打包交易
 * 4、矿工地址给多个地址转账（一个地址多个uxto给多个地址转账）
 *
 * @author: DongLei.Tan
 * @contact: tandonglei@elastos.org
 * @time: 2018/1/22
 */
public class Transaction_multi_address {

    private static final String PATH         = "http://127.0.0.1";
    private static final String RPCPORT      = "10336";
    private static final String RESTFULLPORT = "10334";
    private static final Logger LOGGER      = LoggerFactory.getLogger(Transaction_multi_address.class);
    private static final  int miningNumber  = 200 ;

    private static final  String MINERPRIVATE   = "71279C61A62A54FC399BF06BC70DE331E581BD07D7C1E583C147140F5F2411FF" ;
    private static final  String MINERPUBLIC    = "0248825ED47895390FE1E9076E32665ED64BE6B879B1ECDBAB9666A8BE8B2CA349" ;
    private static final  String MINERADDRESS    = "EMwyeVwvtfNoHGf5VcJoZnf9pzWRN7jbef" ;

    public static void main(String[] args) throws Exception{
//        启动节点，指定启动几个节点
        Runner.runNodes();
        LOGGER.info("sleep 2 秒");
        Thread.sleep(2 * 1000);

        //挖矿,指定挖矿数量
        String rpcUrl = Rpc.getRpcAddress(PATH,RPCPORT);
        String blockHash = Rpc.generateBlock(miningNumber, rpcUrl);
        LOGGER.info("Mining Url = {} , Block Hash = {}",rpcUrl , blockHash);

        LOGGER.info("sleep 5 秒");
        Thread.sleep(5 * 1000);

        String restfulUrl = Restful.getRestfulAddress(PATH,RESTFULLPORT);
        Integer height = Restful.getBestHeight(restfulUrl);
        LOGGER.info("Height = {}" ,height);

        int count = 20 ;
        //花费矿工余额,创建多个地址一个UTXO
        LinkedList<Map> linkedList = new LinkedList<Map>();
        for (int k = 2 ;k < count ; k++ ) {
            LinkedList<UTXOTxInput> inputList_miner = new LinkedList<UTXOTxInput>();
            JSONObject jsonObject = HttpRequestUtil.httpGet(restfulUrl + Restful.api.API_GETBLOCKBYHEIGHT + String.valueOf(k));
            JSONObject result       = (JSONObject) jsonObject.get("Result");
            JSONObject transactions = (JSONObject)result.getJSONArray("tx").get(0);
            String Txid             = transactions.get("txid").toString();
            inputList_miner.add(new UTXOTxInput(Txid,1,MINERPRIVATE,MINERADDRESS));

            List<TxOutput>  outputList_target = new LinkedList<TxOutput>();
            String private_target = Ela.getPrivateKey();
            String outputAddress_target    = Ela.getAddressFromPrivate(private_target);
            outputList_target.add(new TxOutput(outputAddress_target,1000000));
            RawTx rawTx = Ela.makeAndSignTx(inputList_miner.toArray(new UTXOTxInput[inputList_miner.size()]),outputList_target.toArray(new TxOutput[outputList_target.size()]));

            //发送RawTransaction
            String TxHash = SingleSignTransaction.sendRawTransaction(rawTx.getRawTxString(),rpcUrl);
            JSONObject jsonTxHash = JSONObject.fromObject(TxHash);
            String txhash = (String)jsonTxHash.get("result");
            System.out.println("txhash = " + txhash);
            LOGGER.info("SendRawTx_newrpc 1 : {} , Count :{} " , txhash ,  k - 1 );

            Rpc.generateBlock(1, rpcUrl);
            Thread.sleep(1 * 1000);

            Map<String ,String> map = new HashMap<String, String>();
            map.put("privateKey",private_target);
            map.put("address",outputAddress_target);
            map.put("Txid",txhash);
            linkedList.add(map);
        }

        //创建RawTranscation
        //创建inputs
        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < linkedList.size() ; i++){
            String privateKey = (String) linkedList.get(i).get("privateKey");
            String address = (String) linkedList.get(i).get("address");
            String Txid = (String) linkedList.get(i).get("Txid");
            inputList.add(new UTXOTxInput(Txid,0,privateKey,address));
        }
        //创建outputs
        List<TxOutput>  outputList = new LinkedList<TxOutput>();
        for (int j = 0 ; j < count ; j ++){
            String outputAddress = Ela.getAddressFromPrivate(Ela.getPrivateKey());
            outputList.add(new TxOutput(outputAddress,100000 ));
        }
        //多个地址的一个utxo给多个地址转账
        RawTx rawTx = Ela.makeAndSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]),outputList.toArray(new TxOutput[outputList.size()]));

        //发送RawTransaction
        String TxHash = SingleSignTransaction.sendRawTransaction(rawTx.getRawTxString(),rpcUrl);
        LOGGER.info("SendRawTx_newrpc 2 : {} " , TxHash);

        Rpc.generateBlock(1, rpcUrl);

        //关闭节点
        Runner.stopNodes();
        Runner.cleanChain();
    }
}


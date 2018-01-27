package org.elastos.ela;

import net.sf.json.JSONObject;
import org.elastos.ela.bitcoinj.Utils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by nan on 18/1/15.
 */
public class ElaTest {
    public static final String TXURL = "http://127.0.0.1:20334/api/v1/transaction";
    private static final Logger LOGGER = LoggerFactory.getLogger(ElaTest.class);


    @Test
    public void makeAndSignTx() throws Exception {
        List<UTXOTxInput> inputs = new LinkedList<UTXOTxInput>();
        inputs.add(new UTXOTxInput(
                "878ea9293a3cf2fd350adfea51cd84a03853b1c56d900f2e0d046d9b7869ac04",
                1,
                "C1DCA64C32B6F79FCABE35219818342625471AF884DCF6EE705AAFB13B1FFF7F",
                "EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZz"));
        inputs.add(new UTXOTxInput(
                "9e0bbbeab388fa03519ae62c434737b277b5f33755c52e69db9a16f623e7a198",
                0,
                "CAEBF4C512E95C70D244590556602E68A59DFE2BC681B5605FB1E670CD64BE2B",
                "ESpnr4ZtYLMGtoYJekKqzQLiqp99kP3ra2"));
        inputs.add(new UTXOTxInput(
                "9e0bbbeab388fa03519ae62c434737b277b5f33755c52e69db9a16f623e7a198",
                1,
                "EEA28B1643B74AB7715BAB61163617F5298284DEB1F31FB696084534214AE9FE",
                "EUbrh4R1eBaq9rwxWJEwLwoJcjB3cV3teL"));

        List<TxOutput>  outputs = new LinkedList<TxOutput>();
        outputs.add(new TxOutput("ENwa2vtBC1QcU3hYUhAtPuik9HkHQzcgi1",2000000));
        outputs.add(new TxOutput("EasafsSR9e253mQfc5qdYWwFuAhWQWicgB",2200000));

        String rawTx = Ela.makeAndSignTx(inputs.toArray(new UTXOTxInput[0]),outputs.toArray(new TxOutput[0]));
        System.out.println("rawTx:"+rawTx);

        sendRawTransaction(rawTx);
    }

    @Test
    public void revers(){
        byte[] hex = DatatypeConverter.parseHexBinary("23830ee492956816e4fcaf2bfb726fa7b2fe1e644861bca9b9c66e632d5b237e");
        System.out.println(DatatypeConverter.printHexBinary(Utils.reverseBytes(hex)).toLowerCase());
    }

    /**
     * 生成私钥、公钥、地址
     */
    @Test
    public void generateBasic() throws Exception{

        //生成私钥
        String privateKey = Ela.getPrivateKey();
        System.out.println("Private key = " + privateKey);

        //生成公钥
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        System.out.println("Public Key = " + publicKey);

        //生成地址
        String address = Ela.getAddressFromPrivate(privateKey);
        System.out.println("Address = " + address);
    }

    /**
     * 发送Rawtransaction
     * @param rawTx
     */
    public void sendRawTransaction(String rawTx){
        //构造json格式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Action","sendrawtransaction");
        map.put("Version","1.0.0");
        map.put("Type",2);
        map.put("Data",rawTx);

        //发送RowTransaction
        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        JSONObject responseJSONObject = HttpRequestUtil.httpPost(TXURL, jsonParam);
        System.out.println("Send Row Transaction Returning Data = "+ responseJSONObject);
        if("SUCCESS".equals(responseJSONObject.get("Desc"))) {
            System.out.println("Send Row Transaction Success");
        }else {
            System.out.println("Send Row Transaction process fail");
        }
    }
}
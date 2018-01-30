package org.elastos.ela;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.bitcoinj.Utils;
import org.elastos.elaweb.ElaController;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by nan on 18/1/15.
 */
public class ElaTest {

    /**
     * 处理 ELA类的方法
     * @throws Exception
     */
    @Test
    public void  processMethod() throws Exception{
        String filePath   = "C:/DNA/src/ela_tool/src/test/java/org/elastos/ela/transaction";

        FileInputStream inputStream = new FileInputStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        JSONObject jsonObject = JSONObject.fromObject(bufferedReader.readLine());
        JSONArray jsonArray = jsonObject.getJSONArray("params");
        String method = jsonObject.getString("method");
        System.out.println("method :" + method);
        if (jsonArray.size() == 0) {
            if (method.equals("getPrivateKey") ) {
                System.out.println(ElaController.genPrivateKey());
            }
            if (method.equals("get_priv_put_addr")) {
                System.out.println(ElaController.gen_priv_pub_addr());
            }
        }
        if (jsonArray.size() != 0){
            JSONObject param = (JSONObject)jsonArray.get(0);
            if (method.equals("getPublicKey")){
                String privateKey = param.getString("PrivateKey");
                System.out.println(ElaController.genPublicKey(privateKey));
            }
            if (method.equals("getAddress")){
                String privateKey = param.getString("PrivateKey");
                System.out.println(ElaController.genAddress(privateKey));
            }
            if (method.equals("getRawTransaction")){
                System.out.println(ElaController.genRawTransaction(param));
            }
        }
        inputStream.close();
        bufferedReader.close();
    }


    /**
     * 签名
     * @throws Exception
     */
    @Test
    public void makeAndSignTx() throws Exception {
        List<UTXOTxInput> inputs = new LinkedList<UTXOTxInput>();
        inputs.add(new UTXOTxInput(
                "c79f9a951f1aa987bdb1920e42dffe180741543ccd9c8fbbf1987c4db6d5fcd5",
                0,
                "5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB",
                "EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"));
        inputs.add(new UTXOTxInput(
                "7513e197da5f91d1f4eadc797d307e61bbcc155b83346bbb9ae79626b7426766",
                0,
                "A65E9FB6735C5FD33F839036B15D2DA373E15AED38054B69386E322C6BE52994",
                "EgSph8GNaNSMwpv6UseAihsAc5sqSrA7ga"));
        inputs.add(new UTXOTxInput(
                "22bade15481f1af8240993207e1df61144a7776e6087994d240917a887f72052",
                0,
                "4C573939323F11BCDB57B61CCE095D4B1E55E986F9944F88072141F3DFA883A3",
                "Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"));

        List<TxOutput>  outputs = new LinkedList<TxOutput>();
        outputs.add(new TxOutput("ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt",20));
        outputs.add(new TxOutput("EKjeZEmLSXyyJ42xxjJP4QsKJYWwEXabuC",30));

        RawTx rawTx = Ela.makeAndSignTx(inputs.toArray(new UTXOTxInput[inputs.size()]),outputs.toArray(new TxOutput[outputs.size()]));
        System.out.println("rawTx:"+rawTx.getRawTxString());
        System.out.println("txHash:"+rawTx.getTxHash());
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
}
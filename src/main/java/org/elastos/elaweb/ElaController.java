package org.elastos.elaweb;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.elastos.ela.api.Verify;
import org.elastos.wallet.Account;
import org.elastos.wallet.KeystoreFile;
import org.elastos.wallet.WalletMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei@elastos.org
 * @time: 2018/5/20
 */
public class ElaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElaController.class);
    /**
     * 处理请求
     * @param params
     * @return
     * @throws Exception
     */
    public static String processMethod (String params) throws Exception {

        LOGGER.info(params);

        JSONObject jsonObject = JSONObject.fromObject(params);
        JSONArray jsonArray = jsonObject.getJSONArray("params");
        String method = jsonObject.getString("method");
        if (jsonArray.size() == 0) {
            if (method.equals("genPrivateKey") ) {
                return  genPrivateKey();
            }
            if (method.equals("gen_priv_pub_addr")) {
                return gen_priv_pub_addr();
            }
            if (method.equals("getAllAccount")) {
                return getAllAccount();
            }
            if (method.equals("getAccountAllAddress")) {
                return getAccountAllAddress();
            }
        }
        if (jsonArray.size() != 0){
            JSONObject param = (JSONObject)jsonArray.get(0);
            if (method.equals("genPublicKey")){
                return genPublicKey(param);
            }
            if (method.equals("genAddress")){
                return genAddress(param);
            }
            if (method.equals("genRawTransaction")){
                return genRawTransaction(param);
            }
            if (method.equals("decodeRawTransaction")){
                String rawTransaction = param.getString("RawTransaction");
                return decodeRawTransaction(rawTransaction);
            }
            if (method.equals("genRawTransactionByPrivateKey")) {
                return genRawTransactionByPrivateKey(param);
            }
            if (method.equals("checkAddress")) {
                return checkAddress(param);
            }
            if (method.equals("genRawTransactionByAccount")) {
                return genRawTransactionByAccount(param);
            }
            if (method.equals("addAccount")) {
                return addAccount(param);
            }
            if (method.equals("removeAccount")) {
                return removeAccount(param);
            }
            if (method.equals("createAccount")) {
                return createAccount(param);
            }
            if (method.equals("getAccountPrivateKey")) {
                return getAccountPrivateKey(param);
            }
            if (method.equals("genIdentityID")){
                return genIdentityID(param);
            }
        }
        return null;
    }

    /**
     * 校验地址是否为ela合法地址
     * @param addresses 字典格式或者数组格式的地址
     * @return
     */
    public static String checkAddress(JSONObject addresses){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        JSONArray addressesJSONArray = addresses.getJSONArray("Addresses");
        Object addressesObject = addressesJSONArray.get(0);
        if (addressesObject instanceof String){
            for (int i = 0 ; i < addressesJSONArray.size() ; i ++){
                boolean boo =  Util.checkAddress((String)addressesJSONArray.get(i));
                resultMap.put((String)addressesJSONArray.get(i),boo);
            }
        }else {
            for (int i = 0 ; i < addressesJSONArray.size() ; i ++){
                JSONObject o = (JSONObject) addressesJSONArray.get(i);
                String address = o.getString("address");
                boolean boo =  Util.checkAddress(address);
                resultMap.put(address,boo);
            }
        }

        LOGGER.info(formatJson("checkAddress",resultMap));
        return formatJson("checkAddress",resultMap) ;
    }

    /**
     * 生成RawTrnsaction
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return  返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransaction(JSONObject inputsAddOutpus) throws Exception{

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        //解析inputs
        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);

            try {
                Verify.verifyParameter(Verify.Type.TxidLower,utxoInput);
                Verify.verifyParameter(Verify.Type.IndexLower,utxoInput);
                Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInt("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);

            inputList.add(new UTXOTxInput(txid,index,privateKey,address));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        List<TxOutput>  outputList = new LinkedList<TxOutput>();
        for (int j = 0 ; j < outputs.size() ; j ++){
            JSONObject output = (JSONObject)outputs.get(j);

            try {
                Verify.verifyParameter(Verify.Type.AddressLower,output);
                Verify.verifyParameter(Verify.Type.AmountLower,output);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            String address = output.getString("address");
            long amount = output.getLong("amount");
            outputList.add(new TxOutput(address,amount));
        }

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        RawTx rawTx = Ela.makeAndSignTx(inputList.toArray(new UTXOTxInput[utxoInputs.size()]),outputList.toArray(new TxOutput[outputs.size()]));
        resultMap.put("rawTx",rawTx.getRawTxString());
        resultMap.put("txHash",rawTx.getTxHash());

        LOGGER.info(formatJson("genRawTransaction",resultMap));
        return formatJson("genRawTransaction",resultMap);
    }

    /**
     * 生成私钥
     * @return 返回json字符串
     */
    public static String genPrivateKey(){
        String privateKey = Ela.getPrivateKey();

        LOGGER.info(formatJson("genPrivateKey",privateKey));
        return formatJson("genPrivateKey",privateKey);
    }

    /**
     * 生成公钥
     * @param jsonObject  私钥
     * @return 返回json字符串
     */
    public static String genPublicKey(JSONObject jsonObject){
        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper,jsonObject);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }

        String privateKey = jsonObject.getString("PrivateKey");
        String publicKey = Ela.getPublicFromPrivate(privateKey);

        LOGGER.info(formatJson("genPublicKey",publicKey));
        return formatJson("genPublicKey",publicKey);
    }

    /**
     * 生成地址
     * @param jsonObject  私钥
     * @return  返回Json字符串
     */
    public static String genAddress(JSONObject jsonObject){

        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper,jsonObject);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
        String privateKey = jsonObject.getString("PrivateKey");
        String address    = Ela.getAddressFromPrivate(privateKey);

        LOGGER.info(formatJson("genAddress",address));
        return formatJson("genAddress",address);
    }

    /**
     * 生成身份id
     * @param jsonObject  私钥
     * @return  返回Json字符串
     */
    public static String genIdentityID(JSONObject jsonObject){

        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper,jsonObject);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
        String privateKey = jsonObject.getString("PrivateKey");
        String address    = Ela.getIdentityIDFromPrivate(privateKey);

        LOGGER.info(formatJson("genIdentityID",address));
        return formatJson("genIdentityID",address);
    }

    /**
     * 生成私钥、公钥、地址
     * @return  返回json字符串
     */
    public static String gen_priv_pub_addr(){
        String privateKey = Ela.getPrivateKey();
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        String address    = Ela.getAddressFromPrivate(privateKey);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("PrivateKey",privateKey);
        resultMap.put("PublicKey",publicKey);
        resultMap.put("Address",address);

        LOGGER.info(formatJson("gen_priv_pub_addr",resultMap));
        return formatJson("gen_priv_pub_addr",resultMap);
    }

    /**
     * 发送Rawtransaction
     * @param rawTx
     */
    public static String sendRawTransaction(String rawTx ,String txUrl) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data" , rawTx);

        //构造json格式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("method", "sendrawtransaction");
        map.put("params", params);

        //发送RowTransaction
        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        System.out.println("url = " + txUrl);
        System.out.println("json = " + jsonParam);
        JSONObject responseJSONObject = HttpRequestUtil.httpPost(txUrl, jsonParam);

        LOGGER.info(responseJSONObject.toString());
        return responseJSONObject.toString();
    }

    /**
     * 反解析rawTransaction得到TXid,address,value
     * @param rawTransaction
     * @return
     * @throws IOException
     */
    public static String decodeRawTransaction(String rawTransaction) throws IOException {

        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTransaction);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);
        Map resultMap = Tx.DeSerialize(dos);

        LOGGER.info(formatJson("decodeRawTransaction",resultMap));
        return formatJson("decodeRawTransaction",resultMap);
    }

    /**
     * 根据私钥获取utxo生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransactionByPrivateKey(JSONObject inputsAddOutpus){

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");

        //解析inputs
        List<String> privateList = new LinkedList<String>();
        for (int i = 0; i < PrivateKeys.size(); i++) {
            JSONObject utxoInput = (JSONObject) PrivateKeys.get(i);
            try {
                Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            privateList.add(utxoInput.getString("privateKey"));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);
            try {
                Verify.verifyParameter(Verify.Type.AddressLower,output);
                Verify.verifyParameter(Verify.Type.AmountLower,output);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }

        try {
            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }

        String changeAddress = json_transaction.getString("ChangeAddress");

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            String rawTx = FinishUtxo.finishUtxo(privateList, outputList, changeAddress);
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", FinishUtxo.txHash);

            LOGGER.info(formatJson("genRawTransactionByPrivateKey" ,resultMap));
            return formatJson("genRawTransactionByPrivateKey" ,resultMap);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    /**
     * 通过本地账户创建交易
     * @param outpus
     * @return
     * @throws Exception
     */
    public static String genRawTransactionByAccount(JSONObject outpus){

        final JSONArray transaction = outpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);

        List<String> privateList = new LinkedList<String>();

        Object account = json_transaction.get("Account");
        if (account != null){
            JSONArray accountArray = (JSONArray)account;
            for (int i = 0; i < accountArray.size(); i++) {
                JSONObject JsonAccount = (JSONObject) accountArray.get(i);

                try {
                    Verify.verifyParameter(Verify.Type.AddressLower,JsonAccount);
                    Verify.verifyParameter(Verify.Type.PasswordLower,JsonAccount);
                }catch (Exception e){
                    LOGGER.error(e.toString());
                    return e.toString();
                }

                String address = JsonAccount.getString("address");
                if (!KeystoreFile.isExistAccount(address)){
                    LOGGER.error((new SDKException(ErrorCode.ParamErr("[" + address + "] Account not exist"))).toString());
                    return (new SDKException(ErrorCode.ParamErr("[" + address + "] Account not exist"))).toString();
                }
            }

            //解析inputs
            for (int i = 0; i < accountArray.size(); i++) {
                JSONObject JsonAccount = (JSONObject) accountArray.get(i);
                String password = JsonAccount.getString("password");
                String address = JsonAccount.getString("address");
                String privateKey = "";
                try {
                    privateKey = WalletMgr.getAccountPrivateKey(password,address);
                }catch (Exception e){
                    LOGGER.error(e.toString());
                    return e.toString();

                }

                System.out.println(privateKey);
                privateList.add(privateKey);
            }
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);
            try {
                Verify.verifyParameter(Verify.Type.AddressLower,output);
                Verify.verifyParameter(Verify.Type.AmountLower,output);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }

        try {
            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }

        String changeAddress = json_transaction.getString("ChangeAddress");

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            String rawTx = FinishUtxo.finishUtxo(privateList, outputList, changeAddress);
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", FinishUtxo.txHash);

            LOGGER.info(formatJson("genRawTransactionByAccount" ,resultMap));
            return formatJson("genRawTransactionByAccount" ,resultMap);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String formatJson(String action, Object resultMap){
        HashMap map = new HashMap();
        map.put("Action",action);
        map.put("Desc","SUCCESS");
        map.put("Result",resultMap);

        return JSON.toJSONString(map);
    }

    // =================================== Account ===========================================

    public static String createAccount(JSONObject param){
        final JSONArray accountArray = param.getJSONArray("Account");
        JSONArray account = new JSONArray();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            try {
                Verify.verifyParameter(Verify.Type.PasswordLower,JsonAccount);
                String password = JsonAccount.getString("password");
                account = WalletMgr.createAccount(password);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        }

        LOGGER.info(formatJson("createAccount" ,account));
        return formatJson("createAccount",account);
    }

    public static String addAccount(JSONObject param){

        final JSONArray accountArray = param.getJSONArray("Account");
        JSONArray account = new JSONArray();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            try {
                Verify.verifyParameter(Verify.Type.PasswordLower,JsonAccount);
                Verify.verifyParameter(Verify.Type.PrivateKeyLower,JsonAccount);
                String privateKey = JsonAccount.getString("privateKey");
                String password = JsonAccount.getString("password");
                account = WalletMgr.addAccount(password,privateKey);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        }

        LOGGER.info(formatJson("addAccount" ,account));
        return formatJson("addAccount",account);
    }

    public static String removeAccount(JSONObject param){
        final JSONArray accountArray = param.getJSONArray("Account");
        JSONObject JsonAccount = (JSONObject) accountArray.get(0);
        JSONArray account;
        try {
            Verify.verifyParameter(Verify.Type.PasswordLower,JsonAccount);
            Verify.verifyParameter(Verify.Type.AddressLower,JsonAccount);
            String address = JsonAccount.getString("address");
            String password = JsonAccount.getString("password");
            account = WalletMgr.removeAccount(password,address);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }

        LOGGER.info(formatJson("removeAccount" ,account));
        return formatJson("removeAccount",account);
    }

    public static String getAccountAllAddress(){
        String account = null;
        try {
            account = WalletMgr.getAccountAllAddress();
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }

        LOGGER.info(formatJson("getAccountAllAddress" ,account));
        return formatJson("getAccountAllAddress",account);
    }

    public static String getAccountPrivateKey(JSONObject param){

        final JSONArray accountArray = param.getJSONArray("Account");

        LinkedList<String> privateKeyList = new LinkedList<String>();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            String privateKey = "";
            try {
                Verify.verifyParameter(Verify.Type.PasswordLower,JsonAccount);
                Verify.verifyParameter(Verify.Type.AddressLower,JsonAccount);
                String address = JsonAccount.getString("address");
                String password = JsonAccount.getString("password");
                privateKey = WalletMgr.getAccountPrivateKey(password,address);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }
            privateKeyList.add(privateKey);
        }

        LOGGER.info(formatJson("getAccountPrivateKey" ,privateKeyList));
        return formatJson("getAccountPrivateKey",privateKeyList);
    }

    public static String getAllAccount(){
        JSONArray account = KeystoreFile.readAccount();

        LOGGER.info(formatJson("getAllAccount" ,account));
        return formatJson("getAllAccount",account);
    }
}

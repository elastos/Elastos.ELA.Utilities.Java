package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.UsableUtxo;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.TxOutput;
import org.elastos.wallet.KeystoreFile;
import org.elastos.wallet.WalletMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

    /**
     * 通过本地账户创建交易
     * @param outpus
     * @return
     * @throws Exception
     */
    public static String genTxByAccount(JSONObject outpus){
        try {
            final JSONArray transaction = outpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);

            final JSONArray outputs = json_transaction.getJSONArray("Outputs");

            List<String> privateList = new LinkedList<String>();

            Object account = json_transaction.get("Account");
            if (account != null) {
                JSONArray accountArray = (JSONArray) account;
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject JsonAccount = (JSONObject) accountArray.get(i);

                    try {
                        Verify.verifyParameter(Verify.Type.AddressLower, JsonAccount);
                        Verify.verifyParameter(Verify.Type.PasswordLower, JsonAccount);
                    } catch (Exception e) {
                        LOGGER.error(e.toString());
                        return e.toString();
                    }

                    String address = JsonAccount.getString("address");
                    if (!KeystoreFile.isExistAccount(address)) {
                        LOGGER.error((new SDKException(ErrorCode.ParamErr("[" + address + "] Account not exist"))).toString());
                        return (new SDKException(ErrorCode.ParamErr("[" + address + "] Account not exist"))).toString();
                    }
                }
                //解析inputs
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject JsonAccount = (JSONObject) accountArray.get(i);
                    String password = JsonAccount.getString("password");
                    String address = JsonAccount.getString("address");
                    privateList.add(WalletMgr.getAccountPrivateKey(password, address));
                }
            }
            //解析outputs
            LinkedList<TxOutput> txOutputs = Basic.parseOutputs(outputs);
            //解析payloadRecord
            PayloadRecord payload   = Basic.parsePayloadRecord(json_transaction);

            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);
            String changeAddress = json_transaction.getString("ChangeAddress");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            String rawTx = "";
            boolean bool = json_transaction.has("Memo");
            if (payload != null && bool){
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            }else if (payload == null && !bool){
                rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress);
            }else if (bool){
                String memo = json_transaction.getString("Memo");
                rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress,memo);
            }else{
                rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress,payload);
            }
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess("genTxByAccount" ,resultMap));
            return Basic.getSuccess("genTxByAccount" ,resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }


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

        LOGGER.info(Basic.getSuccess("createAccount" ,account));
        return Basic.getSuccess("createAccount",account);
    }

    public static String importAccount(JSONObject param){

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

        LOGGER.info(Basic.getSuccess("importAccount" ,account));
        return Basic.getSuccess("importAccount",account);
    }

    public static String removeAccount(JSONObject param){
        try {
            final JSONArray accountArray = param.getJSONArray("Account");
            JSONObject JsonAccount = (JSONObject) accountArray.get(0);

            Verify.verifyParameter(Verify.Type.PasswordLower,JsonAccount);
            Verify.verifyParameter(Verify.Type.AddressLower,JsonAccount);
            String address = JsonAccount.getString("address");
            String password = JsonAccount.getString("password");
            JSONArray account = WalletMgr.removeAccount(password,address);
            LOGGER.info(Basic.getSuccess("removeAccount" ,account));
            return Basic.getSuccess("removeAccount",account);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String getAccountAddresses(){
        try {
            String account = WalletMgr.getAccountAllAddress();
            LOGGER.info(Basic.getSuccess("getAccountAddresses" ,account));
            return Basic.getSuccess("getAccountAddresses",account);
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }


    }

    public static String exportPrivateKey(JSONObject param){

        final JSONArray accountArray = param.getJSONArray("Account");

        LinkedList<String> privateKeyList = new LinkedList<String>();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            String privateKey ;
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

        LOGGER.info(Basic.getSuccess("exportPrivateKey" ,privateKeyList));
        return Basic.getSuccess("exportPrivateKey",privateKeyList);
    }

    public static String getAccounts(){
        JSONArray account = KeystoreFile.readAccount();

        LOGGER.info(Basic.getSuccess("getAccounts" ,account));
        return Basic.getSuccess("getAccounts",account);
    }

}

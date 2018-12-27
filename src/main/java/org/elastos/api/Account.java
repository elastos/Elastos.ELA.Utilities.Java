package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.UsableUtxo;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.TxOutput;
import org.elastos.framework.rpc.Rpc;
import org.elastos.wallet.KeystoreFile;
import org.elastos.wallet.WalletMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.elastos.ela.UsableUtxo.RPCURL;

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
            final JSONObject json_transaction = outpus.getJSONObject("transaction");
            final JSONArray outputs = json_transaction.getJSONArray("outputs");

            List<String> privateList = new LinkedList<String>();

            Object account = json_transaction.get("account");
            if (account != null) {
                JSONArray accountArray = (JSONArray) account;
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject JsonAccount = (JSONObject) accountArray.get(i);

                    try {
                        Verify.verifyParameter(Verify.Type.Address, JsonAccount);
                        Verify.verifyParameter(Verify.Type.Password, JsonAccount);
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
            String changeAddress = json_transaction.getString("changeAddress");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            String rawTx = "";
            boolean bool = json_transaction.has("memo");
            if (payload != null && bool){
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            }else if (payload == null && !bool){
                rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress);
            }else if (bool){
                String memo = json_transaction.getString("memo");
                rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress,memo);
            }else{
                rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress,payload);
            }
            resultMap.put("rawtx", rawTx);
            resultMap.put("txhash", UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }


    public static String createAccount(JSONObject param){
        final JSONArray accountArray = param.getJSONArray("account");
        JSONArray account = new JSONArray();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            try {
                Verify.verifyParameter(Verify.Type.Password,JsonAccount);
                String password = JsonAccount.getString("password");
                account = WalletMgr.createAccount(password);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        }

        LOGGER.info(Basic.getSuccess(account));
        return Basic.getSuccess(account);
    }

    public static String importAccount(JSONObject param){

        final JSONArray accountArray = param.getJSONArray("account");
        JSONArray account = new JSONArray();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            try {
                Verify.verifyParameter(Verify.Type.Password,JsonAccount);
                Verify.verifyParameter(Verify.Type.PrivateKey,JsonAccount);
                String privateKey = JsonAccount.getString("privateKey");
                String password = JsonAccount.getString("password");
                account = WalletMgr.addAccount(password,privateKey);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        }

        LOGGER.info(Basic.getSuccess(account));
        return Basic.getSuccess(account);
    }

    public static String removeAccount(JSONObject param){
        try {
            final JSONArray accountArray = param.getJSONArray("account");
            JSONObject JsonAccount = (JSONObject) accountArray.get(0);

            Verify.verifyParameter(Verify.Type.Password,JsonAccount);
            Verify.verifyParameter(Verify.Type.Address,JsonAccount);
            String address = JsonAccount.getString("address");
            String password = JsonAccount.getString("password");
            JSONArray account = WalletMgr.removeAccount(password,address);
            LOGGER.info(Basic.getSuccess(account));
            return Basic.getSuccess(account);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String getAccountAddresses(){
        try {
            String account = WalletMgr.getAccountAllAddress().toString();
            LOGGER.info(Basic.getSuccess(account));
            return Basic.getSuccess(account);
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String getAccountAmount(){
        try {
            List addresses =  WalletMgr.getAccountAllAddress();

            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < addresses.size(); i++) {
                String address =(String) addresses.get(i);
                String getreceivedbyaddress = Rpc.getreceivedbyaddress(address, RPCURL);
                JSONObject fromObject = JSONObject.fromObject(getreceivedbyaddress);
                String amount = fromObject.getString("result");
                hashMap.put(address,amount);
            }
            LOGGER.info(Basic.getSuccess(hashMap));
            return Basic.getSuccess(hashMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String exportPrivateKey(JSONObject param){

        final JSONArray accountArray = param.getJSONArray("account");

        LinkedList<String> privateKeyList = new LinkedList<String>();
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject JsonAccount = (JSONObject) accountArray.get(i);
            String privateKey ;
            try {
                Verify.verifyParameter(Verify.Type.Password,JsonAccount);
                Verify.verifyParameter(Verify.Type.Address,JsonAccount);
                String address = JsonAccount.getString("address");
                String password = JsonAccount.getString("password");
                privateKey = WalletMgr.getAccountPrivateKey(password,address);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }
            privateKeyList.add(privateKey);
        }

        LOGGER.info(Basic.getSuccess(privateKeyList));
        return Basic.getSuccess(privateKeyList);
    }

    public static String getAccounts(){
        JSONArray account = KeystoreFile.readAccount();

        LOGGER.info(Basic.getSuccess(account));
        return Basic.getSuccess(account);
    }

}

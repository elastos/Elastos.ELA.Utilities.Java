package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.FinishUtxo;
import org.elastos.ela.TxOutput;
import org.elastos.wallet.KeystoreFile;
import org.elastos.wallet.WalletMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

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

            LOGGER.info(Basic.getSuccess("genRawTransactionByAccount" ,resultMap));
            return Basic.getSuccess("genRawTransactionByAccount" ,resultMap);
        }catch (Exception e){
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

        LOGGER.info(Basic.getSuccess("removeAccount" ,account));
        return Basic.getSuccess("removeAccount",account);
    }

    public static String getAccountAddresses(){
        String account = null;
        try {
            account = WalletMgr.getAccountAllAddress();
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }

        LOGGER.info(Basic.getSuccess("getAccountAddresses" ,account));
        return Basic.getSuccess("getAccountAddresses",account);
    }

    public static String exportPrivateKey(JSONObject param){

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

        LOGGER.info(Basic.getSuccess("exportPrivateKey" ,privateKeyList));
        return Basic.getSuccess("exportPrivateKey",privateKeyList);
    }

    public static String getAccounts(){
        JSONArray account = KeystoreFile.readAccount();

        LOGGER.info(Basic.getSuccess("getAccounts" ,account));
        return Basic.getSuccess("getAccounts",account);
    }

}

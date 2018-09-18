package org.elastos.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.Ela;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class WalletMgr {

    public static JSONArray createAccount(String password)throws Exception{

        if (!KeystoreFile.isExistKeystoreFile()){
            // keystore 文件不存在，创建账户
            List account = createAccount(password,"");
            KeystoreFile.createAccount(account.toString());
        }else throw new SDKException(ErrorCode.KeystoreExist);

        return KeystoreFile.readAccount();
    }

    public static JSONArray addAccount(String password, String privateKey) throws Exception {

        String address = Ela.getAddressFromPrivate(privateKey);

        //keystore 文件存在
        if (KeystoreFile.isExistKeystoreFile()){
            //账户不存在,添加账户
            if (!KeystoreFile.isExistAccount(address)){
                List account = createAccount(password,privateKey);
                KeystoreFile.addAccount((String) account.get(0));
            }else throw new SDKException(ErrorCode.AccountExist);
        }else {
            //keystore 文件不存在，创建账户
            List account = createAccount(password, privateKey);
            KeystoreFile.createAccount(account.toString());
        }
        return KeystoreFile.readAccount();
    }

    public static String getAccountPrivateKey(String password,String address) throws Exception {

        if (KeystoreFile.isExistKeystoreFile()){
            if (KeystoreFile.isExistAccount(address)){
                try {
                    File file = KeystoreFile.getKeystorePath();
                    String content = FileUtils.readFileToString(file,"UTF-8");
                    JSONArray jsonArray = JSONArray.fromObject(content);
                    for (int i = 0 ; i < jsonArray.size() ; i++){
                        JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                        String address1 = jsonObject.getString("address");
                        if (address1.equals(address)){
                            String encryptedPrivateKey = jsonObject.getString("encryptedPrivateKey");
                            byte[] salt = Base64.getDecoder().decode(jsonObject.getString("salt"));

                            JSONObject scrypt = jsonObject.getJSONObject("scrypt");
                            int n = scrypt.getInt("n");
                            byte[] gcmDecodedPrivateKey = Account.getGcmDecodedPrivateKey(encryptedPrivateKey, password, address, salt, n);
                            String privateKey = Account.parsePrivateKey(gcmDecodedPrivateKey);
                            return privateKey;
                        }
                    }
                }catch (Exception e){
                    throw e;
                }
            }else throw new SDKException(ErrorCode.AccountNotExist);
        }else throw new SDKException(ErrorCode.KeystoreNotExist);
        return null;
    }

    public static String getAccountAllAddress() throws SDKException {
        if (KeystoreFile.isExistKeystoreFile()){
            try {
                File file = KeystoreFile.getKeystorePath();
                String content = FileUtils.readFileToString(file,"UTF-8");
                JSONArray jsonArray = JSONArray.fromObject(content);
                List<String> addressList = new ArrayList<String>();
                for (int i = 0 ; i < jsonArray.size() ; i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    addressList.add(jsonObject.getString("address"));
                }
                return addressList.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else throw new SDKException(ErrorCode.KeystoreNotExist);
        return null;
    }

    public static JSONArray removeAccount(String password , String address) throws Exception {
        if (KeystoreFile.isExistKeystoreFile()){
            if (KeystoreFile.isExistAccount(address)){
                try {
                    File file = KeystoreFile.getKeystorePath();
                    String content = FileUtils.readFileToString(file,"UTF-8");
                    JSONArray jsonArray = JSONArray.fromObject(content);
                    if (jsonArray.size() == 1){
                        throw new SDKException(ErrorCode.ParamErr("Only one account can not be deleted"));
                    }
                    for (int i = 0 ; i < jsonArray.size() ; i++){
                        JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                        String address1 = jsonObject.getString("address");
                        if (address1.equals(address)){
                            String encryptedPrivateKey = jsonObject.getString("encryptedPrivateKey");
                            byte[] salt = Base64.getDecoder().decode(jsonObject.getString("salt"));

                            JSONObject scrypt = jsonObject.getJSONObject("scrypt");
                            int n = scrypt.getInt("n");
                            Account.getGcmDecodedPrivateKey(encryptedPrivateKey, password, address, salt, n);
                            KeystoreFile.deleteAccount(address);
                        }
                    }
                }catch (Exception e){
                    throw e;
                }
            }else throw new SDKException(ErrorCode.AccountNotExist);
        }else throw new SDKException(ErrorCode.KeystoreNotExist);
        return KeystoreFile.readAccount();
    }

    public static List createAccount(String password ,String privateKey) throws Exception {
        Account account;
        if (privateKey.equals("")){
            account = new Account();
        }else account = new Account(privateKey);
        account.exportGcmEncryptedPrivateKey(password);

        List<String> list = new ArrayList<String>();
        list.add(account.toString());
        return list;
    }
}

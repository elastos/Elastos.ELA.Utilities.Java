package org.elastos.wallet;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elastos.ela.Ela;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: DongLei.Tan
 * @contact: tandonglei@elastos.org
 * @time: 2018/9/5
 * */
public class Account {

    public static String createAccountFile(){

        if (!KeystoreFile.isExistKeystoreFile()){
            // keystore 文件不存在，创建账户
            JSONArray account = createAccount("");
            KeystoreFile.createAccount(account.toString());
        }
//        else return "keystore file exist";

        return KeystoreFile.readAccount().toString();
    }


    public static String addAccount(String privateKey){

        String publicKey = Ela.getPublicFromPrivate(privateKey);

        //keystore 文件存在
        if (KeystoreFile.isExistKeystoreFile()){
            //账户不存在,添加账户
            if (!KeystoreFile.isExistAccount(publicKey)){
                JSONArray account = createAccount(privateKey);
                KeystoreFile.addAccount(account.get(0).toString());
            }
//            else return "account exist";
        }else {
            //keystore 文件不存在，创建账户
            JSONArray account = createAccount(privateKey);
            KeystoreFile.createAccount(account.toString());
        }
        return KeystoreFile.readAccount().toString();
    }

    public static String deleteAccount(String publicKey){

        if (KeystoreFile.isExistKeystoreFile()){
            if (KeystoreFile.isExistAccount(publicKey.toLowerCase())){
                KeystoreFile.deleteAccount(publicKey.toLowerCase());
            }
        }
        JSONArray jsonAccount = KeystoreFile.readAccount();
        return jsonAccount.toString();
    }

    public static JSONArray createAccount(String privateKey){

        KeystoreFile keystoreFile = new KeystoreFile();

        if (privateKey.equals("")){
            privateKey = Ela.getPrivateKey();
        }
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        keystoreFile.setPrivateKey(privateKey.toLowerCase());
        keystoreFile.setPublicKey(publicKey.toLowerCase());
        return JSONArray.fromObject(keystoreFile);
    }

    public static String getAccountPrivateKey(String publicKey){
        try {
            File file = KeystoreFile.getKeystorePath();
            String content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray jsonArray = JSONArray.fromObject(content);
            for (int i = 0 ; i < jsonArray.size() ; i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                if (jsonObject.getString("publicKey").equals(publicKey)){
                    return jsonObject.getString("privateKey");
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static List<String> getAccountAllPrivateKey(){
        try {
            File file = KeystoreFile.getKeystorePath();
            String content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray jsonArray = JSONArray.fromObject(content);
            List<String> privateList = new ArrayList<String>();
            for (int i = 0 ; i < jsonArray.size() ; i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                privateList.add(jsonObject.getString("privateKey"));
            }
            return privateList;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;

    }
}

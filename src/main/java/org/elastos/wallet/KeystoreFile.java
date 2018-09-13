package org.elastos.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KeystoreFile {

    private static String DefaultKeystoreFile = "keystore.dat";

    /**
     * 创建账户
     * @param account
     */
    public static void createAccount(String account) throws IOException {
        try {
            File file = getKeystorePath();
            FileWriter FW = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(FW);
            writer.write(account);
            writer.close();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 添加账户
     * @param account
     */
    public static void addAccount(String account){
        try {
            File file = getKeystorePath();
            String content = FileUtils.readFileToString(file,"UTF-8");

            JSONArray jsonArray = JSONArray.fromObject(content);
            jsonArray.add(account);

            createAccount(jsonArray.toString());
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * 读取账户
     * @return
     */
    public static JSONArray readAccount(){
        try {
            File file = getKeystorePath();
            String content = FileUtils.readFileToString(file,"UTF-8");

            JSONArray jsonArray = JSONArray.fromObject(content);
            return jsonArray ;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    /**
     * 删除账户
     * @param address
     * @return
     */
    public static String deleteAccount(String address){
        try {
            File file = getKeystorePath();
            String content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray jsonArray = JSONArray.fromObject(content);

            for (int i = 0 ; i < jsonArray.size() ; i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                if (jsonObject.getString("address").equals(address)){
                    jsonArray.remove(i);
                }
            }
            createAccount(jsonArray.toString());
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    /**
     * 获取keystore路径
     * @return
     */
    public static File getKeystorePath(){
        try {
            File directory = new File ("");
            String courseFile = directory.getCanonicalPath();
            File file = new File(courseFile + "/" + DefaultKeystoreFile);
            return file;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    /**
     * 账户是否存在
     * @param address
     * @return
     */
    public static boolean isExistAccount(String address){
        try {
            File file = getKeystorePath();
            String content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray jsonArray = JSONArray.fromObject(content);
            for (int i = 0 ; i < jsonArray.size() ; i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                if (jsonObject.getString("address").equals(address)){
                    return true;
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }


    /**
     * keystore文件是否存在
     * @return
     */
    public static boolean isExistKeystoreFile(){
        File file = getKeystorePath();
        if(file.exists()) return true;
        return false;
    }
}


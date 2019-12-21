package org.elastos.common;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elastos.api.Verify;

import java.io.File;
import java.math.BigDecimal;

import static org.elastos.ela.payload.PayloadRegisterAsset.ElaPrecision;

public class Config {
    private static String Host;
    private static long Fee;
    private static int Confirmation;
    private static long RegisterAssetFee;
    private static String RpcConfiguration;
    private static String User;
    private static String Pass;
    private static String RpcUrl;

    public static String getHost() {
        return Host;
    }

    public static long getFee() {
        return Fee;
    }

    public static int getConfirmation() {
        return Confirmation;
    }

    public static long getRegisterAssetFee() {
        return RegisterAssetFee;
    }

    public static String getRpcConfiguration() {
        return RpcConfiguration;
    }

    public static String getUser() {
        return User;
    }

    public static String getPass() {
        return Pass;
    }

    public static String getRpcUrl() {
        return RpcUrl;
    }


    public static void setRpcUrl(String rpcUrl) {
        RpcUrl = rpcUrl;
    }

    public static void setHost(String host) {
        Host = host;
    }

    public static void setFee(long fee) {
        Fee = fee;
    }

    public static void setConfirmation(int confirmation) {
        Confirmation = confirmation;
    }

    public static void setRegisterAssetFee(long registerAssetFee) {
        RegisterAssetFee = registerAssetFee;
    }

    public static void setUser(String user) {
        User = user;
    }

    public static void setPass(String pass) {
        Pass = pass;
    }


    /**
     * 获取java-config.json配置文件信息
     * @throws SDKException
     */
    public static void getConfig() throws SDKException {
        String content;
        JSONObject jsonObject;
        try {
            File directory = new File ("");
            String courseFile = directory.getCanonicalPath();
//        File file = new File(courseFile + "/src/main/resources/java-config.json.md");
            File file = new File(courseFile + "/java-config.json.md");
            content = FileUtils.readFileToString(file,"UTF-8");
            jsonObject = JSONObject.fromObject(content);

        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("reade java-config.json.md error : " + e.toString()));
        }
            Verify.verifyParameter(Verify.Type.Host,jsonObject);
            Verify.verifyParameter(Verify.Type.Confirmation,jsonObject);
            Verify.verifyParameter(Verify.Type.Fee,jsonObject);
            Verify.verifyParameter(Verify.Type.RegisterAssetFee,jsonObject);
            Verify.verifyParameter(Verify.Type.RpcConfiguration,jsonObject);

            JSONObject rpcConfiguration = jsonObject.getJSONObject(InterfaceParams.RPC_CONFIGURATION);
            Verify.verifyParameter(Verify.Type.User,rpcConfiguration);
            Verify.verifyParameter(Verify.Type.Pass,rpcConfiguration);

            String host = jsonObject.getString(InterfaceParams.HOST);

            String fee = jsonObject.getString(InterfaceParams.FEE);
            Config.setFee(Util.multiplyAmountELA(new BigDecimal(fee), ElaPrecision).longValue());

            Config.setRpcUrl("http://" + host);
            int confirmation = jsonObject.getInt(InterfaceParams.CONFIRMATION);
            Config.setConfirmation(confirmation);

            String registerAssetFee = jsonObject.getString(InterfaceParams.REGISTER_ASSET_FEE);
            Config.setRegisterAssetFee(Util.multiplyAmountELA(new BigDecimal(registerAssetFee), ElaPrecision).longValue());
            if (confirmation == 0){
                Config.setConfirmation(16);
            }

            Config.setUser(rpcConfiguration.getString(InterfaceParams.USER));
            Config.setPass(rpcConfiguration.getString(InterfaceParams.PASS));
    }
}

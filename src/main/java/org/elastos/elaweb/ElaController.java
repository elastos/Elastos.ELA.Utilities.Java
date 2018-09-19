package org.elastos.elaweb;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.api.Account;
import org.elastos.api.Basic;
import org.elastos.api.SignTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
                return Basic.genPrivateKey();
            }
            if (method.equals("gen_priv_pub_addr")) {
                return Basic.gen_priv_pub_addr();
            }
            if (method.equals("getAccounts")) {
                return Account.getAccounts();
            }
            if (method.equals("getAccountAddresses")) {
                return Account.getAccountAddresses();
            }
        }
        if (jsonArray.size() != 0){
            JSONObject param = (JSONObject)jsonArray.get(0);
            if (method.equals("genPublicKey")){
                return Basic.genPublicKey(param);
            }
            if (method.equals("genAddress")){
                return Basic.genAddress(param);
            }
            if (method.equals("genRawTransaction")){
                return SignTransaction.genRawTransaction(param);
            }
            if (method.equals("decodeRawTransaction")){
                String rawTransaction = param.getString("RawTransaction");
                return SignTransaction.decodeRawTransaction(rawTransaction);
            }
            if (method.equals("genRawTransactionByPrivateKey")) {
                return SignTransaction.genRawTransactionByPrivateKey(param);
            }
            if (method.equals("checkAddress")) {
                return Basic.checkAddress(param);
            }
            if (method.equals("genRawTransactionByAccount")) {
                return Account.genRawTransactionByAccount(param);
            }
            if (method.equals("importAccount")) {
                return Account.importAccount(param);
            }
            if (method.equals("removeAccount")) {
                return Account.removeAccount(param);
            }
            if (method.equals("createAccount")) {
                return Account.createAccount(param);
            }
            if (method.equals("exportPrivateKey")) {
                return Account.exportPrivateKey(param);
            }
            if (method.equals("genIdentityID")){
                return Basic.genIdentityID(param);
            }
        }
        return null;
    }
}

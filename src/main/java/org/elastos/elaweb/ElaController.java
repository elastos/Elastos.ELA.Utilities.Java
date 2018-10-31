package org.elastos.elaweb;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.api.Account;
import org.elastos.api.Basic;
import org.elastos.api.MultSigntransaction;
import org.elastos.api.SingleSignTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
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
                return SingleSignTransaction.genRawTransaction(param);
            }
            if (method.equals("genRegisterTransaction")){
                return SingleSignTransaction.genRegisterTransaction(param);
            }
            if (method.equals("genRawTransactionByToken")){
                return SingleSignTransaction.genRawTransactionByToken(param);
            }
            if (method.equals("genMultiSignRawTransactionByToken")){
                return MultSigntransaction.genMultiSignRawTransactionByToken(param);
            }
            if (method.equals("decodeRawTransaction")){
                String rawTransaction = param.getString("RawTransaction");
                return SingleSignTransaction.decodeRawTransaction(rawTransaction);
            }
            if (method.equals("genRawTransactionByPrivateKey")) {
                return SingleSignTransaction.genRawTransactionByPrivateKey(param);
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
            if (method.equals("genGenesisAddress")) {
                return Basic.genGenesisAddress(param);
            }
            if (method.equals("genMultiSignAddress")) {
                return Basic.genMultiSignAddress(param);
            }
            if (method.equals("genMultiSignRawTransaction")) {
                return MultSigntransaction.genMultiSignRawTransaction(param);
            }
            if (method.equals("genGenesisAddress")) {
                return Basic.genGenesisAddress(param);
            }
            if (method.equals("genCrossChainRawTransaction")) {
                return SingleSignTransaction.genCrossChainRawTransaction(param);
            }
            if (method.equals("genCrossChainMultiSignRawTransaction")) {
                return MultSigntransaction.genCrossChainMultiSignRawTransaction(param);
            }
            if (method.equals("genCrossChainRawTransactionByPrivateKey")) {
                return SingleSignTransaction.genCrossChainRawTransactionByPrivateKey(param);
            }
            if (method.equals("gendeyplyContractTransaction")) {
                return SingleSignTransaction.gendeyplyContractTransaction(param);
            }
        }
        return null;
    }
}

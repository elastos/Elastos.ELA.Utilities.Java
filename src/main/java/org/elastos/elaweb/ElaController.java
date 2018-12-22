package org.elastos.elaweb;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.api.*;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
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
        String method = jsonObject.getString("method");
        JSONObject param = jsonObject.getJSONObject("params");
        switch (method) {
            case "genPrivateKey":
                return Basic.genPrivateKey();

            case "gen_priv_pub_addr":
                return Basic.gen_priv_pub_addr();

            case "getAccounts":
                return Account.getAccounts();

            case "getAccountAddresses":
                return Account.getAccountAddresses();

            case "genPublicKey":
                return Basic.genPublicKey(param);

            case "genAddress":
                return Basic.genAddress(param);

            case "genNeoContractHashAndAddress":
                return Basic.genNeoContractHashAndAddress(param);

            case "genNeoContractAddress":
                return Basic.genNeoContractAddress(param);

            case "genIdentityID":
                return Basic.genIdentityID(param);

            case "genGenesisAddress":
                return Basic.genGenesisAddress(param);

            case "genMultiSignAddress":
                return Basic.genMultiSignAddress(param);

            case "checkAddress":
                return Basic.checkAddress(param);

            case "genRegisterTx":
                return TokenTransaction.genRegisterTx(param);

            case "genTokenTx":
                return TokenTransaction.genTokenTx(param);

            case "genTokenMultiSignTx":
                return TokenTransaction.genTokenMultiSignTx(param);

            case "genRegisterTxByPrivateKey":
                return TokenTransaction.genRegisterTxByPrivateKey(param);

            case "genTokenTxByPrivateKey":
                return TokenTransaction.genTokenTxByPrivateKey(param);

            case "genRawTransaction":
                return ELATransaction.genRawTransaction(param);

            case "decodeRawTransaction":
                String rawTransaction = param.getString("rawTransaction");
                return ELATransaction.decodeRawTransaction(rawTransaction);

            case "genRawTransactionByPrivateKey":
                return ELATransaction.genRawTransactionByPrivateKey(param);

            case "genMultiSignTx":
                return ELATransaction.genMultiSignTx(param);

            case "genTxByAccount":
                return Account.genTxByAccount(param);

            case "importAccount":
                return Account.importAccount(param);

            case "removeAccount":
                return Account.removeAccount(param);

            case "createAccount":
                return Account.createAccount(param);

            case "exportPrivateKey":
                return Account.exportPrivateKey(param);

            case "genCrossChainTx":
                return CrossChainTransaction.genCrossChainTx(param);

            case "genCrossChainMultiSignTx":
                return CrossChainTransaction.genCrossChainMultiSignTx(param);

            case "genCrossChainTxByPrivateKey":
                return CrossChainTransaction.genCrossChainTxByPrivateKey(param);

            case "genDeployContractTx":
                return NeoContractTransaction.genDeployContractTx(param);

            case "genInvokeContractTx":
                return NeoContractTransaction.genInvokeContractTx(param);

            default:
                return ErrorCode.ParamErr(method + " method does not exist");
        }
    }
}

package org.elastos.elaweb;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.api.*;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.UsableUtxo;
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

        UsableUtxo.getConfig_url();

        LOGGER.info(params);
        JSONObject jsonObject = JSONObject.fromObject(params);
        String method = jsonObject.getString("method");
        JSONObject param = jsonObject.getJSONObject("params");
        switch (method) {
            case "genprivatekey":
                return Basic.genPrivateKey();

            case "genprivpubaddr":
                return Basic.gen_priv_pub_addr();

            case "getaccounts":
                return Account.getAccounts();

            case "getaccountaddresses":
                return Account.getAccountAddresses();

            case "genpublickey":
                return Basic.genPublicKey(param);

            case "genaddress":
                return Basic.genAddress(param);

            case "genneocontracthashandaddress":
                return Basic.genNeoContractHashAndAddress(param);

            case "genneocontractaddress":
                return Basic.genNeoContractAddress(param);

            case "genidentityid":
                return Basic.genIdentityID(param);

            case "gengenesisaddress":
                return Basic.genGenesisAddress(param);

            case "genmultisignaddress":
                return Basic.genMultiSignAddress(param);

            case "checkaddress":
                return Basic.checkAddress(param);

            case "genregistertx":
                return TokenTransaction.genRegisterTx(param);

            case "gentokentx":
                return TokenTransaction.genTokenTx(param);

            case "gentokenmultisigntx":
                return TokenTransaction.genTokenMultiSignTx(param);

            case "genregistertxbyprivatekey":
                return TokenTransaction.genRegisterTxByPrivateKey(param);

            case "gentokentxbyprivatekey":
                return TokenTransaction.genTokenTxByPrivateKey(param);

            case "genrawtransaction":
                return ELATransaction.genRawTransaction(param);

            case "decoderawtransaction":
                String rawTransaction = param.getString("rawTransaction");
                return ELATransaction.decodeRawTransaction(rawTransaction);

            case "genrawtransactionbyprivatekey":
                return ELATransaction.genRawTransactionByPrivateKey(param);

            case "genmultisigntx":
                return ELATransaction.genMultiSignTx(param);

            case "gentxbyaccount":
                return Account.genTxByAccount(param);

            case "importaccount":
                return Account.importAccount(param);

            case "removeaccount":
                return Account.removeAccount(param);

            case "createaccount":
                return Account.createAccount(param);

            case "exportprivatekey":
                return Account.exportPrivateKey(param);

            case "getaccountamount":
                return Account.getAccountAmount();

            case "gencrosschaintx":
                return CrossChainTransaction.genCrossChainTx(param);

            case "gencrosschainmultisigntx":
                return CrossChainTransaction.genCrossChainMultiSignTx(param);

            case "gencrosschaintxbyprivatekey":
                return CrossChainTransaction.genCrossChainTxByPrivateKey(param);

            case "gendeploycontracttx":
                return NeoContractTransaction.genDeployContractTx(param);

            case "geninvokecontracttx":
                return NeoContractTransaction.genInvokeContractTx(param);

            case "getblockcount":
                return ElaApi.getblockcount();

            case "getblockbyheight":
                return ElaApi.getblockbyheight(param);

            case "listunspent":
                return ElaApi.listunspent(param);

            case "getblock":
                return ElaApi.getblock(param);

            case "getrawtransaction":
                return ElaApi.getrawtransaction(param);

            case "sendrawtransaction":
                return ElaApi.sendrawtransaction(param);

            case "getblockhash":
                return ElaApi.getblockhash(param);

            case "discretemining":
                return ElaApi.discretemining(param);

            case "getreceivedbyaddress":
                return ElaApi.getreceivedbyaddress(param);

            default:
                return ErrorCode.ParamErr(method + " method does not exist");
        }
    }
}

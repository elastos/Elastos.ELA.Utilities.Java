package org.elastos.elaweb;

import net.sf.json.JSONObject;
import org.elastos.api.*;
import org.elastos.common.Config;
import org.elastos.common.ErrorCode;


/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/5/20
 */
class ElaController {

    /**
     * 处理请求
     * @param params
     * @return
     * @throws Exception
     */
    static String processMethod(String params) throws Exception {

        //read config
        try {
            Config.getConfig();
        }catch (Exception e){
            return e.toString();
        }

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

            case "genrawtx":
                return ELATransaction.genRawTx(param);

            case "decoderawtx":
                String rawTransaction = param.getString("rawtransaction");
                return ELATransaction.decodeRawTx(rawTransaction);

            case "genrawtxbyprivatekey":
                return ELATransaction.genRawTxByPrivatekey(param);

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

            case "gendidtx":
                return DidTransaction.genDidTx(param);

            case "gendidtxbyprivatekey":
                return DidTransaction.genDidTxByPrivateKey(param);

            case "getblockcount":
                return ElaApi.getblockcount();

            case "estimatesmartfee":
                return ElaApi.estimatesmartfee(param);

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

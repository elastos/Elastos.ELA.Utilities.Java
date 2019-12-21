package org.elastos.ela;


import org.elastos.common.Common;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;
import org.elastos.ela.payload.*;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elastos.ela.Tx.*;

/**
 * Created by nan on 18/1/10.
 */
public class Ela {


    /**
     * 生成并签名交易
     *
     * @param inputs  交易输入
     * @param outputs 交易输出
     * @return 原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs) throws Exception {
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET, inputs, outputs);
        return singleSignTx(tx);
    }


    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRecord payloadRecord) throws Exception {
        Tx tx = Tx.recordTransaction(RECORD, inputs, outputs, payloadRecord);
        return singleSignTx(tx);
    }

    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRegisterAsset payloadRegisterAsset) throws Exception {
        Tx tx = Tx.registerAssetTransaction(REGISTER_ASSET, inputs, outputs, payloadRegisterAsset);
        return singleSignTx(tx);
    }

    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, String memo) throws Exception {
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET, inputs, outputs, memo);
        return singleSignTx(tx);
    }

    public static RawTx deployContractTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadDeploy payloadDeploy) throws Exception {
        Tx tx = Tx.deployContractTransaction(Deploy, inputs, outputs, payloadDeploy);
        return singleSignTx(tx);
    }

    public static RawTx invokenContractTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadInvoke payloadInvoke) throws Exception {
        Tx tx = Tx.invokeContractTransaction(INVOKE, inputs, outputs, payloadInvoke);
        return singleSignTx(tx);
    }

    public static RawTx didTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, String payload) throws Exception {
        Tx tx = Tx.didTransaction(REGISTER_DID, inputs, outputs, payload);
        return singleSignTx(tx);
    }

    public static RawTx singleSignTx(Tx tx) throws Exception {
        byte[][] phashes = tx.getUniqAndOrdedProgramHashes();
        for (byte[] phash : phashes) {
            String privateKey = tx.hashMapPriv.get(DatatypeConverter.printHexBinary(phash));
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));

            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), Common.SUFFIX_STANDARD);
            tx.sign(privateKey, code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.serialize(dos);

        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());

        return new RawTx(txHash, rawTxString);
    }

    public static RawTx multiSignTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeyScript, List<String> privateKeySign, int M) throws Exception {
        //创建交易
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET, inputs, outputs);

        return multiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static RawTx multiSignTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeyScript, List<String> privateKeySign, int M, String memo) throws Exception {
        //创建交易
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET, inputs, outputs, memo);
        return multiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static RawTx multiSignTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeyScript, List<String> privateKeySign, int M, PayloadRecord payloadRecord) throws Exception {
        //创建交易
        Tx tx = Tx.recordTransaction(RECORD, inputs, outputs, payloadRecord);
        return multiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static RawTx multiSignTx(Tx tx, List<String> privateKeyScript, List<String> privateKeySign, int M) throws Exception {
        //创建赎回脚本
        List<PublicX> privateKeyList = new ArrayList<PublicX>();
        for (int j = 0; j < privateKeyScript.size(); j++) {
            privateKeyList.add(new PublicX(privateKeyScript.get(j)));
        }
        byte[] code = ECKey.getMultiSignatureProgram(privateKeyList, M);

        //签名
        for (int i = 0; i < privateKeySign.size(); i++) {
            tx.multiSign(privateKeySign.get(i), code);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.serialize(dos);
        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());
        return new RawTx(txHash, rawTxString);
    }

    /**
     * 生成单签签名交易_跨链
     *
     * @param inputs          交易输入
     * @param outputs         交易输出
     * @param CrossChainAsset 垮链资产的信息
     * @param privateKeySign  用来签名的私钥
     * @return 原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    public static RawTx crossChainSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadTransferCrossChainAsset[] CrossChainAsset, List<String> privateKeySign) throws SDKException {
        try {
            Tx tx = Tx.crossChainTransaction(TRANSFER_CROSS_CHAIN_ASSET, inputs, outputs, CrossChainAsset);
            for (int i = 0; i < privateKeySign.size(); i++) {
                ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKeySign.get(i)));
                byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), Common.SUFFIX_STANDARD);
                tx.sign(privateKeySign.get(i), code);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            tx.serialize(dos);

            String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
            String txHash = DatatypeConverter.printHexBinary(tx.getHash());

            return new RawTx(txHash, rawTxString);
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("crossChainSignTx err : " + e));
        }
    }

    /**
     * 生成单签签名交易_跨链
     *
     * @param inputs          交易输入
     * @param outputs         交易输出
     * @param CrossChainAsset 垮链资产的信息
     * @param privateKeySign  用来签名的私钥
     * @return 原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    public static RawTx crossChainMultiSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadTransferCrossChainAsset[] CrossChainAsset, List<String> privateKeyScript, List<String> privateKeySign, int M) throws Exception {
        Tx tx = Tx.crossChainTransaction(TRANSFER_CROSS_CHAIN_ASSET, inputs, outputs, CrossChainAsset);

        return multiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    /**
     * generate privateKey
     *
     * @return
     */
    public static String getPrivateKey() {
        ECKey ec = new ECKey();
        return DatatypeConverter.printHexBinary(ec.getPrivateKeyBytes());
    }

    /**
     * generate publicKey
     *
     * @param privateKey
     * @return
     */
    public static String getPublicFromPrivate(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return DatatypeConverter.printHexBinary(ec.getPubBytes());
    }

    /**
     * generate address
     *
     * @param privateKey
     * @return
     */
    public static String getAddressFromPrivate(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return ec.toAddress();
    }

    /**
     * generate identity id
     *
     * @param privateKey
     * @return
     */
    public static String getIdentityIDFromPrivate(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return ec.toIdentityID();
    }

    /**
     * generate multi sian address
     *
     * @return
     * @throws Exception
     */
    public static String getMultiSignAddress(List<String> privateKey, int M) throws SDKException {

        List<PublicX> privateKeyList = new ArrayList<PublicX>();
        for (int i = 0; i < privateKey.size(); i++) {
            privateKeyList.add(new PublicX(privateKey.get(i)));
        }
        ECKey ec = new ECKey();
        return ec.toMultiSignAddress(privateKeyList, M);
    }

    /**
     * generate contract hash and contract address
     *
     * @param contract
     * @return
     */
    public static String genNeoContractHash(String contract) {
        byte[] codeByte = DatatypeConverter.parseHexBinary(contract);
        byte[] codeHash = Util.ToCodeHash(codeByte, Common.PREFIX_CONTRANCT);
        return DatatypeConverter.printHexBinary(codeHash);
    }

    /**
     * generate contractAddress
     *
     * @param contractHash
     * @return
     * @throws SDKException
     */
    public static String genNeoContractAddress(String contractHash) throws SDKException {
        return ECKey.toNeoContranctAddress(contractHash);
    }
}

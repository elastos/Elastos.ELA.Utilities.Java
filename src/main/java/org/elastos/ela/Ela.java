package org.elastos.ela;



import org.elastos.common.SDKException;
import org.elastos.common.Util;
import org.elastos.ela.contract.FunctionCode;
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
     * @param inputs    交易输入
     * @param outputs   交易输出
     * @return  原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    public static RawTx makeAndSignTx(utxoTxInput[] inputs, TxOutput[] outputs) throws Exception {
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET,inputs, outputs);
        return singleSignTx(tx);
    }


    public static RawTx makeAndSignTx(utxoTxInput[] inputs, TxOutput[] outputs, PayloadRecord payloadRecord) throws Exception {
        Tx tx = Tx.recordTransaction(RECORD, inputs, outputs, payloadRecord);
        return singleSignTx(tx);
    }

    public static RawTx makeAndSignTx(utxoTxInput[] inputs, TxOutput[] outputs, PayloadRegisterAsset payloadRegisterAsset) throws Exception {
        Tx tx = Tx.registerAssetTransaction(REGISTER_ASSET, inputs, outputs, payloadRegisterAsset);
        return singleSignTx(tx);
    }

    public static RawTx makeAndSignTx(utxoTxInput[] inputs, TxOutput[] outputs, String memo) throws Exception {
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET, inputs, outputs, memo);
        return singleSignTx(tx);
    }

    public static RawTx deployContractTransaction(utxoTxInput[] inputs, TxOutput[] outputs, FunctionCode functionCode , PayloadDeploy payloadDeploy) throws Exception {
        Tx tx = Tx.deployContractTransaction(Deploy, inputs, outputs,functionCode,payloadDeploy);
        return singleSignTx(tx);
    }

    public static RawTx invokenContractTransaction(utxoTxInput[] inputs, TxOutput[] outputs, PayloadInvoke payloadInvoke) throws Exception {
        Tx tx = Tx.invokeContractTransaction(INVOKE, inputs, outputs, payloadInvoke);
        return singleSignTx(tx);
    }

    public static RawTx singleSignTx(Tx tx) throws  Exception{
        byte[][] phashes = tx.getUniqAndOrdedProgramHashes();
        for(int i=0;i<phashes.length;i++){
            String privateKey = tx.hashMapPriv.get(DatatypeConverter.printHexBinary(phashes[i]));
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));

            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(),1);
            tx.sign(privateKey,code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.serialize(dos);

        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());

        return new RawTx(txHash,rawTxString);
    }

    public static RawTx multiSignTransaction(utxoTxInput[] inputs, TxOutput[] outputs , List<String> privateKeyScript , List<String> privateKeySign , int M) throws Exception {
        //创建交易
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET,inputs, outputs);

        return multiSignTx(tx, privateKeyScript, privateKeySign , M);
    }

    public static RawTx multiSignTransaction(utxoTxInput[] inputs, TxOutput[] outputs , List<String> privateKeyScript , List<String> privateKeySign , int M , String memo) throws Exception {
        //创建交易
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET,inputs, outputs,memo);
        return multiSignTx(tx, privateKeyScript, privateKeySign , M);
    }

    public static RawTx multiSignTransaction(utxoTxInput[] inputs, TxOutput[] outputs , List<String> privateKeyScript , List<String> privateKeySign , int M , PayloadRecord payloadRecord) throws Exception {
        //创建交易
        Tx tx = Tx.recordTransaction(RECORD,inputs, outputs,payloadRecord);
        return multiSignTx(tx, privateKeyScript, privateKeySign , M);
    }

    public static RawTx multiSignTx(Tx tx , List<String> privateKeyScript , List<String> privateKeySign , int M ) throws Exception {
        //创建赎回脚本
        List<PublicX> privateKeyList = new ArrayList<PublicX>();
        for (int j = 0 ; j < privateKeyScript.size() ; j++) {
            privateKeyList.add(new PublicX(privateKeyScript.get(j)));
        }
        byte[] code = ECKey.getMultiSignatureProgram(privateKeyList , M);

        //签名
        for (int i = 0 ; i < privateKeySign.size()  ; i++) {
            tx.multiSign(privateKeySign.get(i), code);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.serialize(dos);
        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash =  DatatypeConverter.printHexBinary(tx.getHash());
        return  new RawTx(txHash,rawTxString);
    }

    /**
     * 生成单签签名交易_跨链
     * @param inputs    交易输入
     * @param outputs   交易输出
     * @param CrossChainAsset  垮链资产的信息
     * @param privateKeySign   用来签名的私钥
     * @return  原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    public static RawTx crossChainSignTx(utxoTxInput[] inputs, TxOutput[] outputs , PayloadTransferCrossChainAsset[] CrossChainAsset , List<String> privateKeySign) throws Exception {
        Tx tx = Tx.crossChainTransaction(TRANSFER_CROSS_CHAIN_ASSET, inputs, outputs ,CrossChainAsset);
        System.out.println("CrossChainAsset : " + CrossChainAsset.length);
        for(int i = 0 ; i < privateKeySign.size() ; i ++){
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKeySign.get(i)));
            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(),1);
            tx.sign(privateKeySign.get(i), code);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.serialize(dos);

        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());

        return new RawTx(txHash,rawTxString);
    }

    /**
     * 生成单签签名交易_跨链
     * @param inputs    交易输入
     * @param outputs   交易输出
     * @param CrossChainAsset  垮链资产的信息
     * @param privateKeySign   用来签名的私钥
     * @return  原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    public static RawTx crossChainMultiSignTx(utxoTxInput[] inputs, TxOutput[] outputs , PayloadTransferCrossChainAsset[] CrossChainAsset , List<String> privateKeyScript , List<String> privateKeySign , int M) throws Exception {
        Tx tx = Tx.crossChainTransaction(TRANSFER_CROSS_CHAIN_ASSET, inputs, outputs ,CrossChainAsset);

        return multiSignTx(tx, privateKeyScript, privateKeySign , M);
    }

    /**
     * 生成私钥
     * @return
     */
    public static String getPrivateKey(){
        ECKey ec = new ECKey();
        return DatatypeConverter.printHexBinary(ec.getPrivateKeyBytes());
    }

    /**
     * 根据私钥获得公钥
     * @param privateKey
     * @return
     */
    public static String getPublicFromPrivate(String privateKey){
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return DatatypeConverter.printHexBinary(ec.getPubBytes());
    }

    /**
     * 根据私钥获得地址
     * @param privateKey
     * @return
     */
    public static String getAddressFromPrivate(String privateKey){
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return ec.toAddress();
    }

    /**
     * 根据私钥获得身份id
     * @param privateKey
     * @return
     */
    public static String getIdentityIDFromPrivate(String privateKey){
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return ec.toIdentityID();
    }

    /**
     *  生成多签地址
     * @throws Exception
     * @return
     */
    public static String getMultiSignAddress(List<String> privateKey , int M) throws SDKException {

        List<PublicX> privateKeyList = new ArrayList<PublicX>();
        for (int i = 0 ; i < privateKey.size() ; i++) {
            privateKeyList.add(new PublicX(privateKey.get(i)));
        }
        ECKey ec = new ECKey();
        return  ec.toMultiSignAddress(privateKeyList , M);
    }
}

package org.elastos.ela;

import org.elastos.common.Common;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static org.elastos.ela.Tx.*;

/**
 * Created by donglei on 18/6/6.
 */
public class SignTxAbnormal {

    /**
     * 生成并签名交易
     * @param inputs    交易输入
     * @param outputs   交易输出
     * @return  原始交易数据 可以使用rest接口api/v1/transaction发送给节点
     * @throws IOException
     */
    static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign) throws SDKException {
        Tx tx = Tx.newTransferAssetTransaction(Tx.TRANSFER_ASSET,inputs, outputs);
        return SingleSignTx(tx,privateKeySign);
    }

    static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign, PayloadRecord payloadRecord) throws SDKException {
        Tx tx = Tx.recordTransaction(RECORD,inputs, outputs,payloadRecord);
        return SingleSignTx(tx,privateKeySign);
    }

    static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeySign, String memo) throws SDKException {
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET,inputs, outputs,memo);
        return SingleSignTx(tx,privateKeySign);
    }

    static RawTx makeSingleSignTxByDid(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeySign, String payload) throws SDKException {
        Tx tx = Tx.didTransaction(REGISTER_DID,inputs, outputs,payload);
        return SingleSignTx(tx,privateKeySign);
    }

    static RawTx makeSingleSignTxByToken(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign, PayloadRegisterAsset payload) throws SDKException {
        Tx tx = Tx.registerAssetTransaction(REGISTER_ASSET,inputs, outputs,payload);
        return SingleSignTx(tx,privateKeySign);
    }

    private static RawTx SingleSignTx(Tx tx, List<String> privateKeySign) throws SDKException {

        try {
            for (String s : privateKeySign) {
                ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(s));
                byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), Common.SUFFIX_STANDARD);
                tx.sign(s, code);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            tx.serialize(dos);

            String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
            String txHash =  DatatypeConverter.printHexBinary(tx.getHash());
            return  new RawTx(txHash,rawTxString);
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("SingleSignTx err : " + e));
        }

    }

}

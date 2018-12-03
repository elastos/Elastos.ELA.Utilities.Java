package org.elastos.ela;

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

import static org.elastos.ela.Tx.RECORD;
import static org.elastos.ela.Tx.REGISTER_ASSET;
import static org.elastos.ela.Tx.TRANSFER_ASSET;

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
    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign) throws SDKException {
        Tx tx = Tx.newTransferAssetTransaction(Tx.TRANSFER_ASSET,inputs, outputs);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign, PayloadRecord payloadRecord) throws SDKException {
        Tx tx = Tx.recordTransaction(RECORD,inputs, outputs,payloadRecord);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign, String memo) throws SDKException {
        Tx tx = Tx.newTransferAssetTransaction(TRANSFER_ASSET,inputs, outputs,memo);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx makeSingleSignTxByToken(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign, PayloadRegisterAsset payload) throws SDKException {
        Tx tx = Tx.registerAssetTransaction(REGISTER_ASSET,inputs, outputs,payload);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx SingleSignTx(Tx tx,List<String> privateKeySign) throws SDKException {

        try {
            for(int i = 0 ; i < privateKeySign.size() ; i ++){
                ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKeySign.get(i)));
                byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(),1);
                tx.sign(privateKeySign.get(i), code);
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

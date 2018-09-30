package org.elastos.ela;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

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
    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign,PayloadRecord payloadRecord) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs,payloadRecord);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs , List<String> privateKeySign,String memo) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs,memo);
        return SingleSignTx(tx,privateKeySign);
    }

    public static RawTx SingleSignTx(Tx tx,List<String> privateKeySign) throws IOException {
        for(int i = 0 ; i < privateKeySign.size() ; i ++){
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKeySign.get(i)));
            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(),1);
            tx.sign(privateKeySign.get(i), code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        tx.Serialize(dos);

        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash =  DatatypeConverter.printHexBinary(tx.getHash());
        return  new RawTx(txHash,rawTxString);
    }

}

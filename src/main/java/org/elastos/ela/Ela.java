package org.elastos.ela;



import net.sf.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction( inputs, outputs);
        return SingleSignTx(tx);
    }


    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs,PayloadRecord payloadRecord) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction( inputs, outputs, payloadRecord);
        return SingleSignTx(tx);
    }

    public static RawTx SingleSignTx (Tx tx) throws  IOException{
        byte[][] phashes = tx.getUniqAndOrdedProgramHashes();
        for(int i=0;i<phashes.length;i++){
            String privateKey = tx.hashMapPriv.get(DatatypeConverter.printHexBinary(phashes[i]));
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));

            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(),1);
            tx.sign(privateKey,code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.Serialize(dos);

        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());

        return new RawTx(txHash,rawTxString);
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
}

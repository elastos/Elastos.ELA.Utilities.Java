package org.elastos.ela;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.chart.PieChart;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    public static String makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction( inputs, outputs);
        byte[][] phashes = tx.getUniqAndOrdedProgramHashes();
        for(int i=0;i<phashes.length;i++){
            String privateKey = tx.hashMapPriv.get(DatatypeConverter.printHexBinary(phashes[i]));
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));

            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes());
            tx.sign(i,privateKey,code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        tx.Serialize(dos);

        return DatatypeConverter.printHexBinary(baos.toByteArray());

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



}

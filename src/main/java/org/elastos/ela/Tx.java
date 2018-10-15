package org.elastos.ela;

import org.elastos.common.SDKException;
import org.elastos.ela.bitcoinj.Sha256Hash;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by nan on 18/1/10.
 */
public class Tx {
    byte TxType;
    byte PayloadVersion;
    PayloadRecord parloadRecord;
    PayloadRegisterAsset payloadRegisterAsset;
    PayloadTransferCrossChainAsset[] CrossChainAsset;
    TxAttribute[] Attributes;
    UTXOTxInput[] UTXOInputs;
    //BalanceInputs  []*BalanceTxInput
    TxOutput[] Outputs;
    long LockTime; //uint32
    List<Program> Programs;
    byte[] hash;    //256byte

    public static byte CoinBase                = 0x00;
    public static byte RegisterAsset           = 0x01;
    public static byte TransferAsset           = 0x02;
    public static byte Record                  = 0x03;
    public static byte Deploy                  = 0x04;
    public static byte SideChainPow            = 0x05;
    public static byte RechargeToSideChain     = 0x06;
    public static byte WithdrawFromSideChain   = 0x07;
    public static byte TransferCrossChainAsset = 0x08;
    public static byte RegisterIdentification  = 0x09;

    Map<String,String> hashMapPriv = new HashMap<String,String>();

    public void sign(String privateKey,byte[] code) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.SerializeUnsigned(dos);

        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        this.Programs.add(new Program(code,signature));

        return;
    }

    public void multiSign(String privateKey,byte[] code) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.SerializeUnsigned(dos);

        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        Program  program =  new Program(code,signature);
        if (this.Programs.size() == 0){
            this.Programs.add(program);
        }else {
            //合并signature
            byte[] byte_buf = new byte[this.Programs.get(0).Parameter.length + program.Parameter.length];
            System.arraycopy(this.Programs.get(0).Parameter,0,byte_buf,0,this.Programs.get(0).Parameter.length);
            System.arraycopy(program.Parameter,0,byte_buf,this.Programs.get(0).Parameter.length,program.Parameter.length);

            this.Programs.get(0).Parameter = byte_buf;
        }
    }

    public static Tx  NewTransferAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs) {

        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList<Program>();

        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs

        return tx;
    }

    public static Tx  NewTransferAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs , String memo) {

        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList<Program>();

        TxAttribute ta = TxAttribute.NewTxNonceAttribute(memo);
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs

        return tx;
    }

    // TODO 交易类型合并处理
    public static Tx  NewTransferAssetTransaction(byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs,PayloadRecord parloadRecord) {

        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Attributes = new TxAttribute[1];
        tx.parloadRecord = parloadRecord;
        tx.Programs = new ArrayList<Program>();

        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs

        return tx;
    }

    // TODO 交易类型合并处理
    public static Tx  NewTransferAssetTransaction(byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs,PayloadRegisterAsset payloadRegisterAsset) {

        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Attributes = new TxAttribute[1];
        tx.payloadRegisterAsset = payloadRegisterAsset;
        tx.Programs = new ArrayList<Program>();

        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs

        return tx;
    }

    // TODO 交易类型合并处理
    public static Tx  NewCrossChainTransaction(byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs , PayloadTransferCrossChainAsset[] CrossChainAsset) {

        Tx tx = new Tx();
        tx.CrossChainAsset = CrossChainAsset;
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList<Program>();

        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs

        return tx;
    }


    //Serialize the SingleSignTransaction
    public void Serialize(DataOutputStream o) throws Exception {
        SerializeUnsigned(o);

        //Serialize  SingleSignTransaction's programs
        Util.WriteVarUint(o,Programs.size());


        if (Programs.size() > 0) {
            for (Program p : this.Programs) {
                p.Serialize(o);
            }
        }
        return;
    }

    public static Map DeSerialize(DataInputStream o) throws IOException {
        return DeSerializeUnsigned(o);
    }

    public byte[][] getUniqAndOrdedProgramHashes() {


        String[] keys = (String[])hashMapPriv.keySet().toArray(new String[0]);

        byte[][] rhashes = new byte[keys.length][];
        for(int i=0;i<keys.length;i++){
            rhashes[i] = DatatypeConverter.parseHexBinary(keys[i]);
        }

        Util.sortByteArrayArrayUseRevertBytesSequence(rhashes);


        return rhashes;
    }



    public void SetPrograms(List<Program> programs) {
        this.Programs = programs;
    }

    public List<Program> GetPrograms() {

        return this.Programs;
    }

    //Serialize the SingleSignTransaction data without contracts
    public void SerializeUnsigned(DataOutputStream o) throws Exception{
        //txType
        //w.Write([]byte{byte(tx.TxType)})
        o.writeByte(this.TxType);

        //PayloadVersion
        //w.Write([]byte{tx.PayloadVersion})
        o.writeByte(this.PayloadVersion);

        //PayloadRecord
        if (this.parloadRecord != null){
            this.parloadRecord.Serialize(o);
        }

        //PayloadRecord
        if ( this.CrossChainAsset != null){
            Util.WriteVarUint(o, this.CrossChainAsset.length);
            for (PayloadTransferCrossChainAsset ca : this.CrossChainAsset)
                ca.Serialize(o);
        }

        //payloadRegisterAsset
        if ( this.payloadRegisterAsset != null) {
//            Util.WriteVarUint(o, 1);
            this.payloadRegisterAsset.Serialize(o);
        }
        //[]*txAttribute
        Util.WriteVarUint(o, this.Attributes.length);
        if (this.Attributes.length > 0) {
            for (TxAttribute arr : this.Attributes) {
                arr.Serialize(o);
            }
        }
        //[]*UTXOInputs
        Util.WriteVarUint(o,this.UTXOInputs.length);
        if (this.UTXOInputs.length > 0) {
            for  (UTXOTxInput utxo : this.UTXOInputs) {
                utxo.Serialize(o);
            }
        }

        //[]*Outputs
        Util.WriteVarUint(o,this.Outputs.length);
        if (this.Outputs.length > 0) {
            for (TxOutput output : this.Outputs) {
                output.Serialize(o);
            }
        }

        o.writeInt(Integer.reverseBytes((int)this.LockTime));
        return;
    }

    public static Map DeSerializeUnsigned(DataInputStream o) throws IOException {
        //txType
        byte TxType = o.readByte();

        //PayloadVersion
        byte PayloadVersion = o.readByte();

        //[]*txAttribute
        long len = 0;
        len = Util.ReadVarUint(o);
        TxAttribute.DeSerialize(o);

        //[]*UTXOInputs
        len =  Util.ReadVarUint(o);
        List<Map>  inputList = new LinkedList<Map>();
        for (int i = 0 ; i < len ; i++){
            Map inputMap = UTXOTxInput.DeSerialize(o);
            inputList.add(inputMap);
        }

        //[]*Outputs
        List<Map> outputList = new LinkedList<Map>();
        len =  Util.ReadVarUint(o);
        for (int i = 0 ; i < len ; i++){
            Map outputMap = TxOutput.DeSerialize(o);
            outputList.add(outputMap);
        }

        Map<String, List<Map>> resultmap = new LinkedHashMap<String, List<Map>>();
        resultmap.put("UTXOInputs:",inputList);
        resultmap.put("Outputs:",outputList);
        return resultmap;
    }

    public byte[] getHash() throws Exception {
        if(this.hash == null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            this.SerializeUnsigned(dos);
            byte[] txUnsigned = baos.toByteArray();
            Sha256Hash sh = Sha256Hash.twiceOf(txUnsigned);
            this.hash = sh.getReversedBytes();
        }
        return this.hash;
    }
    
    public byte[] GetMessage() {
        return new byte[0];
    }
}


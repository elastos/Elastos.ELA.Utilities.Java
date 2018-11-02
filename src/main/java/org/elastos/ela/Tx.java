package org.elastos.ela;

import org.elastos.ela.bitcoinj.Sha256Hash;
import org.elastos.ela.contract.FunctionCode;
import org.elastos.ela.payload.PayloadDeploy;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.elastos.ela.payload.PayloadTransferCrossChainAsset;

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
    private byte TxType;
    private byte PayloadVersion;
    private TxAttribute[] Attributes;
    private UTXOTxInput[] UTXOInputs;
    private TxOutput[] Outputs;
    private int LockTime; //uint32
    private List<Program> Programs;
    private byte[] hash;    //256byte

    private PayloadRecord parloadRecord;
    private PayloadRegisterAsset payloadRegisterAsset;
    private PayloadTransferCrossChainAsset[] CrossChainAsset;
    private PayloadDeploy payloadDeploy;
    private FunctionCode functionCode;

    static byte CoinBase                = 0x00;
    static byte RegisterAsset           = 0x01;
    static byte TransferAsset           = 0x02;
    static byte Record                  = 0x03;
    static byte Deploy                  = 0x04;
    static byte SideChainPow            = 0x05;
    static byte RechargeToSideChain     = 0x06;
    static byte WithdrawFromSideChain   = 0x07;
    static byte TransferCrossChainAsset = 0x08;
    static byte RegisterIdentification  = 0x09;
    static byte Invoke                  = 0x0A;

    Map<String,String> hashMapPriv = new HashMap<String,String>();

    // sigin sign
    public void sign(String privateKey,byte[] code) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.SerializeUnsigned(dos);
        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        this.Programs.add(new Program(code,signature));
    }

    // multi sign
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

    //Ordinary transaction
    public static Tx  NewTransferAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs) {
        Tx tx = new Tx();
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    //Ordinary transaction memo
    public static Tx  NewTransferAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs , String memo) {
        Tx tx = new Tx();
        commonalityTransaction(tx,TransactionType,inputs,outputs,memo);
        return tx;
    }

    // Record Transaction
    public static Tx  RecordTransaction(byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs,PayloadRecord parloadRecord) {
        Tx tx = new Tx();
        tx.parloadRecord = parloadRecord;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // Register Aseet
    public static Tx  RegisterAssetTransaction(byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs,PayloadRegisterAsset payloadRegisterAsset) {
        Tx tx = new Tx();
        tx.payloadRegisterAsset = payloadRegisterAsset;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // CrossChain Transaction
    public static Tx  CrossChainTransaction(byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs , PayloadTransferCrossChainAsset[] CrossChainAsset) {
        Tx tx = new Tx();
        tx.CrossChainAsset = CrossChainAsset;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // CrossChain Transaction
    public static Tx DeployContractTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs, FunctionCode functionCode , PayloadDeploy payloadDeploy) {
        Tx tx = new Tx();
        tx.functionCode = functionCode;
        tx.payloadDeploy = payloadDeploy;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // commonality transaction parameter
    private static void commonalityTransaction(Tx tx, byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs){
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Programs = new ArrayList<Program>();

        tx.Attributes = new TxAttribute[1];
        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs
    }

    private static void commonalityTransaction(Tx tx, byte TransactionType,UTXOTxInput[] inputs, TxOutput[] outputs,String memo){
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Programs = new ArrayList<Program>();

        tx.Attributes = new TxAttribute[1];
        TxAttribute ta = TxAttribute.NewTxNonceAttribute(memo);
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs
    }

    //Serialize the Transaction
    public void Serialize(DataOutputStream o) throws Exception {
        SerializeUnsigned(o);

        //Serialize  Transaction's programs
        Util.WriteVarUint(o,Programs.size());


        if (Programs.size() > 0) {
            for (Program p : this.Programs) {
                p.Serialize(o);
            }
        }
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

    //Serialize the Transaction data without contracts
    public void SerializeUnsigned(DataOutputStream o) throws Exception{
        //txType
        o.writeByte(this.TxType);

        //PayloadVersion
        o.writeByte(this.PayloadVersion);

        //PayloadRecord
        if (this.parloadRecord != null){
            this.parloadRecord.Serialize(o);
        }

        //CrossChainAsset
        if ( this.CrossChainAsset != null){
            Util.WriteVarUint(o, this.CrossChainAsset.length);
            for (PayloadTransferCrossChainAsset ca : this.CrossChainAsset)
                ca.Serialize(o);
        }

        //payloadRegisterAsset
        if ( this.payloadRegisterAsset != null) {
            this.payloadRegisterAsset.Serialize(o);
        }


        //functionCode
        if ( this.functionCode != null) {
            this.functionCode.Serialize(o);
        }

        //payloadDeploy
        if ( this.payloadDeploy != null) {
            this.payloadDeploy.Serialize(o);
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

        o.writeInt(Integer.reverseBytes(this.LockTime));
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


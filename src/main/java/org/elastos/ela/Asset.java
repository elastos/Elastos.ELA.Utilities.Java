package org.elastos.ela;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.bitcoinj.Sha256Hash;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Asset {
    private String Name;
    private String Description;
    private byte Precision;
    private byte AssetType;
    private byte AssetRecordType;

    public static String AssetId;

    public Asset(String name,String description,byte precision,byte assetType, byte assetRecordType){
        this.Name = name;
        this.Description = description;
        this.Precision = precision;
        this.AssetType = assetType;
        this.AssetRecordType = assetRecordType;

        getAssetId();
    }
    public Asset(String name,String description,byte precision,byte assetType){
        this.Name = name;
        this.Description = description;
        this.Precision = precision;
        this.AssetType = assetType;

        getAssetId();
    }

    public void serialize(DataOutputStream o)throws IOException{
        o.write(this.Name.length());
        o.writeBytes(this.Name);
        o.write(this.Description.length());
        o.writeBytes(this.Description);
        o.write(this.Precision);
        o.write(this.AssetType);
        o.write(this.AssetRecordType);
    }

    private void getAssetId(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            serialize(dos);
            byte[] txUnsigned = baos.toByteArray();
            Sha256Hash sh = Sha256Hash.twiceOf(txUnsigned);
            AssetId = DatatypeConverter.printHexBinary(sh.getReversedBytes());
        }catch (Exception e){
            System.out.println(new SDKException(ErrorCode.ParamErr("generate assetId exception :" + e)));
        }
    }
}

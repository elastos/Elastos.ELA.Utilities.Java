package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.SignTool;
import org.elastos.common.Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class PayloadUpdateProducer {
    private byte[] OwnerPublicKey;
    private byte[] NodePublicKey;
    private String NickName;
    private String Url;
    private long Location;
    private String NetAddress;


    private byte[] Signature;

    private String Amount;

    public PayloadUpdateProducer(byte[] ownerPublicKey, byte[] nodePublicKey, String nickName, String url, long location, String address, byte[] privateKey) throws SDKException {
        this.OwnerPublicKey = ownerPublicKey;
        this.NodePublicKey = nodePublicKey;
        this.NickName = nickName;
        this.Url = url;
        this.Location = location;
        this.NetAddress = address;

        genSignature(privateKey);
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try {
            SerializeUnsigned(o);
            Util.WriteVarBytes(o, this.Signature);
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("PayloadUpdateProducer serialize exception :" + e));
        }
    }

    public void SerializeUnsigned(DataOutputStream o) throws SDKException {
        try {
            Util.WriteVarBytes(o, this.OwnerPublicKey);
            Util.WriteVarBytes(o, this.NodePublicKey);
            Util.WriteVarBytes(o, this.NickName.getBytes());
            Util.WriteVarBytes(o, this.Url.getBytes());

            o.writeLong(Long.reverseBytes(this.Location));

            Util.WriteVarBytes(o, this.NetAddress.getBytes());
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("PayloadUpdateProducer SerializeUnsigned exception :" + e));
        }
    }

    public void genSignature(byte[] privateKey) throws SDKException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        SerializeUnsigned(dos);

        this.Signature = SignTool.doSign(baos.toByteArray(), privateKey);
    }
}

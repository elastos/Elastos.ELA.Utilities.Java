package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.SignTool;
import org.elastos.common.Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ProcessProducer {
    private byte[] OwnerPublicKey;
    private byte[] Signature;

    public ProcessProducer(byte[] ownerPublicKey, byte[] privateKey) throws SDKException {
        this.OwnerPublicKey = ownerPublicKey;
        genSignature(privateKey);
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try {
            SerializeUnsigned(o);
            Util.WriteVarBytes(o, this.Signature);
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("PayloadRegisterProducer serialize exception :" + e));
        }
    }

    public void SerializeUnsigned(DataOutputStream o) throws SDKException {
        try {
            Util.WriteVarBytes(o, this.OwnerPublicKey);
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("ProcessProducer serialize exception :" + e));
        }
    }

    public void genSignature(byte[] privateKey) throws SDKException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        SerializeUnsigned(dos);

        this.Signature = SignTool.doSign(baos.toByteArray(), privateKey);
    }
}

package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;
import org.elastos.ela.contract.FunctionCode;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PayloadDeploy {
    public static FunctionCode Code;
    private String Name;
    private String CodeVersion;
    private String Author;
    private String Email;
    private String Description;
    private byte[] ProgramHash;
    private long   Gas;

    public PayloadDeploy(String name,String codeVersion,String author,String email,String description,byte[] programHash,long gas){
        this.Name = name;
        this.CodeVersion = codeVersion;
        this.Author = author;
        this.Email = email;
        this.Description = description;
        this.ProgramHash = programHash;
        this.Gas = gas;
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try{
            PayloadDeploy.Code.Serialize(o);

            Util.WriteVarBytes(o,this.Name.getBytes());

            Util.WriteVarBytes(o,this.CodeVersion.getBytes());

            Util.WriteVarBytes(o,this.Author.getBytes());

            Util.WriteVarBytes(o,this.Email.getBytes());

            Util.WriteVarBytes(o,this.Description.getBytes());

            o.write(this.ProgramHash);

            o.writeLong(Long.reverseBytes(this.Gas));
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("PayloadDeploy serialize exception :" + e));
        }
    }
}

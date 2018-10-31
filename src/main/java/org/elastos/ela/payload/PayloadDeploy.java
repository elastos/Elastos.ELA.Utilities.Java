package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.contract.FunctionCode;

import java.io.DataOutputStream;

public class PayloadDeploy {
    public static FunctionCode Code;
    private String Name;
    private String CodeVersion;
    private String Author;
    private String Email;
    private String Description;
    public static String ProgramHash;
    private long   Gas;

    public PayloadDeploy(String name,String codeVersion,String author,String email,String description){
        this.Name = name;
        this.CodeVersion = codeVersion;
        this.Author = author;
        this.Email = email;
        this.Description = description;
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try{
            PayloadDeploy.Code.Serialize(o);

            o.write(this.Name.length());
            o.writeBytes(this.Name);

            o.write(this.CodeVersion.length());
            o.writeBytes(this.CodeVersion);

            o.write(this.Author.length());
            o.writeBytes(Author);

            o.write(this.Email.length());
            o.writeBytes(this.Email);

            o.write(this.Description.length());
            o.writeBytes(this.Description);

            o.write(PayloadDeploy.ProgramHash.length());
            o.writeBytes(PayloadDeploy.ProgramHash);

            o.writeLong(Long.reverseBytes(this.Gas)); //TODO this is nesessary to reverse?
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("PayloadDeploy serialize exception :" + e));
        }
    }
}

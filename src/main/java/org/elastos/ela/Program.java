package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by nan on 18/1/10.
 */
public class Program {
    //the contract program code,which will be run on VM or specific envrionment
    byte[] Code;

    //the program code's parameter
    byte[] Parameter;

    public Program(byte[] Code,byte[] Paramter) throws IOException {
        this.Code = Code;

        ProgramBuilder pb =  ProgramBuilder.NewProgramBuilder();
        pb.PushData(Paramter);

        this.Parameter = pb.ToArray();
    }

    //Serialize the Program
    void Serialize(DataOutputStream o) throws IOException {

        Util.WriteVarBytes(o,this.Parameter);
        Util.WriteVarBytes(o,this.Code);

        return;
    }

}

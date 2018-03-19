package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by nan on 18/1/13.
 */
public interface ISignableData extends ISignableObject{
    //Get the the SignableData's program hashes
    byte[][] GetUniqAndOrdedProgramHashes();

    void SetPrograms(Program[] programs);

    Program[] GetPrograms();

        //TODO: add SerializeUnsigned
    void SerializeUnsigned(DataOutputStream o) throws IOException;
}

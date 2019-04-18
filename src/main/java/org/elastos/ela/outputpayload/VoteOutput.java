package org.elastos.ela.outputpayload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import java.io.DataOutputStream;

public class VoteOutput {
    private byte Version;
    private VoteContent[] Contents;

    public static byte VERSION = 0x00;

    public VoteOutput(byte version, VoteContent[] contents) {
        this.Version = version;
        this.Contents = contents;
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try {
            o.write(this.Version);
            Util.WriteVarUint(o, this.Contents.length);
            for (VoteContent Content : this.Contents) {
                Content.Serialize(o);
            }
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("VOTE_OUTPUT serialize exception :" + e));
        }
    }
}
package org.elastos.ela.outputpayload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import java.io.DataOutputStream;
import java.util.List;

public class VoteContent {
    private byte VoteType;
    private List<byte[]> Candidates;

    public static byte DELEGATE = 0x00;
    public static byte CRC = 0x01;

    public VoteContent(byte voteType, List<byte[]> candidates) {
        this.VoteType = voteType;
        this.Candidates = candidates;
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try {
            o.write(this.VoteType);
            Util.WriteVarUint(o, this.Candidates.size());
            for (byte[] candidate : this.Candidates) {
                Util.WriteVarBytes(o, candidate);
            }
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("VoteContent serialize exception :" + e));
        }
    }
}


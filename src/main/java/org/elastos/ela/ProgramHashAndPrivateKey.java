package org.elastos.ela;

/**
 * Created by nan on 18/1/26.
 */
public class ProgramHashAndPrivateKey {
    public byte[] programHash;
    public String privateKey;
    public ProgramHashAndPrivateKey(byte[] programHash,String privateKey){
        this.programHash = programHash;
        this.privateKey = privateKey;
    }
}

package org.elastos.ela;

/**
 * Created by nan on 18/1/27.
 */
public class RawTx {
    private String txHash;

    public String getRawTxString() {
        return rawTxString;
    }

    public void setRawTxString(String rawTxString) {
        this.rawTxString = rawTxString;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    private String rawTxString;
    public RawTx(String txHash, String rawTxString){
        this.txHash = txHash;
        this.rawTxString = rawTxString;
    }
}

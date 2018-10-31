package org.elastos.ela.payload;

import org.elastos.ela.Util;

import java.io.DataOutputStream;
import java.io.IOException;
/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/26
 */
public class PayloadTransferCrossChainAsset {
    private String   CrossChainAddress;
    private int      OutputIndex;
    private long     CrossChainAmount;


    public PayloadTransferCrossChainAsset(String address, long amount , int index){
        this.CrossChainAddress = address;
        this.CrossChainAmount = amount;
        this.OutputIndex = index;
    }


    public void Serialize(DataOutputStream o) throws IOException {
        o.write(this.CrossChainAddress.length());
        o.writeBytes(this.CrossChainAddress);
        Util.WriteVarUint(o,this.OutputIndex);
        o.writeLong(Long.reverseBytes(this.CrossChainAmount));
    }

    public String getCrossChainAddress() {return CrossChainAddress;}
    public int getOutputIndex() {return OutputIndex;}
    public long getCrossChainAmount() {return CrossChainAmount;}
}

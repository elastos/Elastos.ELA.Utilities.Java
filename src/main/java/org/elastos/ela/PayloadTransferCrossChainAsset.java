package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;
/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/26
 */
public class PayloadTransferCrossChainAsset {
    String   CrossChainAddress;
    int      OutputIndex;
    long     CrossChainAmount;


    public PayloadTransferCrossChainAsset(String address, long amount , int index){
        this.CrossChainAddress = address;
        this.CrossChainAmount = amount;
        this.OutputIndex = index;
    }


    void Serialize(DataOutputStream o) throws IOException {
        o.write(this.CrossChainAddress.length());
        o.writeBytes(this.CrossChainAddress);
        Util.WriteVarUint(o,this.OutputIndex);
        o.writeLong(Long.reverseBytes(this.CrossChainAmount));
    }
}

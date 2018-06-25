package org.elastos.ela;

public class UTXOInputSort implements Comparable<UTXOInputSort>{

    private String txid;
    private String address;
    private long amount;
    private int vont;


    public UTXOInputSort(String txid, String address , int vont , long amount){
        this.txid       = txid;
        this.address    = address;
        this.amount     = amount;
        this.vont       = vont;
    }


    public int compareTo(UTXOInputSort o) {
        return new Long(this.amount).compareTo(o.amount);

    }

    public String getTxid(){
        return  this.txid;
    }

    public String getAddress() {
        return this.address;
    }

    public long getAmount(){
        return  this.amount;
    }

    public int getVont(){
        return  this.vont;
    }
}

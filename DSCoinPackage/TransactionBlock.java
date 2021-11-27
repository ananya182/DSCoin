package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    trarray=new Transaction[t.length];
    for(int i=0;i<trarray.length;i++)
      trarray[i]=t[i];
    this.previous=null;
    this.dgst=null;
    MerkleTree m=new MerkleTree();
    trsummary=m.Build(t);
    this.Tree=m;}

  public int count(String cid,Transaction[] arr){
    int c=0;
    for(int i=0;i<arr.length;i++){
      if(arr[i].coinID.equals(cid))
        c++;
    }
    return c;
  }

  public boolean checkTransaction (Transaction t) {

    TransactionBlock b=t.coinsrc_block;
    if(b==null)
      return true;

    if(count(t.coinID,b.trarray)!=1)
      return false;

    TransactionBlock cur=this.previous;
    int f=0;

    while(cur!=null){
      if(cur==b){
        f++;
        break;
      }
      cur=cur.previous;}

    if(f==0)
      return false;

    for(int i=0;i<b.trarray.length;i++){
      if(b.trarray[i].coinID.equals(t.coinID)){
        if(!(b.trarray[i].Destination==t.Source))
          return false;
        break;
      }}

    cur=this.previous;

    while(cur!=b){
      if(count(t.coinID,cur.trarray)!=0)
        return false;
      cur=cur.previous;
    }

      return true;

  }
}

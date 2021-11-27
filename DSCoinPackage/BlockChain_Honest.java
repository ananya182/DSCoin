package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;
  CRF obj=new CRF(64);

  public void InsertBlock_Honest (TransactionBlock newBlock) {

    int start=1000000001;
    String s;

    if(this.lastBlock==null){
      s=obj.Fn(start_string+"#"+newBlock.trsummary+"#"+Integer.toString(start));

      while(!s.substring(0,4).equals("0000")) {
        start++;
        s = obj.Fn(start_string + "#" + newBlock.trsummary + "#" + Integer.toString(start));
      }
    }

    else{
      s=obj.Fn(this.lastBlock.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);

      while(!s.substring(0,4).equals("0000")){
        start++;
        s=obj.Fn(this.lastBlock.dgst+"#"+newBlock.trsummary+"#"+Integer.toString(start));
      }
    }

    newBlock.nonce = Integer.toString(start);
    newBlock.dgst=s;
    newBlock.previous=this.lastBlock;
    this.lastBlock=newBlock;
  }
}

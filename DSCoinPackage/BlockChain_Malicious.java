package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF obj=new CRF(64);
    String z;

    if(tB.previous==null)
      z=start_string;
    else
      z=tB.previous.dgst;

    if(tB.dgst.substring(0,4).equals("0000") && obj.Fn(z+"#" + tB.trsummary+"#" + tB.nonce).equals(tB.dgst)){

      List<String> q = new ArrayList<String>();
      String val;

      for(int i=0;i<tB.trarray.length;i++) {
        val=tB.trarray[i].coinID;
        if (tB.trarray[i].Source == null)
          val = val + "#" + "Genesis";
        else
          val = val + "#" + tB.trarray[i].Source.UID;

        val = val + "#" + tB.trarray[i].Destination.UID;

        if (tB.trarray[i].coinsrc_block == null)
          val = val + "#" + "Genesis";
        else
          val = val + "#" + tB.trarray[i].coinsrc_block.dgst;

        q.add(obj.Fn(val));

      }
      String l,r;
      while (q.size() > 1) {
        l = q.get(0);
        q.remove(0);
        r = q.get(0);
        q.remove(0);
        q.add(obj.Fn(l+"#"+r));
      }

      if(q.get(0).equals(tB.trsummary)){
        for(int i=0;i<tB.trarray.length;i++){
          if(!tB.checkTransaction(tB.trarray[i]))
            return false;
        }
        return true;

    }
    return false;}

    return false;
  }

  public TransactionBlock f(TransactionBlock b){

    TransactionBlock cur=b;
    TransactionBlock inv=null;

    while(cur!=null){
      if(!checkTransactionBlock(cur))
        inv=cur;
      cur=cur.previous;
    }
    if(inv==null)
      return b;

    return inv.previous;
  }

  public int ct(TransactionBlock b){
    if(b==null)
      return 0;
    else
      return 1+ct(b.previous);
  }

  public TransactionBlock FindLongestValidChain() {
    TransactionBlock[] list=this.lastBlocksList;
    if(list[0]==null)
      return null;
    List<TransactionBlock> q = new ArrayList<TransactionBlock>();
    for(int i=0;i<list.length;i++){
      if(list[i]==null)
        break;
      q.add(f(list[i]));
    }
    int maxlen=Integer.MIN_VALUE;

    TransactionBlock b=null;

    for(int i=0;i<q.size();i++){
      if(ct(q.get(i))>maxlen){
        maxlen=ct(q.get(i));
        b=q.get(i);}
    }
    return b;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {

    TransactionBlock b=FindLongestValidChain();

    CRF obj=new CRF(64);
    int start=1000000001;
    String s;

    if(b==null){
      s=obj.Fn(start_string+"#"+newBlock.trsummary+"#"+Integer.toString(start));

      while(!s.substring(0,4).equals("0000")){
        start++;
        s=obj.Fn(start_string+"#"+newBlock.trsummary+"#"+Integer.toString(start));
      }

      newBlock.nonce=Integer.toString(start);
      newBlock.dgst=s;
      newBlock.previous=b;
        this.lastBlocksList[0]=newBlock;
      }

      else{
      s=obj.Fn(start_string+"#"+newBlock.trsummary+"#"+Integer.toString(start));

      while(!s.substring(0,4).equals("0000")){
        start++;
        s=obj.Fn(b.dgst+"#"+newBlock.trsummary+"#"+Integer.toString(start));
      }

      newBlock.nonce=Integer.toString(start);
      newBlock.dgst=s;
      newBlock.previous=b;

    TransactionBlock[] list=this.lastBlocksList;

    for(int i=0;i<list.length;i++){
      if(list[i]==b){
        list[i]=newBlock;
        return;
      }}

    TransactionBlock[] lnew=new TransactionBlock[100];

    int i;

    for(i=0;i<list.length;i++){
      if(list[i]==null)
        break;
      lnew[i]=list[i];}

    lnew[i]=newBlock;
    this.lastBlocksList=lnew;}}
}

package DSCoinPackage;

import HelperClasses.Pair;

import java.util.ArrayList;
import java.util.List;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {

   String coin="100000";
   int n=DSObj.memberlist.length;

   int j=0;

   Members mem=new Members();

   mem.UID="Moderator";
   mem.mycoins=new ArrayList<Pair<String, TransactionBlock>>();
   mem.in_process_trans=new Transaction[100];

   List<Transaction>tlist=new ArrayList<Transaction>();

   for(int i=0;i<coinCount;i++){
    Transaction t=new Transaction();
    t.Source=mem;
    t.coinsrc_block=null;
    t.coinID=coin;
    t.Destination=DSObj.memberlist[j];
    tlist.add(t);
    DSObj.memberlist[j].mycoins.add(new Pair<String,TransactionBlock>(coin,null));
    j++;
    j%=n;
    coin=Integer.toString(Integer.parseInt(coin)+1);
   }

   DSObj.latestCoinID=Integer.toString(Integer.parseInt(coin)-1);
   Transaction arr[]=new Transaction[DSObj.bChain.tr_count];

   List<TransactionBlock> tblist=new ArrayList<TransactionBlock>();

   for(int i=0;i<coinCount;i++){

    if(i%DSObj.bChain.tr_count==0 && i>0){
     TransactionBlock tb=new TransactionBlock(arr);
     DSObj.bChain.InsertBlock_Honest(tb);
     tblist.add(tb);
     arr=new Transaction[DSObj.bChain.tr_count];
    }

    arr[i%DSObj.bChain.tr_count]=tlist.get(i);
   }

   TransactionBlock tb=new TransactionBlock(arr);
   DSObj.bChain.InsertBlock_Honest(tb);
   tblist.add(tb);


   for(int i=0;i<tblist.size();i++){
    for(int k=0;k<tblist.get(i).trarray.length;k++)
     upd(tblist.get(i).trarray[k].Destination.mycoins,tblist.get(i).trarray[k].coinID,tblist.get(i));
   }
  }


  public void upd(List<Pair<String, TransactionBlock>>l,String cid,TransactionBlock tb){
   for(int i=0;i<l.size();i++){

    if(l.get(i).first.equals(cid)){
     l.set(i,new Pair<String,TransactionBlock>(cid,tb));
     return;}
   }

  }
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
   String coin="100000";
   int n=DSObj.memberlist.length;
   int j=0;
   Members mem=new Members();
   mem.UID="Moderator";
   mem.mycoins=new ArrayList<Pair<String, TransactionBlock>>();
   mem.in_process_trans=new Transaction[100];

   List<Transaction>tlist=new ArrayList<Transaction>();

   for(int i=0;i<coinCount;i++){
    Transaction t=new Transaction();
    t.Source=mem;
    t.coinsrc_block=null;
    t.coinID=coin;
    t.Destination=DSObj.memberlist[j];
    tlist.add(t);
    DSObj.memberlist[j].mycoins.add(new Pair<String,TransactionBlock>(coin,null));
    j++;
    j%=n;
    coin=Integer.toString(Integer.parseInt(coin)+1);
   }

   DSObj.latestCoinID=Integer.toString(Integer.parseInt(coin)-1);
   Transaction arr[]=new Transaction[DSObj.bChain.tr_count];

   List<TransactionBlock> tblist=new ArrayList<TransactionBlock>();

   for(int i=0;i<coinCount;i++){

    if(i%DSObj.bChain.tr_count==0 && i>0){
     TransactionBlock tb=new TransactionBlock(arr);
     DSObj.bChain.InsertBlock_Malicious(tb);
     tblist.add(tb);
     arr=new Transaction[DSObj.bChain.tr_count];
    }

    arr[i%DSObj.bChain.tr_count]=tlist.get(i);
   }

   TransactionBlock tb=new TransactionBlock(arr);
   DSObj.bChain.InsertBlock_Malicious(tb);
   tblist.add(tb);


   for(int i=0;i<tblist.size();i++){
    for(int k=0;k<tblist.get(i).trarray.length;k++)
     upd(tblist.get(i).trarray[k].Destination.mycoins,tblist.get(i).trarray[k].coinID,tblist.get(i));
   }

  }
}

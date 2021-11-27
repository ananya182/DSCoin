package DSCoinPackage;

import java.util.*;

import HelperClasses.MerkleTree;
import HelperClasses.Pair;
import HelperClasses.TreeNode;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins=new ArrayList<>();
  public Transaction[] in_process_trans=new Transaction[100];

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
   Pair<String, TransactionBlock> p;
   p=mycoins.get(0);
   mycoins.remove(0);
   Transaction t=new Transaction();
   t.coinID=p.first;
   t.Source=this;
   for(int i=0;i<DSobj.memberlist.length;i++){
    if(DSobj.memberlist[i].UID.equals(destUID)){
     t.Destination=DSobj.memberlist[i];
     break;
    }}
   t.coinsrc_block=p.second;
   Transaction[] newl=new Transaction[this.in_process_trans.length];
   int i;

   for(i=0;i<this.in_process_trans.length;i++){
    if(newl[i]==null)
     break;
    newl[i]=this.in_process_trans[i];}

   newl[i]=t;
   this.in_process_trans=newl;

   DSobj.pendingTransactions.AddTransactions(t);

  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
   Pair<String, TransactionBlock> p;
   p=mycoins.get(0);
   mycoins.remove(0);
   Transaction t=new Transaction();
   t.coinID=p.first;
   t.Source=this;

   for(int i=0;i<DSobj.memberlist.length;i++){
    if(DSobj.memberlist[i].UID.equals(destUID)){
     t.Destination=DSobj.memberlist[i];
     break;
    }}

   t.coinsrc_block=p.second;
   Transaction[] newl=new Transaction[this.in_process_trans.length];
   int i;

   for(i=0;i<this.in_process_trans.length;i++){
    if(newl[i]==null)
     break;
    newl[i]=this.in_process_trans[i];}

   newl[i]=t;
   this.in_process_trans=newl;

   DSobj.pendingTransactions.AddTransactions(t);

  }

  public boolean chk(TransactionBlock b,Transaction t){
   for(int i=0;i<b.trarray.length;i++){
    if(b.trarray[i]==t)
     return true;
   }
   return false;
  }

  public void sibling(List<Pair<String, String>> l, TreeNode t, MerkleTree m){
   if(t.parent==null){
    l.add(new Pair<String, String>(t.val,null));
    return;}
   else
    l.add(new Pair<String, String>(t.parent.left.val,t.parent.right.val));
   sibling(l,t.parent,m);
  }

  public TreeNode find(String s, TreeNode n){
   if(n.left==null){
    if(n.val.equals(s))
     return n;
    else
     return null;
   }
   else{
    if(find(s,n.left)==null)
     return find(s,n.right);
    return find(s,n.left);
   }
  }

  public void sort(List<Pair<String, TransactionBlock>> l){
   int n = l.size();
   Pair<String, TransactionBlock> temp;
   for(int i=0; i < n; i++){
    for(int j=1; j < (n-i); j++){
     if(l.get(j-1).first.compareTo(l.get(j).first)>0){
      temp = l.get(j-1);
      l.set(j-1, l.get(j));
      l.set(j,temp);
     }}}}

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
   TransactionBlock b=DSObj.bChain.lastBlock;
   int f=0;
   while(b!=null){
    if(chk(b,tobj)){
     f++;
     break;}
   b=b.previous;}

   if(f==0)
    throw new MissingTransactionException();

   List<Pair<String, String>> l=new ArrayList<Pair<String, String>>();
   List<Pair<String, String>> n=new ArrayList<Pair<String, String>>();
   List<Pair<String, String>> q=new ArrayList<Pair<String, String>>();


   sibling(l,find(b.Tree.get_str(tobj),b.Tree.rootnode),b.Tree);

   TransactionBlock cur=DSObj.bChain.lastBlock;

   while(cur!=b){
    n.add(new Pair<String, String>(cur.dgst,cur.previous.dgst+"#"+cur.trsummary+"#"+cur.nonce));
    cur=cur.previous;
   }

   if(b.previous==null){
    n.add(new Pair<String, String>(b.dgst,"DSCoin"+"#"+b.trsummary+"#"+b.nonce));
    n.add(new Pair<String, String>("DSCoin",null));
   }

   else{
    n.add(new Pair<String, String>(b.dgst,b.previous.dgst+"#"+b.trsummary+"#"+b.nonce));
    n.add(new Pair<String, String>(b.previous.dgst,null));}

   int z=n.size();

   for(int i=z-1;i>=0;i--){
    q.add(n.get(i));
   }

   Transaction[] tl=new Transaction[100];
   int j=0;


   for(int i=0;i<this.in_process_trans.length;i++){
    if(this.in_process_trans[i]!=tobj){
     tl[j]=this.in_process_trans[i];
     j++;
    }}

   this.in_process_trans=tl;

   tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID,b));
   sort(tobj.Destination.mycoins);
   return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(l,q);
  }

  public boolean chk(List<Transaction>l,Transaction t){
   for(int i=0;i<l.size();i++){
    if(l.get(i).coinID.equals(t.coinID))
     return false;
   }
   return true;
  }

  public void MineCoin(DSCoin_Honest DSObj) {

   int n=DSObj.bChain.tr_count-1;
   List<Transaction> l=new ArrayList<Transaction>();
   Transaction t=new Transaction();

  while(l.size()!=n){
    try{
     t=DSObj.pendingTransactions.RemoveTransaction();}
    catch(Exception e){
     System.out.println("empty queue");}
    if(chk(l,t) && chtr(DSObj.bChain.lastBlock,t))
     l.add(t);}


   Transaction min=new Transaction();
   min.coinsrc_block=null;
   min.Destination=this;
   min.Source=null;
   min.coinID=Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
   l.add(min);

   Transaction[] tar=new Transaction[l.size()];

   for(int i=0;i<l.size();i++)
    tar[i]=l.get(i);

   TransactionBlock bl=new TransactionBlock(tar);
   DSObj.bChain.InsertBlock_Honest(bl);
   DSObj.latestCoinID= min.coinID;
   mycoins.add(new Pair<String, TransactionBlock>(min.coinID, bl));

  }

  public int count(String cid,Transaction[] arr){
   int c=0;
   for(int i=0;i<arr.length;i++){
    if(arr[i].coinID.equals(cid))
     c++;
   }
   return c;
  }

  public boolean chtr (TransactionBlock bl,Transaction t) {

   TransactionBlock b=t.coinsrc_block;
   if(b==null)
    return true;

   if(count(t.coinID,b.trarray)!=1)
    return false;

   TransactionBlock cur=bl;
   int f=0;

   while(cur!=null){
    if(cur==b){
     f++;
     break;
    }
    cur=cur.previous;
   }

   if(f==0)
    return false;

   for(int i=0;i<b.trarray.length;i++){
    if(b.trarray[i].coinID.equals(t.coinID)){
     if(!(b.trarray[i].Destination==t.Source))
      return false;
     break;
    }
   }

   cur=bl;

   while(cur!=b){
    if(count(t.coinID,cur.trarray)!=0)
     return false;
    cur=cur.previous;
   }

   return true;

  }

  public void MineCoin(DSCoin_Malicious DSObj) {

   int n=DSObj.bChain.tr_count-1;
   List<Transaction> l=new ArrayList<Transaction>();
   Transaction t=new Transaction();
   TransactionBlock b=DSObj.bChain.FindLongestValidChain();

   while(l.size()!=n){
    try{
     t=DSObj.pendingTransactions.RemoveTransaction();}
    catch(Exception e){
     System.out.println("empty queue");}
    if(chk(l,t) && chtr(b,t))
     l.add(t);}


   Transaction min=new Transaction();
   min.coinsrc_block=null;
   min.Destination=this;
   min.Source=null;
   min.coinID=Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
   l.add(min);

   Transaction[] tar=new Transaction[l.size()];

   for(int i=0;i<l.size();i++)
    tar[i]=l.get(i);

   TransactionBlock bl=new TransactionBlock(tar);
   DSObj.bChain.InsertBlock_Malicious(bl);
   DSObj.latestCoinID= min.coinID;
   mycoins.add(new Pair<String, TransactionBlock>(min.coinID, bl));


  }  
}

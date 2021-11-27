package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {

    if(this.lastTransaction==null)
      this.firstTransaction=transaction;
    else
      this.lastTransaction.next=transaction;

    transaction.next=null;

    this.lastTransaction=transaction;
    this.numTransactions++;
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {

    if(this.numTransactions==1){
      Transaction cur=this.firstTransaction;
      this.firstTransaction=null;
      this.lastTransaction=null;
      this.numTransactions--;
      return cur;
    }
    else if(this.numTransactions==0){
      throw new EmptyQueueException();
    }
    else{
      Transaction cur=this.firstTransaction;
      this.firstTransaction=this.firstTransaction.next;
      this.numTransactions--;
      return cur;
    }
  }

  public int size() {
    return this.numTransactions;
  }
}

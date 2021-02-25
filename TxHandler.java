import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;

public class TxHandler {

	/* Creates a public ledger whose current UTXOPool (collection of unspent 
	 * transaction outputs) is utxoPool. This should make a defensive copy of 
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	private UTXOPool utxoPool;
	public TxHandler(UTXOPool utxoPool) {
		this.utxoPool = new UTXOPool(utxoPool);
		// IMPLEMENT THIS
	}

	/* Returns true if 
	 * (1) all outputs claimed by tx are in the current UTXO pool, 
	 * (2) the signatures on each input of tx are valid, 
	 * (3) no UTXO is claimed multiple times by tx, 
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of   
	        its output values;
	   and false otherwise.
	 */

	public boolean isValidTx(Transaction tx) {
		double total_input = 0;

		ArrayList<UTXO> temp_unique_utox = new ArrayList<>();
		int tx_size = tx.numInputs();

		for(int i = 0 ; i < tx_size;i ++){ // we are going through the entire input of tx 
			Transaction.Input holds_input = tx.getInput(i); // get all the tx info
			if(holds_input == null ) return false; // if it doesnt have a value it automatically becomes false 
			UTXO temp = new UTXO(holds_input.prevTxHash,holds_input.outputIndex); // after that makes a temporary UTXO s
			Transaction.Output holds_output = utxoPool.getTxOutput(temp); // with the temporary UTXO makes a temporoy that holds the output 
			//1 
			if(utxoPool.contains(temp)==false) // we check with the temp and see if it insde the pool of utxo thats located in system 
				return false;
				RSAKey answer = holds_output.address;
			
			
			if(answer.verifySignature(tx.getRawDataToSign(i), holds_input.signature)== false) return false;
			//2 
			
			//3
			if(temp_unique_utox.contains(temp)) return false;
			temp_unique_utox.add(temp);
			total_input+= holds_output.value;

			
		}
		
		boolean test4 = true;
		boolean test5 = true;
		
		// For each of the conditions, if the condition is false, set boolean to false.
		
		// Condition 4: All tx's outputs values must be non-negative.
		// Create new array list of outputs.
		ArrayList<Transaction.Output> testOutputs = tx.getOutputs();
		
		// Create double variable as a sum of outputs.
		double total_Output = 0;
		
		// If outputs are empty, then abort and return false.
		if (testOutputs.isEmpty() == true)
		{
			return false;
		}
		
		// Look through the outputs and check to see if each output is non-negative.
		// If output is not negative, add the output to the sum.
		for (int i = 0; i < testOutputs.size(); i++)
		{
			Transaction.Output eachOut = testOutputs.get(i);
			if (eachOut.value <= 0)
			{
				test4 = false;
			}
			else
			{
				total_Output += eachOut.value;
			}
			
			if (test4 == false)
			{
				// If the output is negative, break out of the for loop.
				break;
			}
		}
		
		// Condition 5: The sum of tx's input values must not be less than the sum of tx's output values.
		if (total_input < total_Output)
		{
			test5 = false;
		}
		
		// Return true only if all two tests return true.
		if (test4 == false || test5 == false)
		{
			return false;
		}
		
		// IMPLEMENT THIS
		return true;
	}

	/* Handles each epoch by receiving an unordered array of proposed 
	 * transactions, checking each transaction for correctness, 
	 * returning a mutually valid array of accepted transactions, 
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		
		
		 LinkedList<Transaction> Trans = new LinkedList<Transaction>();
                int count = 0;
	for (Transaction Transc : possibleTxs)
			 
     {
	if(isValidTx(Transc))              //goes in if statmenet , if transaction is correct
	   {
		    Trans.add(Transc);               // add to the linkedlist
		    
		 for(Transaction.Input Input : Transc.getInputs())           //  the loop is to remove the UTXO from the pool
		 {
			 
		    UTXO Utxo =new UTXO(Input.prevTxHash,Input.outputIndex);
		       utxoPool.removeUTXO(Utxo);    // calls removeUTXO from UTXOPool file

		 }
		 
		for(Transaction.Output Output : Transc.getOutputs())      // the loop  is to add new UTXO to the pool
		{	
		     byte[]Txhash = Transc.getHash();
		     
			UTXO Utxo = new UTXO(Txhash,count);
			utxoPool.addUTXO(Utxo,Output);           // calls addUTXO from UTXOPool file
			
			count =+ 1;
		}
			 
	   }	 
     }       
		 Transaction[] TranscValid = new Transaction[Trans.size()];  
		
         
		return TranscValid;   // return mutually valid array of the only accepted transactions
		
		
		
		
   }

} 

import java.util.Vector;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import static java.lang.System.*;
import java.util.*;




public class MaxFeeTxHandler {

	/* Creates a public ledger whose current UTXOPool (collection of unspent 
	 * transaction outputs) is utxoPool. This should make a defensive copy of 
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	private UTXOPool utxoPool;
	public MaxFeeTxHandler(UTXOPool utxoPool) {
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
		double total_input = 0; // get the total amount of the input 
		UTXOPool pool_tx = new UTXOPool(); // we make a pool of tx 
		int tx_size = tx.numInputs(); // get the sze of the tx 

		for(int i = 0 ; i < tx_size;i ++){ // we are going through the entire input of tx 
			Transaction.Input holds_input = tx.getInput(i); // get all the tx info
			if(holds_input == null ) return false; // if it doesnt have a value it automatically becomes false 
			UTXO temp = new UTXO(holds_input.prevTxHash,holds_input.outputIndex); // after that makes a temporary UTXO s
			Transaction.Output holds_output = utxoPool.getTxOutput(temp); // with the temporary UTXO makes a temporoy that holds the output 
			//1 
			if(utxoPool.contains(temp)==false){// we check with the temp and see if it insde the pool of utxo thats located in system 
				return false;
			}
			RSAKey answer = holds_output.address; // makes a refrence to the address 
			
			
			if(answer.verifySignature(tx.getRawDataToSign(i), holds_input.signature)== false) {return false;} // verifes if not UTXO is claimed twice
			//2 
			
			//3
			if(pool_tx.contains(temp)) {return false;} // return false if pool_tx contains that certin one 
			pool_tx.addUTXO(temp, holds_output); // adds it to the UTXO pool 
			total_input+= holds_output.value; // gets the total 

			
		}
		
		
		
		// For each of the conditions, if the condition is false, set boolean to false.
		
		// For each of the conditions, if the condition is false, set boolean to false.
		
		// Condition 4: All tx's outputs values must be non-negative.
		// Create new array list of outputs.
	
		
		// Create double variable as a sum of outputs.
		double total_Output = 0;
		
		
		// Look through the outputs and check to see if each output is non-negative.
		// If output is not negative, add the output to the sum.
		for (int i = 0; i < tx.numOutputs(); i++)
		{
			Transaction.Output eachOut = tx.getOutput(i);
			if ( eachOut== null) {return false;}
			if (eachOut.value < 0)
			{
				return false;
			}
			
				total_Output += eachOut.value;
			
			
		}
		
		// Condition 5: The sum of tx's input values must not be less than the sum of tx's output values.
		if (total_input <  total_Output)
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
	
		double[] txs_amount = new double[possibleTxs.length];
		//Vector<Double> txs_amount = new Vector<Double>(array_length);
		for (int i = 0; i < possibleTxs.length; i++) {
			
		double calc_all_input = 0.0;
		for (int k = 0; k < possibleTxs[i].numInputs(); k++) {
			Transaction.Input temp_input_value = possibleTxs[i].getInput(k);
			UTXO temp_UTXO = new UTXO(temp_input_value.prevTxHash, temp_input_value.outputIndex);
			Transaction.Output tx_ouput = utxoPool.getTxOutput(temp_UTXO);
			if (isValidTx(possibleTxs[i])) {
				calc_all_input += tx_ouput.value;
				//out.println("the output is = " + tx_ouput.value);	
			}
		}
		double calc_all_output = 0.0;
		for (int j = 0; j < possibleTxs[i].numOutputs(); j++)
		{
			Transaction.Output temp_output_value = possibleTxs[i].getOutput(j);
			calc_all_output += temp_output_value.value;
		}
		//	out.println("the output is = " + possibleTxs[i].numOutputs());
		//	out.println("The input is = " + possibleTxs[i].numInputs());
		
		double temp_holder = calc_all_input - calc_all_output;
		txs_amount[i] = temp_holder;
	}
		int l = 0;
		// https://en.wikipedia.org/wiki/Selection_sort
		while (l < txs_amount.length) {
			int m = l;
			while (m+1 < txs_amount.length) {
				if (txs_amount[l] < txs_amount[m]) {
					double temp_holder_double;
					Transaction temp_holder_trans;
					temp_holder_trans = possibleTxs[l];
					possibleTxs[l] = possibleTxs[m];
					possibleTxs[m] = temp_holder_trans;
					temp_holder_double = txs_amount[l];
					txs_amount[l] = txs_amount[m];
					txs_amount[m] = temp_holder_double;
				}
				m++;
			}
			l++;
		}
				
	LinkedList<Transaction> Trans = new LinkedList<Transaction>();
                
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
		 
		 byte[]Txhash = Transc.getHash();
		for(int i = 0 ; i<  Transc.numOutputs();++i)      // the loop  is to add new UTXO to the pool
		{	
		     
		     
			UTXO Utxo = new UTXO(Txhash,i);
			utxoPool.addUTXO(Utxo,Transc.getOutput(i));           // calls addUTXO from UTXOPool file
			
			//count =+ 1;
		}
			 
	   }
	else{
	       continue;
	    }
     }       
		 Transaction[] TranscValid = new Transaction[Trans.size()];  
		
                Trans.toArray(TranscValid);  // Organizes the transactions before returning 
		return TranscValid;   // return mutually valid array of the only accepted transactions
		
		
		
		
   }

} 

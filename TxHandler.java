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
		int tx_size = tx.numInputs();
		for(int i = 0 ; i < tx_size;i ++){ // we are going through the entire input of tx 
			Transaction.Input holds_input = tx.getInput(i); // get all the tx info
			if(holds_input == null ) return false; // if it doesnt have a value it automatically becomes false 
			UTXO temp = new UTXO(holds_input.prevTxHash,holds_input.outputIndex); // after that makes a temporary UTXO s
			Transaction.Output holds_output = utxoPool.getTxOutput(temp); // with the temporary UTXO makes a temporoy that holds the output 
			//1 
			if(utxoPool.contains(temp)==false){ // we check with the temp and see if it insde the pool of utxo thats located in system 
				return false;
			}
			if()
			
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
		// IMPLEMENT THIS
		return null;
	}

} 

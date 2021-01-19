package logic.data_io.data_load;

import logic.TransactionsManager;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class TransactionDeserializationLoader extends DataDeserialization {
	
	public void load(String path_to_dir) {
		super.load(path_to_dir, "t");
	}

	@Override
	protected void loadItem(Object item) {
		try {
			TransactionsManager.getInstance().addTransaction((Transaction) item);
		} catch (InvalidTransactionException | ResumenNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

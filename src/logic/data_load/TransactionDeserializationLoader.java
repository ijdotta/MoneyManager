package logic.data_load;

import logic.TransactionsManager;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class TransactionDeserializationLoader extends DataDeserialization {

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

package logic.data_io.data_load;

import logic.TransactionsManager;
import logic.transactions.User;
import logic.transactions.Transaction;
import logic.transactions.TransactionSnapshot;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.UserNotFoundException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class TransactionDeserializationLoader extends DataDeserialization {

	public void load(String path_to_dir) throws UserNotFoundException {
		super.load(path_to_dir, "t");
	}

	@Override
	protected void loadItem(Object item) throws UserNotFoundException {
		try {
			TransactionsManager manager = TransactionsManager.getInstance();
			TransactionSnapshot snap = (TransactionSnapshot) item;
			Transaction transaction;
			
			User pagador = manager.getUser(snap.getPagador_id());
			transaction = new Transaction(snap.getImporte(), snap.getConcepto(), snap.getFecha(), snap.getNotas(),
					pagador, (User[]) null);

			for (Integer beneficiario_id : snap.getBeneficiarios_id()) {
				transaction.addBeneficiarios(manager.getUser(beneficiario_id));
			}

			TransactionsManager.getInstance().addTransaction(transaction);
		} catch (InvalidTransactionException | ResumenNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

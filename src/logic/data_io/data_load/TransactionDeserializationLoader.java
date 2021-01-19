package logic.data_io.data_load;

import logic.TransactionsManager;
import logic.transactions.Participant;
import logic.transactions.Transaction;
import logic.transactions.TransactionSnapshot;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ParticipantNotFoundException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class TransactionDeserializationLoader extends DataDeserialization {

	public void load(String path_to_dir) throws ParticipantNotFoundException {
		super.load(path_to_dir, "t");
	}

	@Override
	protected void loadItem(Object item) throws ParticipantNotFoundException {
		try {
			TransactionsManager manager = TransactionsManager.getInstance();
			TransactionSnapshot snap = (TransactionSnapshot) item;
			Transaction transaction;
			
			Participant pagador = manager.getParticipant(snap.getPagador_id());
			transaction = new Transaction(snap.getImporte(), snap.getConcepto(), snap.getFecha(), snap.getNotas(),
					pagador, (Participant[]) null);

			for (Integer beneficiario_id : snap.getBeneficiarios_id()) {
				transaction.addBeneficiarios(manager.getParticipant(beneficiario_id));
			}

			manager.getInstance().addTransaction(transaction);
		} catch (InvalidTransactionException | ResumenNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

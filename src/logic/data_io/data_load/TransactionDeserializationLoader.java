package logic.data_io.data_load;

import java.util.ArrayList;
import java.util.List;

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
/*			// DEBUGGING
			System.out.println("Loading transaction " + item.toString());

			for (Participant p : ((Transaction) item).getBeneficiarios()) {
				if (TransactionsManager.getInstance().getParticipants().contains(p)) {
					System.out.println("FOUND BENEFICIARIO OF NEW TRANSACTION IN MANAGER");
				}
				else {
					System.out.println("CANT FOUND BENEFICIARIO XXXXX");
				}
			}
			// END DEBUGGING
*/
			TransactionsManager manager = TransactionsManager.getInstance();
			TransactionSnapshot snap = (TransactionSnapshot) item;
			Transaction transaction;
			Participant pagador = null;
			List<Participant> beneficiarios = new ArrayList<>(6);
			int pagador_id = snap.getPagador_id();
			
			// TODO por ahora usar así, luego cambiar en TransactionsManager por Map<Integer, Participant>
			
			for (Participant p : manager.getParticipants()) {
				if (p.getId() == pagador_id) {
					pagador = p;
					break;
				}
			}
			if (pagador == null) throw new ParticipantNotFoundException("Could't find participant in loding");
			
			for (Integer beneficiario_id : snap.getBeneficiarios_id()) {
				for (Participant p : manager.getParticipants()) {
					if (p.getId() == beneficiario_id) {
						beneficiarios.add(p);
						break;
					}
				}
				System.out.println("break solo breaks el de adentro!");
			}
			
			transaction = new Transaction(snap.getImporte(), snap.getConcepto(), snap.getFecha(), snap.getNotas(), pagador, (Participant[]) null);
			for (Participant beneficiario : beneficiarios)
				transaction.addBeneficiarios(beneficiario);
			
			
			TransactionsManager.getInstance().addTransaction(transaction);
		} catch (InvalidTransactionException | ResumenNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

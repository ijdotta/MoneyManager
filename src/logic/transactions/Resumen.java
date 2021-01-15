package logic.transactions;

import java.util.LinkedList;
import java.util.List;

import logic.data_exportation.Exportable;
import logic.data_exportation.ExportationVisitor;
import logic.transactions.exceptions.TransactionNotFoundException;

public class Resumen implements Exportable, TransactionModificationSensitive {

	protected float amount;
	protected List<Transaction> transactions;
	protected Participant actor;

	public Resumen(Participant actor) {
		super();
		this.actor = actor;
		this.transactions = new LinkedList<Transaction>();
	}

	@Override
	public void add(Transaction transaction) {
		this.transactions.add(transaction);
		this.amount += transaction.getAmount();
	}

	@Override
	public void remove(Transaction transaction) throws TransactionNotFoundException {
		if (!this.transactions.remove(transaction)) {
			throw new TransactionNotFoundException(
					"Transaction #" + transaction.getId() + ": " + transaction.getConcepto() + " not found. ");
		}
		
		this.amount -= transaction.getAmount();
	}

	@Override
	public void export(ExportationVisitor exportationVisitor) {
		exportationVisitor.visit(this);
	}

	public float getImporte() {
		return amount;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

}

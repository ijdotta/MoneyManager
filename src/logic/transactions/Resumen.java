package logic.transactions;

import java.util.LinkedList;
import java.util.List;

import logic.data_io.data_exportation.Exportable;
import logic.data_io.data_exportation.ExportationVisitor;
import logic.transactions.exceptions.TransactionNotFoundException;

public class Resumen implements TransactionModificationSensitive, Exportable {

	protected float amount;
	protected List<Transaction> transactions;
	protected User actor;

	public Resumen(User actor) {
		super();
		this.actor = actor;
		this.transactions = new LinkedList<Transaction>();
	}

	@Override
	public void add(Transaction transaction) {
		this.transactions.add(transaction);
		this.amount += transaction.getAmount() / transaction.getBeneficiarios().size();
	}

	@Override
	public void remove(Transaction transaction) throws TransactionNotFoundException {
		if (!this.transactions.remove(transaction)) {
			throw new TransactionNotFoundException(
					"Transaction #" + transaction.getId() + ": " + transaction.getConcepto() + " was not found in " + this.toString() + ". ");
		}

		this.amount -= transaction.getAmount() / transaction.getBeneficiarios().size();
	}

	public float getImporte() {
		return amount;
	}
	
	public User getActor() {
		return actor;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	@Override
	public int hashCode() {
		return this.actor.getId();
	}
	
	@Override
	public String toString() {
		return "Resumen: " + this.actor.toString();
	}

	@Override
	public void export(ExportationVisitor exportationVisitor) {
		exportationVisitor.visit(this);		
	}

}

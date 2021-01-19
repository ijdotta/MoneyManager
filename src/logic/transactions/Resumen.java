package logic.transactions;

import java.util.LinkedList;
import java.util.List;

import logic.transactions.exceptions.TransactionNotFoundException;

public class Resumen implements TransactionModificationSensitive {

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

}

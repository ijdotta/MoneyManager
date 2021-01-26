package logic.transactions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.data_io.data_exportation.Exportable;
import logic.data_io.data_exportation.ExportationVisitor;
import logic.transactions.exceptions.TransactionNotFoundException;

public class Balance implements TransactionModificationSensitive, Exportable {
	
	protected static Logger logger;

	protected float saldo;
	protected List<Transaction> transactions;
	protected User user1, user2;
	
	public Balance(User user1, User user2) {
		setUpLogger();
		
		this.user1 = user1;
		this.user2 = user2;
		transactions = new LinkedList<>();
	}

	public Debt getDebt() {
		User deudor, beneficiario;
		
		if (saldo < 0) {
			deudor = user1;
			beneficiario = user2;
		}
		else {
			deudor = user2;
			beneficiario = user1;
		}
		
		return new Debt(Math.abs(saldo) /*TODO/ 2*/, deudor, beneficiario, this);
	}

	@Override
	public void add(Transaction transaction) {
		User pagador = transaction.getPagador();
		float amount = transaction.getAmount() / transaction.getBeneficiarios().size();
		
		transactions.add(transaction);
		
		if (pagador.equals(user2)) {
			amount = -amount;
		}
		
		saldo += amount;
	}

	@Override
	public void remove(Transaction transaction) throws TransactionNotFoundException {
		
		if (! this.transactions.remove(transaction) ) {
			logger.warning("Transaction not found");
			throw new TransactionNotFoundException("Transaction #" + transaction.getId() + ": " + transaction.getConcepto() + " was not found in " + this.toString() + ". ");
		}
		
		User pagador = transaction.getPagador();
		float amount = transaction.getAmount();
		
		if (pagador.equals(user2)) {
			amount = -amount;
		}
		
		this.saldo -= amount;

	}

	/**
	 * Returns a list containing the users of the current balance.
	 * It is safe to modify the list since it does not affect the intern state of the balance.
	 * @return 
	 */
	public List<User> getUsers() {
		List<User> users = new ArrayList<>(2);
		users.add(user1);
		users.add(user2);
		return users;
	}
	
	@Override
	public String toString() {
		return "Balance: " + user1.toString() + " - " + user2.toString();
	}
	
	/**
	 * Configura el logger de la clase.
	 */
	private void setUpLogger() {
		if (logger == null) {

			logger = Logger.getLogger(this.getClass().getName());

			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.ALL);
			logger.addHandler(hnd);

			logger.setLevel(Level.ALL);

			Logger rootLoger = logger.getParent();
			for (Handler h : rootLoger.getHandlers())
				h.setLevel(Level.OFF);
		}
	}

	@Override
	public void export(ExportationVisitor exportationVisitor) {
		exportationVisitor.visit(this);
	}

}

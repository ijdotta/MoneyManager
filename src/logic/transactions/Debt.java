package logic.transactions;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.TransactionsManager;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class Debt {

	protected static Logger logger;

	protected float amount;
	protected Participant deudor, beneficiario;
	protected Balance balance;

	public Debt(float amount, Participant deudor, Participant beneficiario, Balance balance) {
		super();
		setUpLogger();
		this.amount = amount;
		this.deudor = deudor;
		this.beneficiario = beneficiario;
		this.balance = balance;
	}

	public float getImporte() {
		return amount;
	}

	public Participant getDeudor() {
		return deudor;
	}

	public Balance getBalance() {
		return balance;
	}

	public void pay(float amount) {
		TransactionsManager manager = TransactionsManager.getInstance();
		String concept = "Pago deuda. Deudor: " + deudor.toString();
		Transaction payment = new Transaction(amount, concept, deudor, beneficiario);
		try {
			manager.addTransaction(payment);
		} catch (InvalidTransactionException e) {
			e.printStackTrace();
			logger.info(
					"No se realizó el pago de la deuda \"" + concept + "\". Transacción con información incompleta. ");
		} catch (ResumenNotFoundException e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
		}
	}
	
	public void pay() {
		pay(this.amount);
	}
	
	@Override
	public String toString() {
		return "$" + this.amount + " : " + deudor.toString() + " -> " + beneficiario.toString();
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

}

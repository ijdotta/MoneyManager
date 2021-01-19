package logic.transactions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.transactions.exceptions.TransactionNotFoundException;

public class Balance implements TransactionModificationSensitive {
	
	protected static Logger logger;

	protected float saldo;
	protected List<Transaction> transactions;
	protected Participant participant1, participant2;
	
	public Balance(Participant participant1, Participant participant2) {
		setUpLogger();
		
		this.participant1 = participant1;
		this.participant2 = participant2;
		transactions = new LinkedList<>();
	}

	public Debt getDebt() {
		Participant deudor, beneficiario;
		
		if (saldo < 0) {
			deudor = participant1;
			beneficiario = participant2;
		}
		else {
			deudor = participant2;
			beneficiario = participant1;
		}
		
		return new Debt(Math.abs(saldo) /*TODO/ 2*/, deudor, beneficiario, this);
	}

	@Override
	public void add(Transaction transaction) {
		Participant pagador = transaction.getPagador();
		float amount = transaction.getAmount() / transaction.getBeneficiarios().size();
		
		transactions.add(transaction);
		
		if (pagador.equals(participant2)) {
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
		
		Participant pagador = transaction.getPagador();
		float amount = transaction.getAmount();
		
		if (pagador.equals(participant2)) {
			amount = -amount;
		}
		
		this.saldo -= amount;

	}

	public List<Participant> getParticipants() {
		List<Participant> participants = new ArrayList<>(2);
		participants.add(participant1);
		participants.add(participant2);
		return participants;
	}
	
	@Override
	public String toString() {
		return "Balance: " + participant1.toString() + " - " + participant2.toString();
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

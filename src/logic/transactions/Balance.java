package logic.transactions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.data_exportation.Exportable;
import logic.data_exportation.ExportationVisitor;
import logic.transactions.exceptions.TransactionNotFoundException;

public class Balance implements Exportable, TransactionModificationSensitive {
	
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
		Participant deudor = saldo < 0 ? participant2 : participant1;
		return new Debt(saldo, deudor, this);
	}

	@Override
	public void add(Transaction transaction) {
		Participant pagador = transaction.getPagador();
		float amount = transaction.getAmount();
		
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
			throw new TransactionNotFoundException("Transaction #" + transaction.getId() + ": " + transaction.getConcepto() + " not found. ");
		}
		
		Participant pagador = transaction.getPagador();
		float amount = transaction.getAmount();
		
		if (pagador.equals(participant2)) {
			amount = -amount;
		}
		
		this.saldo -= amount;

	}

	@Override
	public void export(ExportationVisitor exportationVisitor) {
		exportationVisitor.visit(this);
	}
	
	public List<Participant> getParticipants() {
		List<Participant> participants = new ArrayList<>(2);
		participants.add(participant1);
		participants.add(participant2);
		return participants;
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

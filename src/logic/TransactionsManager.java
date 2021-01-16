package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.transactions.Balance;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidParticipantException;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ParticipantNotFoundException;

public class TransactionsManager {

	private static Logger logger;
	private static TransactionsManager instance;

	protected List<Balance> balances;
	protected HashMap<Participant, Resumen> resumenes;
	protected List<Participant> participants;
	protected HashSet<Transaction> transactions;

	private TransactionsManager() {
		setUpLogger();
		balances = new ArrayList<>(5);
		resumenes = new HashMap<>();
		participants = new ArrayList<>(5);
		transactions = new HashSet<>();
	}

	public static TransactionsManager getInstance() {
		return instance == null ? new TransactionsManager() : instance;
	}

	public void addTransaction(Transaction transaction) throws InvalidTransactionException, ParticipantNotFoundException {
		checkTransaction(transaction);
		updateResumenes(transaction);
		updateBalances(transaction);
	}

	private void updateResumenes(Transaction transaction) throws ParticipantNotFoundException {
		for (Participant beneficiario : transaction.getBeneficiarios()) {
			Resumen resumen = resumenes.get(beneficiario);

			//if resumen==null throw ParticipantNotFoundException
			if (resumen == null) throw new ParticipantNotFoundException("Participant #" + beneficiario.getId() + " not found. ");

			resumen.add(transaction);
			transaction.addCollections(resumen);
		}
	}

	private void updateBalances(Transaction transaction) {
		Set<Balance> balances_to_update = new HashSet<>();
		Participant pagador = transaction.getPagador();
		
		for (Participant current_beneficiario : transaction.getBeneficiarios()) {
			
			Balance target_balance = null;
			
			for (Balance current_balance : this.balances) {
				
				List<Participant> current_balance_participants = current_balance.getParticipants();
				
				// Uso remove pagador para evitar el caso en el que beneficiario y pagador son
				// la misma persona.
				// Remove no afecta a la transacción real porque es una lista externa de
				// elementos.
				if (current_balance_participants.remove(pagador) && current_balance_participants.contains(current_beneficiario)) {
					target_balance = current_balance;
				}
				
			}

			if (target_balance == null) {
				// Crear el balance
				target_balance = new Balance(pagador, current_beneficiario);
				this.balances.add(target_balance);
			}

			balances_to_update.add(target_balance);
		}

		transaction.addCollections((Balance[]) balances_to_update.toArray());
		
		for (Balance balance : balances_to_update) {
			balance.add(transaction);
		}

	}

	public void addParticipant(Participant participant) throws InvalidParticipantException {
		checkParticipant(participant);
		
		//Crear resumen
		Resumen resumen = new Resumen(participant);
		resumenes.put(participant, resumen);
		logger.info("Created resumen for " + participant.toString());

		// TODO
	}

	private void checkParticipant(Participant participant) throws InvalidParticipantException {
		if (participant.getId() == 0) {
			throw new InvalidParticipantException("Participat id == 0. ");
		}
		if (this.participants.contains(participant)) {
			throw new InvalidParticipantException("Participant #" + participant.getId() + " already exists. ");
		}		
	}

	public void removeTransaction(Transaction transaction) {
		// TODO
	}

	private void checkTransaction(Transaction transaction) throws InvalidTransactionException {
		if (transaction.getAmount() == 0) {
			logger.warning("Transaction #" + transaction.getId() + ": " + transaction.getConcepto() + " costs $0. ");
		}
		if (transaction.getPagador() == null || transaction.getBeneficiarios().isEmpty()) {
			throw new InvalidTransactionException("Transaction #" + transaction.getId() + ": "
					+ transaction.getConcepto() + " does not have an expender. ");
		}
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

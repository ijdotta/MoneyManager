package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.data_io.data_exportation.ExportationVisitor;
import logic.data_io.data_load.DataDeserialization;
import logic.data_io.data_load.ParticipantDeserializationLoader;
import logic.data_io.data_load.TransactionDeserializationLoader;
import logic.transactions.Balance;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;
import logic.transactions.TransactionModificationSensitive;
import logic.transactions.exceptions.InvalidParticipantException;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ParticipantNotFoundException;
import logic.transactions.exceptions.ResumenNotFoundException;
import logic.transactions.exceptions.TransactionNotFoundException;

public class TransactionsManager {

	private static Logger logger;
	private static TransactionsManager instance;

	protected List<Balance> balances;
	protected Map<Participant, Resumen> resumenes;
	protected Map<Integer, Participant> participants;
	protected Set<Transaction> transactions;

	private TransactionsManager() {
		setUpLogger();
		balances = new ArrayList<>(5);
		resumenes = new HashMap<>();
		/*
		 * Alternativas: 
		 * 		usar Map para implementar getParticipant(id) en O(1) pero getParticipants : Collection en O(n)
		 * 		usar List para implementar getParticipants() : Collection en O(1) pero getParticipant(id) en O(n)
		 * 
		 * 		En principio, solo se itera sobre participants en el caso de checkParticipant, cuando se añaden nuevos participantes
		 * 		mientras que el getParticipant(id) se utiliza cada vez que se carga una transacción (solo en etapa de carga de app, no en uso).
		 */
		participants = new HashMap<>(5);
		transactions = new HashSet<>();
	}

	public static TransactionsManager getInstance() {
		
		if (instance == null) {
			instance = new TransactionsManager();
		}
		
		return instance;
	}

	public void addTransaction(Transaction transaction) throws InvalidTransactionException, ResumenNotFoundException {
		checkTransaction(transaction);	logger.fine("Passed checkTransaction");
		addTransactionToResumenes(transaction); logger.fine("Passed addToResumen");
		addTransactionToBalances(transaction);	logger.fine("Passed addToBalances");
		
		this.transactions.add(transaction);
	}

	private void addTransactionToResumenes(Transaction transaction) throws ResumenNotFoundException {
		for (Participant beneficiario : transaction.getBeneficiarios()) {
			Resumen resumen = resumenes.get(beneficiario);
			
			logger.warning("Beneficiario hashcode " + beneficiario.hashCode());

			//if resumen==null throw ParticipantNotFoundException
			if (resumen == null) throw new ResumenNotFoundException("Resumen of participant #" + beneficiario.getId() + " not found. ");

			resumen.add(transaction);
			transaction.addCollections(resumen);
		}
	}

	private void addTransactionToBalances(Transaction transaction) {
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
			transaction.addCollections(target_balance);
		}

//		transaction.addCollections((Balance[]) balances_to_update.toArray());
		
		for (Balance balance : balances_to_update) {
			balance.add(transaction);
		}

	}

	public void removeTransaction(Transaction transaction) {
		
		for (TransactionModificationSensitive collection : transaction.getCollections()) {
			
			try {
				collection.remove(transaction);
			} catch (TransactionNotFoundException e) {
				e.printStackTrace();
				logger.warning(e.getMessage());
			}
			
		}
		
		if (this.transactions.remove(transaction)) {
			logger.fine("Transaction " + transaction.toString() + " succesfully removed. ");
		}
		else {
			logger.warning("Transaction " + transaction.toString() + " was not found in manager's transaction set. ");
		}
		
	}

	public void addParticipant(Participant participant) throws InvalidParticipantException {
		checkParticipant(participant);
		
		//Crear resumen
		Resumen resumen = new Resumen(participant);
		resumenes.put(participant, resumen);
		logger.info("Created resumen for " + participant.toString());

		this.participants.put(participant.getId(), participant);
	}

	public List<Balance> getBalances() {
		return balances;
	}

	public Map<Participant, Resumen> getResumenes() {
		return resumenes;
	}

	public Collection<Participant> getParticipants() {
		return participants.values();
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	private void checkParticipant(Participant participant) throws InvalidParticipantException {
		if (participant == null) {
			throw new InvalidParticipantException("Null reference");
		}
		if (participant.getId() == 0) {
			throw new InvalidParticipantException("Participat id == 0. ");
		}
		if (this.participants.containsValue(participant)) {
			throw new InvalidParticipantException("Participant #" + participant.getId() + " already exists. ");
		}		
	}

	private void checkTransaction(Transaction transaction) throws InvalidTransactionException {
		if (transaction == null) {
			throw new InvalidTransactionException("Null reference");
		}
		if (transaction.getAmount() == 0) {
			logger.warning("Transaction #" + transaction.getId() + ": " + transaction.getConcepto() + " costs $0. ");
		}
		if (transaction.getPagador() == null || transaction.getBeneficiarios().isEmpty()) {
			throw new InvalidTransactionException("Transaction #" + transaction.getId() + ": "
					+ transaction.getConcepto() + " does not have an expender. ");
		}
	}
	
	public void export(ExportationVisitor exportationVisitor) {
		for (Transaction t : this.transactions) {
			t.export(exportationVisitor);
		}
		for (Participant p : this.participants.values()) {
			p.export(exportationVisitor);
		}
		
	}
	
	public void load() {
		String path = "./";
		DataDeserialization participant_loader, transaction_loader;
		
		participant_loader = new ParticipantDeserializationLoader();
		transaction_loader = new TransactionDeserializationLoader();
		
		try {
			participant_loader.load(path);
			transaction_loader.load(path);
		} catch (ParticipantNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Participant getParticipant(int id) {
		return this.participants.get(id);
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

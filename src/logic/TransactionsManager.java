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
import logic.data_io.data_load.UserDeserializationLoader;
import logic.data_io.data_load.TransactionDeserializationLoader;
import logic.transactions.Balance;
import logic.transactions.User;
import logic.transactions.Resumen;
import logic.transactions.Transaction;
import logic.transactions.TransactionModificationSensitive;
import logic.transactions.exceptions.InvalidUserException;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.UserNotFoundException;
import logic.transactions.exceptions.ResumenNotFoundException;
import logic.transactions.exceptions.TransactionNotFoundException;

public class TransactionsManager {

	private static Logger logger;
	private static TransactionsManager instance;

	protected List<Balance> balances;
	protected Map<User, Resumen> resumenes;
	protected Map<Integer, User> users;
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
		users = new HashMap<>(5);
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
		for (User beneficiario : transaction.getBeneficiarios()) {
			Resumen resumen = resumenes.get(beneficiario);
			
			logger.warning("Beneficiario hashcode " + beneficiario.hashCode());

			//if resumen==null throw ParticipantNotFoundException
			if (resumen == null) throw new ResumenNotFoundException("Resumen of user #" + beneficiario.getId() + " not found. ");

			resumen.add(transaction);
			transaction.addCollections(resumen);
		}
	}

	private void addTransactionToBalances(Transaction transaction) {
		Set<Balance> balances_to_update = new HashSet<>();
		User pagador = transaction.getPagador();
		
		for (User current_beneficiario : transaction.getBeneficiarios()) {
			
			Balance target_balance = null;
			
			for (Balance current_balance : this.balances) {
				
				List<User> current_balance_users = current_balance.getUsers();
				
				// Uso remove pagador para evitar el caso en el que beneficiario y pagador son
				// la misma persona.
				// Remove no afecta a la transacción real porque es una lista externa de
				// elementos.
				if (current_balance_users.remove(pagador) && current_balance_users.contains(current_beneficiario)) {
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

	public void addUser(User user) throws InvalidUserException {
		checkUser(user);
		
		//Crear resumen
		Resumen resumen = new Resumen(user);
		resumenes.put(user, resumen);
		logger.info("Created resumen for " + user.toString());

		this.users.put(user.getId(), user);
	}

	public List<Balance> getBalances() {
		return balances;
	}

	public Map<User, Resumen> getResumenes() {
		return resumenes;
	}

	public Collection<User> getUsers() {
		return users.values();
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	private void checkUser(User user) throws InvalidUserException {
		if (user == null) {
			throw new InvalidUserException("Null reference");
		}
		if (user.getId() == 0) {
			throw new InvalidUserException("Participat id == 0. ");
		}
		if (this.users.containsKey(user.getId())) {
			throw new InvalidUserException("User #" + user.getId() + " already exists. ");
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
		for (User u : this.users.values()) {
			u.export(exportationVisitor);
		}
		
	}
	
	public void load() {
		String path = "./";
		DataDeserialization user_loader, transaction_loader;
		
		user_loader = new UserDeserializationLoader();
		transaction_loader = new TransactionDeserializationLoader();
		
		try {
			user_loader.load(path);
			transaction_loader.load(path);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public User getUser(int id) {
		return this.users.get(id);
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

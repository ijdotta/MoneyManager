package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import logic.transactions.Balance;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidParticipantException;
import logic.transactions.exceptions.InvalidTransactionException;

public class TransactionsManager {

	private static TransactionsManager instance;

	protected List<Balance> balances;
	protected HashSet<Resumen> resumenes;

	private TransactionsManager() {
		balances = new ArrayList<>(5);
		resumenes = new HashSet<>();
	}

	public static TransactionsManager getInstance() {
		return instance == null ? new TransactionsManager() : instance;
	}

	public void addTransaction(Transaction transaction) throws InvalidTransactionException {
		// TODO
	}

	public void addParticipant(Participant participant) throws InvalidParticipantException {
		// TODO
	}

	public void removeTransaction(Transaction transaction) {
		// TODO
	}

}

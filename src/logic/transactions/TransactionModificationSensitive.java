package logic.transactions;

import logic.transactions.exceptions.TransactionNotFoundException;

public interface TransactionModificationSensitive {

	/**
	 * Actualiza el registro por añadir una nueva transacción.
	 * 
	 * @param transaction a añadir.
	 */
	public void add(Transaction transaction);

	/**
	 * Elimina la transacción recibida y actualiza el registro.
	 * 
	 * @param transaction transacción a remover.
	 * @throws TransactionNotFoundException si no encuentra la transacción.
	 */
	public void remove(Transaction transaction) throws TransactionNotFoundException;

}

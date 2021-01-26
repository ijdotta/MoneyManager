package logic.data_io.data_exportation;

import logic.transactions.Balance;
import logic.transactions.User;
import logic.transactions.Resumen;
import logic.transactions.Transaction;

public interface ExportationVisitor {

	public void visit(Transaction transaction);

	public void visit(User user);

	public void visit(Balance balance);

	public void visit(Resumen resumen);

}

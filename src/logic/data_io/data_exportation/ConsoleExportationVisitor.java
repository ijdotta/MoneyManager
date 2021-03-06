package logic.data_io.data_exportation;

import logic.transactions.Balance;
import logic.transactions.User;
import logic.transactions.Resumen;
import logic.transactions.Transaction;

public class ConsoleExportationVisitor implements ExportationVisitor {

	@Override
	public void visit(Transaction transaction) {
		System.out.println(transaction.toString());
	}

	@Override
	public void visit(User user) {
		System.out.println(user.toString());
	}

	@Override
	public void visit(Balance balance) {
		System.out.println(balance);
	}

	@Override
	public void visit(Resumen resumen) {
		System.out.println(resumen);
	}

}

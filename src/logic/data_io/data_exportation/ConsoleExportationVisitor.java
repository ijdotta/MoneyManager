package logic.data_io.data_exportation;

import logic.transactions.Participant;
import logic.transactions.Transaction;

public class ConsoleExportationVisitor implements ExportationVisitor{

	@Override
	public void visit(Transaction transaction) {
		System.out.println(transaction.toString());
	}

	@Override
	public void visit(Participant participant) {
		System.out.println(participant.toString());
	}

}

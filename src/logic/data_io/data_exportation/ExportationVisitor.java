package logic.data_io.data_exportation;

import logic.transactions.Participant;
import logic.transactions.Transaction;

public interface ExportationVisitor {

	public void visit(Transaction transaction);

	public void visit(Participant participant);

}

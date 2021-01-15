package logic.data_exportation;

import logic.transactions.Balance;
import logic.transactions.Resumen;

public interface ExportationVisitor {

	public void visit(Balance balance);

	public void visit(Resumen resumen);

}

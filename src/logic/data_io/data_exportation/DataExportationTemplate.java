package logic.data_io.data_exportation;

import java.time.LocalDate;

import logic.transactions.Balance;
import logic.transactions.Debt;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;

public abstract class DataExportationTemplate {

	protected String[] getFields(Transaction t) {
		String[] fields = new String[3];

		// getDate
		LocalDate date = t.getFecha();
		fields[0] = String.format("%02d/%02d/%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());

		// getAmount
		fields[1] = String.format("$%.2f", t.getAmount());

		// getConcepto
		fields[2] = String.format("%-65s", t.getConcepto());
		
		// getPagador...

		return fields;
	}
	
	protected String[] getFields(Participant p) {
		String[] fields = new String[2];
		
		fields[0] = String.format("#%06d", p.getId());
		fields[1] = String.format("%30s", p.getName());
		
		return fields;
	}
	
	protected String[] getFields(Balance b) {
		String[] fields = new String[3];
		
		Debt debt = b.getDebt();

		fields[0] = String.format("%30s (D)", debt.getDeudor().getName());
		fields[1] = String.format("%30s", debt.getBeneficiario().getName());
		fields[2] = String.format("$%.2f", debt.getImporte());
		
		return fields;
	}
	
	protected String[] getFields(Resumen r) {
		String[] fields = new String[3];
		
		fields[0] = String.format("%30s", r.getActor().getName());
		fields[1] = String.format("$%.2f", r.getImporte());
		
		return fields;
	}

}

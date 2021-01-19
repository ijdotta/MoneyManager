package logic.data_io.data_exportation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import logic.TransactionsManager;
import logic.transactions.Participant;
import logic.transactions.Transaction;

public class CSVDataExportationVisitor extends DataExportationTemplate implements ExportationVisitor {


	@Override
	protected void exportTransactions(File transactions_file) {
		FileWriter writer;
		Iterable<Transaction> transactions = TransactionsManager.getInstance().getTransactions();
		
		try {
			writer = new FileWriter(transactions_file);
			
			for (Transaction transaction : transactions) {
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void exportParticipants(File participants_file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Transaction transaction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Participant participant) {
		// TODO Auto-generated method stub
		
	}

}

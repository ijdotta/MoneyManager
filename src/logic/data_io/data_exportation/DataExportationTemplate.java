package logic.data_io.data_exportation;

import java.io.File;

public abstract class DataExportationTemplate {
	
	public void export() {
		File participants_file, transactions_file;
		
		participants_file = createFile();
		exportParticipants(participants_file);
		
		transactions_file = createFile();
		exportTransactions(transactions_file);
	}

	protected abstract void exportTransactions(File transactions_file);

	protected abstract void exportParticipants(File participants_file);

	protected File createFile() {
		//TODO
		return null;
	}

}

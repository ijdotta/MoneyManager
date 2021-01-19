package logic.data_io.data_exportation;

import java.io.FileWriter;
import java.io.IOException;

import logic.transactions.Participant;
import logic.transactions.Transaction;

public class CSVDataExportationVisitor implements ExportationVisitor {


	@Override
	public void visit(Transaction transaction) {
		
		try {
			FileWriter writer = new FileWriter("t_csv_"+transaction.getId()+".txt");
			
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Participant participant) {
		// TODO Auto-generated method stub
		
	}

}

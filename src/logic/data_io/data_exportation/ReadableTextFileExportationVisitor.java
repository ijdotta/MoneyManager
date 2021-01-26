package logic.data_io.data_exportation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import logic.transactions.Balance;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;

public class ReadableTextFileExportationVisitor extends DataExportationTemplate implements ExportationVisitor {

	@Override
	public void visit(Transaction transaction) {
		FileWriter writer;

		try {
			/*
			 * Ver cómo hacer que se haga append, pero que se cree un archivo nuevo en el primer llamado
			 */
			writer = new FileWriter("Transacciones.txt", true);
			String[] fields = getFields(transaction);
			writer.write(getLine(fields) + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(Participant participant) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Balance balance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Resumen resumen) {
		// TODO Auto-generated method stub

	}

	protected String getLine(String[] fields) {
		String line = "";
		for (String s : fields) {
			line += s + " ";
		}
		return line.trim();
	}

	private void freeName(String filename) {
		File file_to_free = new File(filename);
		if (file_to_free.isFile()) {
			File rename_target = new File(filename + ".old");
			file_to_free.renameTo(rename_target);
			file_to_free.delete();
		}
	}

}

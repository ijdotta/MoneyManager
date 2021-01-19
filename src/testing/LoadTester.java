package testing;

import logic.TransactionsManager;
import logic.data_io.data_exportation.ConsoleExportationVisitor;
import logic.data_io.data_exportation.ExportationVisitor;

public class LoadTester {
	
	public static void main(String[] args) {
		TransactionsManager manager = TransactionsManager.getInstance();
		ExportationVisitor consoleExport = new ConsoleExportationVisitor();
		
		
		System.out.println("Trying to export...");
		manager.export(consoleExport);
		
		System.out.println("Trying to load from other instance of the app... ");
		manager.load();
		
		System.out.println("Result: ");
		manager.export(consoleExport);
	}

}

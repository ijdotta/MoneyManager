package testing;

import java.util.Collection;
import logic.TransactionsManager;
import logic.data_io.data_exportation.ConsoleExportationVisitor;
import logic.data_io.data_exportation.ExportationVisitor;
import logic.data_io.data_exportation.ReadableTextFileExportationVisitor;
import logic.data_io.data_exportation.SerializationDataExportationVisitor;
import logic.transactions.Balance;
import logic.transactions.Debt;
import logic.transactions.User;
import logic.transactions.Resumen;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidUserException;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class Tester {


	public static void main(String[] args) {
		TransactionsManager manager = TransactionsManager.getInstance();
		ExportationVisitor serialExport, consoleExport;
		
		serialExport = new SerializationDataExportationVisitor();
		consoleExport = new ConsoleExportationVisitor();

		// Definir participantes

		User p1, p2, p3;
		p1 = new User(42631354, "Ignacio");
		p2 = new User(33125166, "Leandro");
		p3 = new User(38252788, "Agustina");

		try {
			manager.addUser(p1);
			manager.addUser(p2);
			manager.addUser(p3);
		} catch (InvalidUserException e) {
			e.printStackTrace();
		}

		manager.export(consoleExport);

		// Definir transacciones

		Transaction t1, t2, t3, t4;

		t1 = new Transaction(1315.34f, "Comida", p1, p2);
		t1.addBeneficiarios(p1);

		t2 = new Transaction(2515.41f, "Bebida", p2, p1);
		t2.addBeneficiarios(p2);

		t3 = new Transaction(13113.99f, "Ropa", p2, p1);
		t3.addBeneficiarios(p2);
		
		t4 = new Transaction(25000.33f, "Viaje", p3, p1, p2, p3);

		try {
			manager.addTransaction(t1);
			manager.addTransaction(t2);
			manager.addTransaction(t3);
			manager.addTransaction(t4);
		} catch (InvalidTransactionException | ResumenNotFoundException e) {
			e.printStackTrace();
		}

		manager.export(consoleExport);

		// Obtener resumenes
		printResumen(p1);
		printResumen(p2);
		printResumen(p3);

		System.out.println("\n DEUDA:");

		// Obtener deuda
		Debt deuda = debt(p1, p2);
		System.out.println("Pagando deuda... ");
		deuda.pay();
		
		deuda = debt(p1, p3);
		System.out.println("Pagando deuda... ");
		deuda.pay();
		
		deuda = debt(p2, p3);
		System.out.println("Pagando deuda... ");
		deuda.pay();
		
		manager.export(consoleExport);

		// Obtener deuda
		deuda = debt(p1, p2);
		System.out.println(deuda.toString());
		deuda.pay();
		
		manager.export(consoleExport);
		
		// Serialize
		manager.export(serialExport);
		manager.export(new ReadableTextFileExportationVisitor());
		
	}

	private static Debt debt(User p1, User p2) {
		Balance balance = findBalance(p1, p2);
		Debt deuda = balance.getDebt();
		System.out.println("DEBT: " + deuda.toString());
		return deuda;
	}

	private static void printResumen(User p) {
		System.out.println();
		System.out.println("Resumen: " + p.toString());

		Resumen r = TransactionsManager.getInstance().getResumenes().get(p);
		for (Transaction t : r.getTransactions()) {
			System.out.println(t.toString());
		}
		System.out.println("__________________________________________");
		System.out.println("Total: $" + r.getImporte());

	}

	private static Balance findBalance(User p1, User p2) {
		for (Balance balance : TransactionsManager.getInstance().getBalances()) {
			Collection<User> participants = balance.getUsers();
			if (participants.contains(p1) && participants.contains(p2)) {
				return balance;
			}
		}
		return null;
	}

//	protected static Transaction getRandomTransaction() {
//		Random r = new Random();
//		Participant pagador = r.nextInt(2) == 0? p1 : p2;
//		Transaction t = new Transaction(r.nextFloat() % 10000, "", pagador, p1);
//	}

}

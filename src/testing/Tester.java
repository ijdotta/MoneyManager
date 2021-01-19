package testing;

import java.util.Collection;
import java.util.Random;

import logic.TransactionsManager;
import logic.data_io.data_exportation.ConsoleExportationVisitor;
import logic.transactions.Balance;
import logic.transactions.Debt;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidParticipantException;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class Tester {


	public static void main(String[] args) {
		TransactionsManager manager = TransactionsManager.getInstance();
		ConsoleExportationVisitor exportationVisitor = new ConsoleExportationVisitor();

		// Definir participantes

		Participant p1, p2, p3;
		p1 = new Participant(42631354, "Ignacio");
		p2 = new Participant(33125166, "Leandro");
		p3 = new Participant(38252788, "Agustina");

		try {
			manager.addParticipant(p1);
			manager.addParticipant(p2);
			manager.addParticipant(p3);
		} catch (InvalidParticipantException e) {
			e.printStackTrace();
		}

		manager.export(exportationVisitor);

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

		manager.export(exportationVisitor);

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
		
		manager.export(exportationVisitor);

		// Obtener deuda
		deuda = debt(p1, p2);
		System.out.println(deuda.toString());
		deuda.pay();
		
		manager.export(exportationVisitor);

	}

	private static Debt debt(Participant p1, Participant p2) {
		Balance balance = findBalance(p1, p2);
		Debt deuda = balance.getDebt();
		System.out.println("DEBT: " + deuda.toString());
		return deuda;
	}

	private static void printResumen(Participant p) {
		System.out.println();
		System.out.println("Resumen: " + p.toString());

		Resumen r = TransactionsManager.getInstance().getResumenes().get(p);
		for (Transaction t : r.getTransactions()) {
			System.out.println(t.toString());
		}
		System.out.println("__________________________________________");
		System.out.println("Total: $" + r.getImporte());

	}

	private static Balance findBalance(Participant p1, Participant p2) {
		for (Balance balance : TransactionsManager.getInstance().getBalances()) {
			Collection<Participant> participants = balance.getParticipants();
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

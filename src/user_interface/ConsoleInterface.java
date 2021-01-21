package user_interface;

import java.awt.EventQueue;
import java.util.List;
import java.util.Scanner;

import logic.TransactionsManager;
import logic.data_io.data_exportation.ConsoleExportationVisitor;
import logic.transactions.Balance;
import logic.transactions.Participant;
import logic.transactions.Transaction;
import logic.transactions.exceptions.InvalidParticipantException;
import logic.transactions.exceptions.InvalidTransactionException;
import logic.transactions.exceptions.ResumenNotFoundException;

public class ConsoleInterface {
	
	protected static Balance current_balance;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

//					ConsoleInterface app = new ConsoleInterface();

					displayOptions();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected static void displayOptions() {
		Scanner sc = new Scanner(System.in);

		int i = 0;

		System.out.println("Options: ");
		System.out.println(++i + ". List participants");
		System.out.println(++i + ". Add new participant");
		System.out.println(++i + ". Add new transaction");
		System.out.println(++i + ". Get balance");
		System.out.println(++i + ". Get resumen");
		System.out.println(++i + ". Display manager");
		System.out.print("Ingrese opción: ");

		int opt = sc.nextInt();

		switch (opt) {
		case 1:
			listParticipants();
			break;
		case 2:
			addParticipant();
			break;
		case 3:
			addTransaction();
			break;
		case 4:
			getBalance();
			break;
		case 5:
			getResumen();
			break;
		case 6: 
			displayManager();
		default:
			displayOptions();
		}
		displayOptions();
		sc.close();
	}

	private static void displayManager() {
		TransactionsManager.getInstance().export(new ConsoleExportationVisitor());
	}

	private static void getResumen() {
		
	}

	private static void getBalance() {
		Scanner sc = new Scanner(System.in);
		TransactionsManager manager = TransactionsManager.getInstance();
		List<Balance> balances = manager.getBalances();
		int id_A, id_B;
		
		System.out.print("Ingrese ID A: ");
		id_A = sc.nextInt();
		
		System.out.print("Ingrese ID B: ");
		id_B = sc.nextInt();
		
		Participant pA, pB;
		
		pA = manager.getParticipant(id_A);
		pB = manager.getParticipant(id_B);
		
		for (Balance b : balances) {
			if (b.getParticipants().contains(pA) && b.getParticipants().contains(pB)) {
				current_balance = b;
				break;
			}
		}
		
		sc.close();
	}

	private static void addTransaction() {
		Scanner sc = new Scanner(System.in);
		TransactionsManager manager = TransactionsManager.getInstance();
		Transaction t;
		
		float amount;
		String concepto;
		int payer_id;
		
		System.out.print("Ingrese importe: $");
		amount = sc.nextFloat();
		
		System.out.print("Ingrese concepto: ");
		concepto = sc.nextLine().trim();
		
		System.out.print("Ingrese ID pagador: ");
		payer_id = sc.nextInt();
		
		t = new Transaction(amount, concepto, manager.getParticipant(payer_id));
		
		int beneficiario_id, cant = 0;
		do {
			
			System.out.print("Ingrese un ID beneficiario: ");
			beneficiario_id = sc.nextInt();
			
			if (beneficiario_id == 0) {
				if (cant == 0) {
					System.err.println("Ingrese al menos 1 beneficiario. ");
				}
				else {
					break;
				}
			}
			else {
				Participant p = manager.getParticipant(beneficiario_id);
				if (p != null)
					t.addBeneficiarios(p);
				else
					System.err.println("ID inválido");
			}
			
		} while (beneficiario_id != 0);
		
		try {
			manager.addTransaction(t);
		} catch (InvalidTransactionException | ResumenNotFoundException e) {
			addTransaction();
		}
		
		sc.close();
	}

	private static void addParticipant() {
		Scanner sc = new Scanner(System.in);
		Participant p;
		int id;
		String name;
		
		System.out.print("ID: ");
		id = sc.nextInt();
		
		System.out.print("Name: ");
		name = sc.nextLine().trim();
		
		p = new Participant(id, name);
		
		try {
			TransactionsManager.getInstance().addParticipant(p);
		} catch (InvalidParticipantException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		sc.close();
	}

	private static void listParticipants() {
		System.out.println();
		System.out.println(">> Participants: ");
		for (Participant p : TransactionsManager.getInstance().getParticipants()) {
			System.out.println(p);
		}
	}

}

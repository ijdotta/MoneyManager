package gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import gui.commands.AddUserCommand;
import gui.commands.Command;
import gui.commands.components.CommandButton;
import gui.forms.TransactionInputForm;

public class GUI_Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3739307868059471262L;
	
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					GUI_Main frame = new GUI_Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Command addUserCommand;
		
		addUserCommand = new AddUserCommand();

		CommandButton btnClickMe = new CommandButton("Add user");
		btnClickMe.setCommand(addUserCommand);
		btnClickMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClickMe.executeCommand();
			}

		});
		contentPane.add(btnClickMe);
		
		CommandButton btnAddTransaction = new CommandButton("Add transaction");
		btnAddTransaction.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(null, new TransactionInputForm(), "Test", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		contentPane.add(btnAddTransaction);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);

	}

}

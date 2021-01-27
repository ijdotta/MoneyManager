package gui.forms;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.actionhandlers.CleanTextFieldMouseListener;

public class UserInputForm extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7129142963346368124L;
	private JTextField txtId;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtDateOfBirth;
	private JTextField txtEmail;

	/**
	 * Create the panel.
	 */
	public UserInputForm() {
		setLayout(null);
		
		txtId = new JTextField();
		txtId.setText("ID");
		txtId.setBounds(12, 12, 220, 19);
		add(txtId);
		txtId.setColumns(10);
		txtId.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((JTextField)e.getSource()).setText("");
			}
		});
		txtId.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				((JTextField)e.getSource()).setText("");
			}
		});
		
		txtFirstName = new JTextField();
		txtFirstName.setText("First name");
		txtFirstName.setBounds(12, 43, 220, 19);
		add(txtFirstName);
		txtFirstName.setColumns(10);
		
		txtFirstName.addMouseListener(new CleanTextFieldMouseListener(txtFirstName.getText()));
		
		txtLastName = new JTextField();
		txtLastName.setText("Last name");
		txtLastName.setBounds(12, 74, 220, 19);
		add(txtLastName);
		txtLastName.setColumns(10);
		
		txtLastName.addMouseListener(new CleanTextFieldMouseListener(txtFirstName.getText()));
		
		txtDateOfBirth = new JTextField();
		txtDateOfBirth.setText("Date of birth");
		txtDateOfBirth.setBounds(12, 105, 220, 19);
		add(txtDateOfBirth);
		txtDateOfBirth.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setText("email");
		txtEmail.setBounds(12, 136, 220, 19);
		add(txtEmail);
		txtEmail.setColumns(10);
		
		setPreferredSize(new Dimension(200, 200));

	}
	
	public JTextField getTxtId() {
		return txtId;
	}

	public JTextField getTxtFirstName() {
		return txtFirstName;
	}

	public JTextField getTxtLastName() {
		return txtLastName;
	}

	public JTextField getTxtDateOfBirth() {
		return txtDateOfBirth;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	@Override
	public String toString() {
		return "User input form";
	}
}

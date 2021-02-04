package gui.forms;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.ListModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import logic.TransactionsManager;
import logic.transactions.User;

import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * Basico. Deberían añadirse selectores de fecha, formateadores, etc...
 * @author ignacio
 *
 */
public class TransactionInputForm extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8125732301030438869L;
	private JTextField txtImporte;
	private JTextField txtConcepto;
	private JTextField txtFecha;
	private JLabel lblPagador;
	private JComboBox comboBox;
	private JPanel beneficiarios_panel;
	private JPanel beneficiarios_border_panel;
	private JList<? extends Object> list_1;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public TransactionInputForm() {
		setLayout(new MigLayout("", "[grow]", "[][][][][grow]"));
		
		txtImporte = new JTextField();
		txtImporte.setText("Importe");
		add(txtImporte, "cell 0 0,growx");
		txtImporte.setColumns(10);
		
		txtConcepto = new JTextField();
		txtConcepto.setText("Concepto");
		add(txtConcepto, "cell 0 1,growx");
		txtConcepto.setColumns(10);
		
		txtFecha = new JTextField();
		txtFecha.setText("Fecha: dd/mm/aaaa");
		add(txtFecha, "cell 0 2,growx");
		txtFecha.setColumns(10);
		
		lblPagador = new JLabel("Pagador: ");
		add(lblPagador, "flowx,cell 0 3");
		
		comboBox = new JComboBox();
		add(comboBox, "cell 0 3,growx");
		
		beneficiarios_border_panel = new JPanel();
		beneficiarios_border_panel.setBorder(new TitledBorder(null, "Beneficiarios: ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(beneficiarios_border_panel, "cell 0 4,grow");
		beneficiarios_border_panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		beneficiarios_panel = new JPanel();
		beneficiarios_border_panel.add(beneficiarios_panel, "cell 0 0,grow");
		beneficiarios_panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		
		
		List<User> beneficiarios = new LinkedList<>();
		List<JCheckBox> items = new ArrayList<>();
		
//		ListModel model = new DefaultListModel<>();
		
		for (User b : TransactionsManager.getInstance().getUsers()) {
			System.out.println("USER " + b.toString());
			JCheckBox box = new JCheckBox();
			box.setText(b.toString());
			box.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (box.isSelected()) {
						beneficiarios.add(b);
						System.out.println(b.toString() + " ADDED");
					}
					else {
						beneficiarios.remove(b);
						System.out.println(b.toString() + " REMOVED");
					}
					printList(beneficiarios);
				}
			});
			
			items.add(box);
			
		}
		
		list_1 = new JList<>(items.toArray());
		beneficiarios_panel.add(list_1, "cell 0 0,grow");
		
	}
	
	private void printList(List<? extends Object> list) {
		for (Object o : list) {
			System.out.println(o.toString());
		}
	}

}

package gui.forms;

import java.awt.Dimension;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;

public class UserCheckboxPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public UserCheckboxPanel() {
		
		setPreferredSize(new Dimension(200, 50));
		setLayout(new MigLayout("", "[]", "[]"));
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		add(chckbxNewCheckBox, "cell 0 0");

	}

}

package gui.actionhandlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.text.JTextComponent;

public class CleanTextFieldMouseListener extends MouseAdapter {
	
	protected String original_text;
	
	public CleanTextFieldMouseListener(String original_text) {
		super();
		this.original_text = original_text.trim();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		JTextComponent comp = (JTextComponent) e.getSource();
		if (comp.getText().trim().equals(this.original_text)) {
			comp.setText("");
		}
	}
	

}

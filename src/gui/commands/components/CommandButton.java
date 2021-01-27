package gui.commands.components;

import javax.swing.JButton;

import gui.commands.Command;

public class CommandButton extends JButton implements CommandComponent {
	
	protected Command command;
	
	public CommandButton(String title) {
		super(title);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6978749844674150803L;

	@Override
	public void setCommand(Command command) {
		this.command = command;
	}

	@Override
	public void executeCommand() {
		command.execute();
	}

}

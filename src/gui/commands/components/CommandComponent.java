package gui.commands.components;

import gui.commands.Command;

public interface CommandComponent {
	
	public void setCommand(Command command);
	
	public void executeCommand();

}

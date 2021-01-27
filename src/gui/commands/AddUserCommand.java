package gui.commands;

import javax.swing.JOptionPane;

import gui.forms.UserInputForm;
import logic.TransactionsManager;
import logic.transactions.User;
import logic.transactions.exceptions.InvalidUserException;

public class AddUserCommand implements Command {

	public AddUserCommand() {
	}

	@Override
	public void execute() {
		UserInputForm form = new UserInputForm();
		User user = null;
		String out_message = "";

		// Launch input form
		int ans = JOptionPane.showConfirmDialog(null, form, form.toString(), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (ans == JOptionPane.OK_OPTION) {

			try {

				user = getUser(form);
				TransactionsManager.getInstance().addUser(user);
				out_message = "Usuario añadido exitosamente";
			} catch (NumberFormatException e) {
				out_message = "ID inválido";
			} catch (InvalidUserException e) {
				out_message = e.getMessage();
			} finally {
				JOptionPane.showMessageDialog(null, out_message);
			}

		}

	}

	private User getUser(UserInputForm form) {
		int id = Integer.parseInt(form.getTxtId().getText().trim());
		String first = form.getTxtFirstName().getText().trim();
		String lastname = form.getTxtLastName().getText().trim();

		return new User(id, first + " " + lastname);
	}

}

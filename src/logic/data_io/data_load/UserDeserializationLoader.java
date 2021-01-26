package logic.data_io.data_load;

import logic.TransactionsManager;
import logic.transactions.User;
import logic.transactions.exceptions.InvalidUserException;
import logic.transactions.exceptions.UserNotFoundException;

public class UserDeserializationLoader extends DataDeserialization {
	
	public void load(String path_to_dir) throws UserNotFoundException {
		super.load(path_to_dir, "u");
	}

	@Override
	protected void loadItem(Object item) {
		try {
			TransactionsManager.getInstance().addUser((User) item);
			
			// DEBUGGING
			System.out.println("Loaded user " + item.toString());
		} catch (InvalidUserException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

package logic.data_io.data_load;

import logic.TransactionsManager;
import logic.transactions.Participant;
import logic.transactions.exceptions.InvalidParticipantException;
import logic.transactions.exceptions.ParticipantNotFoundException;

public class ParticipantDeserializationLoader extends DataDeserialization {
	
	public void load(String path_to_dir) throws ParticipantNotFoundException {
		super.load(path_to_dir, "p");
	}

	@Override
	protected void loadItem(Object item) {
		try {
			TransactionsManager.getInstance().addParticipant((Participant) item);
			
			// DEBUGGING
			System.out.println("Loaded participant " + item.toString());
		} catch (InvalidParticipantException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

package logic.data_io.data_load;

import logic.TransactionsManager;
import logic.transactions.Participant;
import logic.transactions.exceptions.InvalidParticipantException;

public class ParticipantDeserializationLoader extends DataDeserialization {
	
	public void load(String path_to_dir) {
		super.load(path_to_dir, "p");
	}

	@Override
	protected void loadItem(Object item) {
		try {
			TransactionsManager.getInstance().addParticipant((Participant) item);
		} catch (InvalidParticipantException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}

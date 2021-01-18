package logic.data_load;

import logic.TransactionsManager;
import logic.transactions.Participant;
import logic.transactions.exceptions.InvalidParticipantException;

public class ParticipantDeserializationLoader extends DataDeserialization {

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

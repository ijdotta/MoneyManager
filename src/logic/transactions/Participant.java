package logic.transactions;

public class Participant {

	protected int id;
	protected String name;

	public Participant(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}

package logic.transactions;

import java.io.Serializable;

import logic.data_exportation.Exportable;
import logic.data_exportation.ExportationVisitor;

public class Participant implements Exportable, Serializable{

	private static final long serialVersionUID = 1169051661928131692L;
	
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
	
	public boolean equals(Participant participant) {
		return this.id == participant.getId();
	}

	@Override
	public void export(ExportationVisitor exportationVisitor) {
		exportationVisitor.visit(this);
	}

}

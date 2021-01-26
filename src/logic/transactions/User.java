package logic.transactions;

import java.io.Serializable;

import logic.data_io.data_exportation.Exportable;
import logic.data_io.data_exportation.ExportationVisitor;

public class User implements Exportable, Serializable{

	private static final long serialVersionUID = 1169051661928131692L;
	
	protected int id;
	protected String name;

	public User(int id, String name) {
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
	
	public boolean equals(User participant) {
		return this.id == participant.getId();
	}

	@Override
	public void export(ExportationVisitor exportationVisitor) {
		exportationVisitor.visit(this);
	}
	
	@Override
	public String toString() {
		return "P" + this.id + " : " + this.name;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}

}

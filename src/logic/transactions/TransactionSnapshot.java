package logic.transactions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSnapshot implements Serializable {

	private static final long serialVersionUID = -2134643326230971629L;
	
	protected int id;
	protected float importe;
	protected String concepto;
	protected LocalDate fecha;
	protected String notas;
	protected int pagador_id;
	protected List<Integer> beneficiarios_id;
	
	public TransactionSnapshot() {
		beneficiarios_id = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	public int getPagador_id() {
		return pagador_id;
	}

	public void setPagador_id(int pagador_id) {
		this.pagador_id = pagador_id;
	}

	public List<Integer> getBeneficiarios_id() {
		return beneficiarios_id;
	}

	public void setBeneficiarios_id(List<Integer> beneficiarios_id) {
		this.beneficiarios_id = beneficiarios_id;
	}

	public void addBeneficiarioId(int beneficiario_id) {
		this.beneficiarios_id.add(beneficiario_id);
	}

}

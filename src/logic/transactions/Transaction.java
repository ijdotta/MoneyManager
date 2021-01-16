package logic.transactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.transactions.exceptions.TransactionNotFoundException;

public class Transaction {

	private static Logger logger;
	private static int GLOBAL_ID = 0;

	protected int id;
	protected float importe;
	protected String concepto;
	protected LocalDate fecha;
	protected String notas;
	protected Participant pagador;
	protected List<Participant> beneficiarios;
	protected List<TransactionModificationSensitive> collections;

	/**
	 * POR DEFECTO EL PAGADOR NO ES UN BENEFICIARIO. DEBE AÑADIRSE MANUALMENTE.
	 * @param importe
	 * @param concepto
	 * @param fecha
	 * @param notas
	 * @param pagador
	 * @param beneficiarios
	 */
	public Transaction(float importe, String concepto, LocalDate fecha, String notas, Participant pagador,
			Participant... beneficiarios) {
		setUpLogger();
		
		this.id = GLOBAL_ID++;
		this.importe = importe;
		this.concepto = concepto;
		this.fecha = fecha;
		this.notas = notas;
		this.pagador = pagador;
		this.beneficiarios = new ArrayList<>(beneficiarios.length + 1);
		for (Participant beneficiario : beneficiarios)
			this.beneficiarios.add(beneficiario);
		this.collections = new ArrayList<>(3);
	}

	public Transaction(float importe, String concepto, Participant pagador, Participant beneficiario) {
		this(importe, concepto, LocalDate.now(), null, pagador, beneficiario);
	}

	public Transaction() {
		this(0, null, null, null);
	}

	public int getId() {
		return id;
	}

	public float getAmount() {
		return importe;
	}

	public String getConcepto() {
		return concepto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public String getNotas() {
		return notas;
	}

	public Participant getPagador() {
		return pagador;
	}

	public List<Participant> getBeneficiarios() {
		return beneficiarios;
	}

	public List<TransactionModificationSensitive> getCollections() {
		return collections;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	public void setPagador(Participant pagador) {
		this.pagador = pagador;
	}

	public void addBeneficiarios(Participant... beneficiarios) {
		for (Participant beneficiario : beneficiarios) {
			this.beneficiarios.add(beneficiario);
		}
	}

	public void addCollections(TransactionModificationSensitive... collections) {
		for (TransactionModificationSensitive collection : collections) {
			this.collections.add(collection);
		}
	}
	
	public void destroy() {
		for (TransactionModificationSensitive collection : collections) {
			try {
				collection.remove(this);
			} catch (TransactionNotFoundException e) {
				// No importa si no la encuentra, el objetivo es deshacerse de la transacción en todas las colecciones sensibles a su eliminación.
				e.printStackTrace();
				logger.warning("Transaction #" + this.getId() + ": " + this.getConcepto() + " not found in " + collection.toString());
			}
		}
	}
	
	/**
	 * Configura el logger de la clase.
	 */
	private void setUpLogger() {
		if (logger == null) {

			logger = Logger.getLogger(this.getClass().getName());

			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.ALL);
			logger.addHandler(hnd);

			logger.setLevel(Level.ALL);

			Logger rootLoger = logger.getParent();
			for (Handler h : rootLoger.getHandlers())
				h.setLevel(Level.OFF);
		}
	}
	
	@Override
	public int hashCode() {
		return id;
	}

}

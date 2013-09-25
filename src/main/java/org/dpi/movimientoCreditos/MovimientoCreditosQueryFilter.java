package org.dpi.movimientoCreditos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dpi.empleo.EmploymentQueryFilter;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class MovimientoCreditosQueryFilter implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EmploymentQueryFilter empleoQueryFilter;
	
	Long id;
	
	Long idCreditsPeriod;

	List<TipoMovimientoCreditos> tiposMovimientoCreditos = new ArrayList<TipoMovimientoCreditos>();
	
	List<GrantedStatus> grantedStatuses = new ArrayList<GrantedStatus>();
	
	Boolean hasCredits = null;
	

	public MovimientoCreditosQueryFilter(){
		
	}

	public EmploymentQueryFilter getEmploymentQueryFilter() {
		return empleoQueryFilter;
	}

	public void setEmploymentQueryFilter(EmploymentQueryFilter empleoQueryFilter) {
		this.empleoQueryFilter = empleoQueryFilter;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<TipoMovimientoCreditos> getTiposMovimientoCreditos() {
		return tiposMovimientoCreditos;
	}

	public void setTiposMovimientoCreditos(
			List<TipoMovimientoCreditos> tiposMovimientoCreditos) {
		this.tiposMovimientoCreditos = tiposMovimientoCreditos;
	}
	
	public void addTipoMovimientoCreditos(TipoMovimientoCreditos tipoMovimientoCreditos) {
		this.tiposMovimientoCreditos.add(tipoMovimientoCreditos);
	}
	
	public List<GrantedStatus> getGrantedStatuses(){
		return this.grantedStatuses;
	}

	public void setGrantedStatuses(
			List<GrantedStatus> grantedStatuses) {
		this.grantedStatuses = grantedStatuses;
	}
	
	public void addGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatuses.add(grantedStatus);
	}
	
	public Long getIdCreditsPeriod() {
		return idCreditsPeriod;
	}

	public void setIdCreditsPeriod(Long idCreditsPeriod) {
		this.idCreditsPeriod = idCreditsPeriod;
	}
	
	public Boolean isHasCredits() {
		return hasCredits;
	}

	public void setHasCredits(Boolean hasCredits) {
		this.hasCredits = hasCredits;
	}
	
}

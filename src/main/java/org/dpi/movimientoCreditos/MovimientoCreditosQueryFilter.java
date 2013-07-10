package org.dpi.movimientoCreditos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dpi.empleo.EmpleoQueryFilter;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class MovimientoCreditosQueryFilter implements Serializable {
	
	
	EmpleoQueryFilter empleoQueryFilter;
	
	Long id;
	

	List<TipoMovimientoCreditos> tiposMovimientoCreditos = new ArrayList<TipoMovimientoCreditos>();
	
	
	public MovimientoCreditosQueryFilter(){
		
	}

	public EmpleoQueryFilter getEmpleoQueryFilter() {
		return empleoQueryFilter;
	}

	public void setEmpleoQueryFilter(EmpleoQueryFilter empleoQueryFilter) {
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
}

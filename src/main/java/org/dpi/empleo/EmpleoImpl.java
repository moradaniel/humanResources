package org.dpi.empleo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.agente.Agente;
import org.dpi.category.Category;
import org.dpi.centroSector.CentroSector;
import org.dpi.domain.PersistentAbstract;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.janux.util.JanuxToStringStyle;

public class EmpleoImpl  extends PersistentAbstract implements Empleo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Agente agente;
	CentroSector centroSector;
	
	Category category;
	
	private Set<MovimientoCreditos> movimientosCreditos = new HashSet<MovimientoCreditos>();

	EmploymentStatus estado;
	
	Date fechaInicio;
	Date fechaFin;
	
	Empleo empleoAnterior;
	
	OccupationalGroup occupationalGroup;
	

	@Override
	public Agente getAgente() {
		return this.agente;
	}

	@Override
	public void setAgente(Agente agente) {
		this.agente=agente;
		
	}
	
	@Override
	public CentroSector getCentroSector() {
		return this.centroSector;
	}

	@Override
	public void setCentroSector(CentroSector centroSector) {
		this.centroSector=centroSector;
		
	}
	
	@Override
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@Override
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Override
	public Date getFechaFin() {
		return fechaFin;
	}

	@Override
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	@Override
	public Category getCategory() {
		return category;
	}
	
	@Override
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public Set<MovimientoCreditos> getMovimientosCreditos() {
		return this.movimientosCreditos;
	}
	
	@Override
	public void setMovimientosCreditos(Set<MovimientoCreditos> movimientosCreditos) {
		this.movimientosCreditos = movimientosCreditos;
		
	}

	@Override
	public void addMovimientoCreditos(MovimientoCreditos movimientoCreditos) {
		this.movimientosCreditos.add(movimientoCreditos);
	}

	@Override
	public EmploymentStatus getEstado() {
		return estado;
	}

	@Override
	public void setEstado(EmploymentStatus estado) {
		this.estado= estado;
		
	}

	@Override
	public boolean isClosed() {
		if (this.fechaFin == null)
			return false;
		return true;
	}

	@Override
	public Empleo getEmpleoAnterior() {
		return empleoAnterior;
	}

	@Override
	public void setEmpleoAnterior(Empleo empleoAnterior) {
		this.empleoAnterior = empleoAnterior;
	}
	
	@Override
	public OccupationalGroup getOccupationalGroup() {
		return occupationalGroup;
	}

	@Override
	public void setOccupationalGroup(OccupationalGroup occupationalGroup) {
		this.occupationalGroup = occupationalGroup;
	}
	
	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("category", getCategory().getCode());
		sb.append("estado", getEstado());

		
		return sb.toString();
	}

}

package org.dpi.empleo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.dpi.agente.Agente;
import org.dpi.categoria.Categoria;
import org.dpi.centroSector.CentroSector;
import org.dpi.domain.PersistentAbstract;
import org.dpi.movimientoCreditos.MovimientoCreditos;

public class EmpleoImpl  extends PersistentAbstract implements Empleo{
	
	Agente agente;
	CentroSector centroSector;
	
	Categoria categoria;
	
	private Set<MovimientoCreditos> movimientosCreditos = new HashSet<MovimientoCreditos>();

	EstadoEmpleo estado;
	
	Date fechaInicio;
	Date fechaFin;
	
	Empleo empleoAnterior;
	
	
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
	
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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
	public EstadoEmpleo getEstado() {
		return estado;
	}

	@Override
	public void setEstado(EstadoEmpleo estado) {
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

}

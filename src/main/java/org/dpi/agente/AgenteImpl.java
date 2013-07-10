package org.dpi.agente;

import java.util.HashSet;
import java.util.Set;

import org.dpi.domain.PersistentAbstract;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EstadoEmpleo;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;

public class AgenteImpl  extends PersistentAbstract implements Agente{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String cuil;
	String apellidoNombre;
	CondicionAgente condicion;
	private Set<Empleo> empleos = new HashSet<Empleo>();

	public String getCuil() {
		return cuil;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public String getApellidoNombre() {
		return apellidoNombre;
	}

	public void setApellidoNombre(String apellidoNombre) {
		this.apellidoNombre = apellidoNombre;
	}

	@Override
	public CondicionAgente getCondicion() {
		return condicion;
	}

	@Override
	public void setCondicion(CondicionAgente condicionAgente) {
		condicion = condicionAgente;
		
	}

	public Set<Empleo> getEmpleos() {
		return empleos;
	}

	public void setEmpleos(Set<Empleo> empleos) {
		this.empleos = empleos;
	}
	
	@Override
	public boolean hasMovimientosAscensoPendientes() {
		for(Empleo empleo:this.empleos){
			for(MovimientoCreditos movimiento : empleo.getMovimientosCreditos()){
				if(movimiento.getTipoMovimientoCreditos()==TipoMovimientoCreditos.AscensoAgente){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Empleo getEmpleoActivo(){
		for(Empleo empleo:this.empleos){
			if(empleo.getEstado()==EstadoEmpleo.ACTIVO){
				return empleo;
			}
		}
		return null;
	}
	
}

package org.dpi.reparticion;

import java.util.HashSet;
import java.util.Set;

import org.dpi.centroSector.CentroSector;
import org.dpi.domain.PersistentAbstract;

public class ReparticionImpl extends PersistentAbstract implements Reparticion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String nombre;
	
	private Set<CentroSector> centroSectores = new HashSet<CentroSector>();

	public ReparticionImpl(){
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String codigo){
		this.code = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public Set<CentroSector> getCentroSectores() {
		return centroSectores;
	}

	@Override
	public void setCentroSectores(Set<CentroSector> centroSectores) {
		this.centroSectores = centroSectores;
		
	}

	@Override
	public void addCentroSector(CentroSector centroSector) {
		this.centroSectores.add(centroSector);
	}
}

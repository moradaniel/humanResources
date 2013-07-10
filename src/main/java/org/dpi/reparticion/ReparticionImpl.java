package org.dpi.reparticion;

import java.util.HashSet;
import java.util.Set;

import org.dpi.centroSector.CentroSector;
import org.dpi.domain.PersistentAbstract;

public class ReparticionImpl extends PersistentAbstract implements Reparticion {

	private String nombre;
	
	private Set<CentroSector> centroSectores = new HashSet<CentroSector>();

	public ReparticionImpl(){
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

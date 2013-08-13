package org.dpi.reparticion;

import java.io.Serializable;
import java.util.Set;

import org.dpi.centroSector.CentroSector;
import org.dpi.domain.Persistent;

public interface Reparticion extends Persistent, Serializable{
	
	public String getCode();
	public void setCode(String codigo);
	public String getNombre();
	public void setNombre(String nombre);
	public Set<CentroSector> getCentroSectores();
	public void setCentroSectores(Set<CentroSector> centroSectores);
	public void addCentroSector(CentroSector centroSector);

}

package org.dpi.centroSector;

import java.io.Serializable;

import org.dpi.domain.Persistent;
import org.dpi.reparticion.Reparticion;

public interface CentroSector extends Persistent,Serializable{

	String getCodigoCentro();

	void setCodigoCentro(String codigoCentro);

	String getCodigoSector();

	void setCodigoSector(String codigoSector);

	String getNombreCentro();

	void setNombreCentro(String nombreCentro);

	String getNombreSector();

	void setNombreSector(String nombreSector);

	Reparticion getReparticion();
	
	void setReparticion(Reparticion reparticion);
}
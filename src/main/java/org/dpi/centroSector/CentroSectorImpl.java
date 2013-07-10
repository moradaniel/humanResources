package org.dpi.centroSector;

import org.dpi.domain.PersistentAbstract;
import org.dpi.reparticion.Reparticion;

public class CentroSectorImpl  extends PersistentAbstract implements CentroSector{
	String codigoCentro;
	String codigoSector;
	String nombreCentro;
	String nombreSector;
	
	Reparticion reparticion;

	public String getCodigoCentro() {
		return codigoCentro;
	}

	public void setCodigoCentro(String codigoCentro) {
		this.codigoCentro = codigoCentro;
	}

	public String getCodigoSector() {
		return codigoSector;
	}

	public void setCodigoSector(String codigoSector) {
		this.codigoSector = codigoSector;
	}

	public String getNombreCentro() {
		return nombreCentro;
	}

	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}

	public String getNombreSector() {
		return nombreSector;
	}

	public void setNombreSector(String nombreSector) {
		this.nombreSector = nombreSector;
	}

	@Override
	public Reparticion getReparticion() {
		return this.reparticion;
	}

	@Override
	public void setReparticion(Reparticion reparticion) {
		this.reparticion = reparticion;
		
	}


}

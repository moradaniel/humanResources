package org.dpi.subDepartment;

import java.io.Serializable;

import org.dpi.department.Department;
import org.dpi.domain.Persistent;


public interface SubDepartment extends Persistent,Serializable{

	String getCodigoCentro();

	void setCodigoCentro(String codigoCentro);

	String getCodigoSector();

	void setCodigoSector(String codigoSector);

	String getNombreCentro();

	void setNombreCentro(String nombreCentro);

	String getNombreSector();

	void setNombreSector(String nombreSector);

	Department getDepartment();
	
	void setDepartment(Department department);
}
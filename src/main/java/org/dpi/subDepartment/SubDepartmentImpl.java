package org.dpi.subDepartment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.department.Department;
import org.dpi.domain.PersistentAbstract;
import org.janux.util.JanuxToStringStyle;

public class SubDepartmentImpl  extends PersistentAbstract implements SubDepartment{

	private static final long serialVersionUID = 1L;
	
	String codigoCentro;
	String codigoSector;
	String nombreCentro;
	String nombreSector;
	
	Department department;

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
	public Department getDepartment() {
		return this.department;
	}

	@Override
	public void setDepartment(Department department) {
		this.department = department;
		
	}
	
	public String toString(){
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("codigoCentro", getCodigoCentro());
		sb.append("codigoSector", getCodigoSector());
		sb.append("nombreCentro", getNombreCentro());
		sb.append("nombreSector", getNombreSector());
		
		return sb.toString();
	}


}

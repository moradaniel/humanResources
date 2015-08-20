package org.dpi.subDepartment;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.department.Department;
import org.dpi.domain.PersistentAbstract;

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

	/*
    @Override
    public String toString() {
        return "SubDepartment [codigoCentro=" + codigoCentro
                + ", codigoSector=" + codigoSector + ", nombreCentro="
                + nombreCentro + ", nombreSector=" + nombreSector
                + ", department=" + department.toString() + "]";
    }*/
    @Override
    public String toString() {
        
        StandardToStringStyle style = new StandardToStringStyle();
        style.setFieldSeparator(", ");
        style.setUseShortClassName(true);
        style.setUseIdentityHashCode(false);
        
        return new ToStringBuilder(this,style).
                append("codigoCentro", codigoCentro).
                append("codigoSector", codigoSector).
                append("nombreCentro", nombreCentro).
                append("nombreSector", nombreSector).
                append("department", department).
                toString();
    }
 

}

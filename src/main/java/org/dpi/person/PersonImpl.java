package org.dpi.person;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.domain.PersistentAbstract;
import org.dpi.employment.Employment;
import org.janux.util.JanuxToStringStyle;

public class PersonImpl  extends PersistentAbstract implements Person{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String cuil;
	String apellidoNombre;

	private Set<Employment> employments = new HashSet<Employment>();

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


	public Set<Employment> getEmployments() {
		return employments;
	}

	public void setEmployments(Set<Employment> empleos) {
		this.employments = empleos;
	}
	
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("cuil", getCuil());
		sb.append("apellidoNombre", getApellidoNombre());

		
		return sb.toString();
	}
}

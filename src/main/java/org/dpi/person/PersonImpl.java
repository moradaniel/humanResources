package org.dpi.person;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.domain.PersistentAbstract;
import org.dpi.empleo.Empleo;
import org.janux.util.JanuxToStringStyle;

public class PersonImpl  extends PersistentAbstract implements Person{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String cuil;
	String apellidoNombre;
	PersonCondition condition;
	private Set<Empleo> employments = new HashSet<Empleo>();

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
	public PersonCondition getCondition() {
		return condition;
	}

	@Override
	public void setCondition(PersonCondition personCondition) {
		condition = personCondition;
		
	}

	public Set<Empleo> getEmpleos() {
		return employments;
	}

	public void setEmpleos(Set<Empleo> empleos) {
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

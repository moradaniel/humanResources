package org.dpi.person;

import java.io.Serializable;
import java.util.Set;

import org.dpi.domain.Persistent;
import org.dpi.employment.Employment;


public interface Person extends Persistent,Serializable{

	String getApellidoNombre();

	void setApellidoNombre(String apellidoNombre);

	String getCuil();

	void setCuil(String cuil);
	
	public Set<Employment> getEmployments();

	public void setEmployments(Set<Employment> employments);

	PersonCondition getCondition();
	
	void setCondition(PersonCondition personCondition);

	
		
}
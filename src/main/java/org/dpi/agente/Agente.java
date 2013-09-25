package org.dpi.agente;

import java.io.Serializable;
import java.util.Set;

import org.dpi.domain.Persistent;
import org.dpi.empleo.Empleo;


public interface Agente extends Persistent,Serializable{

	String getApellidoNombre();

	void setApellidoNombre(String apellidoNombre);

	String getCuil();

	void setCuil(String cuil);
	
	public Set<Empleo> getEmpleos();

	public void setEmpleos(Set<Empleo> empleos);

	CondicionAgente getCondicion();
	
	void setCondicion(CondicionAgente condicionAgente);

	public Empleo getEmpleoActivo();
		
}
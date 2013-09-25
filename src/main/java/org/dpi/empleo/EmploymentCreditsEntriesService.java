package org.dpi.empleo;

import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Hotel objects from
 * persistent storage
 *
 */
public interface EmploymentCreditsEntriesService extends ApplicationContextAware
{
	public void ascenderAgente(Empleo empleo, String codigoCategoriaNueva);
	
	public void darDeBaja(Empleo empleo);
	
	public void ingresarPropuestaAgente(String codigoCategoriaPropuesta,Long centroSectorId);
}

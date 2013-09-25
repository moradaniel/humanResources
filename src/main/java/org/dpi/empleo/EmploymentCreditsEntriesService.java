package org.dpi.empleo;

import java.util.List;

import org.janux.bus.security.Account;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Employment objects from
 * persistent storage
 *
 */
public interface EmploymentCreditsEntriesService extends ApplicationContextAware
{
	public void ascenderAgente(Empleo empleo, String codigoCategoriaNueva);
	
	public void darDeBaja(Empleo empleo);
	
	public void ingresarPropuestaAgente(String codigoCategoriaPropuesta,Long centroSectorId);

	public List<EmploymentVO> buildEmploymentsVO(List<Empleo> empleosActivos, Long reparticionId, Account currenUser);
}

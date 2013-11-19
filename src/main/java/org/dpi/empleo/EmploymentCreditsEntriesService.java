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
	public void promotePerson(Empleo empleo, String newCategoryCode);
	
	public void darDeBaja(Empleo empleo);
	
	public void proposeNewEmployment(String proposedCategoryCode,Long centroSectorId);

	public List<EmploymentVO> buildEmploymentsVO(List<Empleo> empleosActivos, Long reparticionId, Account currenUser);
}

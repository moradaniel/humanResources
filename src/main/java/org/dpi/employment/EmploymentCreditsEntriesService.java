package org.dpi.employment;

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
	public void promotePerson(Employment employment, String newCategoryCode);
	
	public void deactivate(Employment employment, Account currentUser);
	
	public void undoDeactivation(Employment employment, Account currentUser);
	
	public void proposeNewEmployment(String proposedCategoryCode,Long subDepartmentId);

	public List<EmploymentVO> buildEmploymentsVO(List<Employment> activeEmployments, Long departmentId, Account currenUser);
}

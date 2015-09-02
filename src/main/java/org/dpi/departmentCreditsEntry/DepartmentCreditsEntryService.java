package org.dpi.departmentCreditsEntry;

import java.util.List;

import org.dpi.departmentCreditsEntry.DepartmentCreditsEntry.GrantedStatus;
import org.dpi.util.PageList;
import org.janux.bus.security.Account;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete DepartmentCreditsEntry objects from
 * persistent storage
 *
 */
public interface DepartmentCreditsEntryService extends ApplicationContextAware
{

	public List<DepartmentCreditsEntry> find(DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter);
	
	public PageList<DepartmentCreditsEntry> findCreditsEntries(final DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter);
	   
	public void delete(DepartmentCreditsEntry creditsEntry);
	
	public List<DepartmentCreditsEntryVO> buildCreditsEntryVO(List<DepartmentCreditsEntry> creditsEntryReparticion, Account account);
	public void saveOrUpdate(final DepartmentCreditsEntry creditsEntry);
	public void actualizarCreditosPorAscenso();
	
	public void changeGrantedStatus(DepartmentCreditsEntry movimiento, GrantedStatus newEstado);
	
	//public Set<Long> havePendingEntries(List<Long> personsIds, Long idReparticion,Long idCreditsPeriod);
		
}

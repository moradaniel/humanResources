package org.dpi.departmentCreditsEntry;

import java.util.List;

import org.dpi.util.PageList;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;
import org.springframework.util.CollectionUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class DepartmentCreditsEntryDaoHibImpl extends BaseDAOHibernate implements DepartmentCreditsEntryDao
{
	@SuppressWarnings("unchecked")
	public List<DepartmentCreditsEntry> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all departmentCreditsEntries");

		List<DepartmentCreditsEntry> list = (List<DepartmentCreditsEntry>)getHibernateTemplate().find("from DepartmentCreditsEntryImpl");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " entry in " + timer.printElapsedTime());

		return list;
	}	
	
	
	@SuppressWarnings( "unchecked" )
	public List<DepartmentCreditsEntry> find(final DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter){
		throw new UnsupportedOperationException();
	}
	
    public static void buildWhereClause(DepartmentCreditsEntryQueryFilter departmentCreditsEntryQueryFilter,
            List<String> wheres, List<String> paramNames, List<Object> values) {

        if (departmentCreditsEntryQueryFilter != null) {

            if(!CollectionUtils.isEmpty(departmentCreditsEntryQueryFilter.getCreditsPeriodNames())){
                wheres.add("  creditsPeriod.name IN ( :creditsPeriodsNames )  ");
                paramNames.add("creditsPeriodsNames");
                values.add(departmentCreditsEntryQueryFilter.getCreditsPeriodNames());

            }

            if(!CollectionUtils.isEmpty(departmentCreditsEntryQueryFilter.getDepartmentCreditsEntryTypes())){
                wheres.add("  entry.departmentCreditsEntryType IN (:departmentCreditsEntryType) ");
                paramNames.add("departmentCreditsEntryType");
                values.add(departmentCreditsEntryQueryFilter.getDepartmentCreditsEntryTypes());
            }
            
            if(!CollectionUtils.isEmpty(departmentCreditsEntryQueryFilter.getCreditsEntryTransactionTypes())){
                wheres.add("  entry.creditsEntryTransactionType IN (:creditsEntryTransactionType) ");
                paramNames.add("creditsEntryTransactionType");
                values.add(departmentCreditsEntryQueryFilter.getCreditsEntryTransactionTypes());
            }

            if(!CollectionUtils.isEmpty(departmentCreditsEntryQueryFilter.getGrantedStatuses())){

                wheres.add(" entry.grantedStatus IN (:grantedStatus) ");
                paramNames.add("grantedStatus");
                values.add(departmentCreditsEntryQueryFilter.getGrantedStatuses());

            }


            if(departmentCreditsEntryQueryFilter.getId()!=null){

                wheres.add(" entry.id = :entryId");

                paramNames.add("entryId");
                values.add(departmentCreditsEntryQueryFilter.getId());
            }

            if(departmentCreditsEntryQueryFilter.getCreditsPeriodId()!=null){

                wheres.add(" creditsPeriod.id = :creditsPeriodId");

                paramNames.add("creditsPeriodId");
                values.add(departmentCreditsEntryQueryFilter.getCreditsPeriodId());
            }
            
            if(departmentCreditsEntryQueryFilter.getDepartmentId()!=null){

                wheres.add(" department.id = :departmentId");

                paramNames.add("departmentId");
                values.add(departmentCreditsEntryQueryFilter.getDepartmentId());
            }
            

            /*
            if(departmentCreditsEntryQueryFilter.hasCredits!=null && departmentCreditsEntryQueryFilter.hasCredits.booleanValue()==true){

                wheres.add(" entry.numberOfCredits > :hasCredits");

                paramNames.add("hasCredits");
                values.add(0);
            }*/
/*
            if(creditsEntryQueryFilter.getEmploymentQueryFilter()!=null) {
                EmploymentQueryFilter employmentQueryFilter = creditsEntryQueryFilter.getEmploymentQueryFilter();

                String cuil = employmentQueryFilter.getCuil();
                if(!StringUtils.isEmpty(cuil)) {


                    wheres.add(" person.cuil = :cuil");

                    paramNames.add("cuil");
                    values.add(cuil);

                }

                String apellidoNombre = employmentQueryFilter.getApellidoNombre();
                if(!StringUtils.isEmpty(apellidoNombre)) {

                    wheres.add(" person.apellidoNombre = :apellidoNombre ");

                    paramNames.add("apellidoNombre");
                    values.add(apellidoNombre);

                }

                String codigoCentro = employmentQueryFilter.getCodigoCentro();
                if(!StringUtils.isEmpty(codigoCentro)) {

                    wheres.add(" subDepartment.codigoCentro = :codigoCentro");
                    paramNames.add("codigoCentro");
                    values.add(codigoCentro);

                }

                String codigoSector = employmentQueryFilter.getCodigoSector();
                if(!StringUtils.isEmpty(codigoSector)) {
                    wheres.add(" subDepartment.codigoSector = :codigoSector ");
                    paramNames.add("codigoSector");
                    values.add(codigoSector);
                }

                String categoryCode = employmentQueryFilter.getCategoryCode();
                if(!StringUtils.isEmpty(categoryCode)) {
                    wheres.add(" category.code = :categoryCode");
                    paramNames.add("categoryCode");
                    values.add(categoryCode);
                }

                Long departmentId = employmentQueryFilter.getDepartmentId();
                if(departmentId!=null) {
                    wheres.add(" subDepartment.department.id  = :departmentId");
                    paramNames.add("departmentId");
                    values.add(departmentId);
                }

                Long employmentId = employmentQueryFilter.getEmploymentId();
                if(employmentId!=null) {
                    wheres.add(" employment.id = :employmentId");
                    paramNames.add("employmentId");
                    values.add(employmentId);
                }

                Long previousEmploymentId = employmentQueryFilter.getPreviousEmploymentId();
                if(previousEmploymentId!=null) {
                    wheres.add(" previousEmployment.id = :previousEmploymentId");
                    paramNames.add("previousEmploymentId");
                    values.add(previousEmploymentId);
                }
                
                List<Long> personsIds = employmentQueryFilter.getPersonsIds();
                if(!CollectionUtils.isEmpty(personsIds)){
                    StringBuilder sb = new StringBuilder();
                    if(personsIds.size()==1) {
                        sb.append(" person.id = :personId");
                        paramNames.add("personId");
                        values.add(personsIds.get(0));

                    }else{
                        sb.append(" (1<>1 ");
                        //take into account the limit of 1000 elements in an IN condition in Oracle
                        List<List<Long>> listsOfPersonsIds = org.dpi.util.CollectionUtils.chopped(personsIds, 999);

                        int subGroupId = 1;
                        for(List<Long> listOfPersonsIds :listsOfPersonsIds){
                            sb.append(" OR person.id IN (:subGroupId"+subGroupId+")");

                            paramNames.add("subGroupId"+subGroupId);
                            values.add(listOfPersonsIds);
                            subGroupId = subGroupId+1;
                        }
                        sb.append(")");

                    }
                    wheres.add(sb.toString());
                }


                if(!CollectionUtils.isEmpty(employmentQueryFilter.getEmploymentStatuses())){
                    wheres.add(" employment.status IN (:employmentStatuses)");

                    paramNames.add("employmentStatuses");
                    values.add(employmentQueryFilter.getEmploymentStatuses());
                }



            }*/


        }
    }

	
	
	@Override
    public PageList<DepartmentCreditsEntry> findCreditsEntries(final DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter){
        throw new UnsupportedOperationException();
    }
}

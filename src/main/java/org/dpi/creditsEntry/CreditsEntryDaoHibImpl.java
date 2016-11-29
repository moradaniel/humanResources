package org.dpi.creditsEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind.OrderDirection;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class CreditsEntryDaoHibImpl extends BaseDAOHibernate implements CreditsEntryDao
{
    @SuppressWarnings("unchecked")
    public List<CreditsEntry> findAll()
    {
        Chronometer timer = new Chronometer();

        if (log.isDebugEnabled()) log.debug("attempting to find all creditsEntries");

        List<CreditsEntry> list = (List<CreditsEntry>)getHibernateTemplate().find("from CreditsEntryImpl");

        if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " entry in " + timer.printElapsedTime());

        return list;
    }	


    @SuppressWarnings( "unchecked" )
    public List<CreditsEntry> find(final CreditsEntryQueryFilter creditsEntryQueryFilter){
        Chronometer timer = new Chronometer();

        if (log.isDebugEnabled()) log.debug("attempting to find entry with filter '" + creditsEntryQueryFilter.toString() + "'" );

        List<CreditsEntry> list = (List<CreditsEntry>)getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session sess)
                    throws HibernateException, SQLException  {	


                List<String> wheres = new ArrayList<String>();
                List<String> paramNames = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();

                StringBuffer queryBuilder = new StringBuffer();

                queryBuilder.append(" FROM CreditsEntryImpl entry ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH entry.employment employment ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH employment.person person ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH subDepartment.department department ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH employment.category category ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH entry.creditsPeriod creditsPeriod ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH employment.previousEmployment previousEmployment ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH previousEmployment.category previousCategory ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");

                buildWhereClause(creditsEntryQueryFilter,wheres,paramNames,values);


                String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                        org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

                String queryWithOrdering = queryWithoutOrdering;
                if (creditsEntryQueryFilter != null && creditsEntryQueryFilter.getOrderBy() != null) {
                    queryWithOrdering += " ORDER BY LOWER(" + creditsEntryQueryFilter.getOrderBy() + ")"
                            + (creditsEntryQueryFilter.getOrderDirection().equals(OrderDirection.DESCENDING) ? " DESC " : " ASC ");
                } else {
                    queryWithOrdering += " ORDER BY LOWER(person.apellidoNombre) ASC, "+
                            "          employment.startDate desc ";

                }


                int startIndex = creditsEntryQueryFilter != null ? creditsEntryQueryFilter.getStartIndex() : null;
                //int maxResults = creditsEntryQueryFilter != null ? creditsEntryQueryFilter.getMaxResults() : null;
                Integer maxResults = null;

                return findByNamedParam(" Select distinct entry  " + queryWithOrdering, paramNames, values, startIndex, maxResults);

            }
        });

        //if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
        return list;
    }

    /*
    public static  String buildWhereClause(CreditsEntryQueryFilter entryQueryFilter) {
        StringBuffer sb = new StringBuffer();

        if(!CollectionUtils.isEmpty(entryQueryFilter.getCreditsEntryTypes())){
            sb.append(" AND (");
            for (Iterator iterator = entryQueryFilter.getCreditsEntryTypes().iterator(); iterator.hasNext();) {
                CreditsEntryType creditsEntryType = (CreditsEntryType) iterator.next();
                sb.append(" entry.creditsEntryType = '"+creditsEntryType.name()+"' ");
                if(iterator.hasNext()){
                    sb.append(" OR ");
                }
            }
            sb.append(" ) ");				

        }

        if(!CollectionUtils.isEmpty(entryQueryFilter.getGrantedStatuses())){
            sb.append(" AND (");
            for (Iterator iterator = entryQueryFilter.getGrantedStatuses().iterator(); iterator.hasNext();) {
                GrantedStatus grantedStatus = (GrantedStatus) iterator.next();
                sb.append(" entry.grantedStatus = '"+grantedStatus.name()+"' ");
                if(iterator.hasNext()){
                    sb.append(" OR ");
                }
            }
            sb.append(" ) ");				

        }


        if(entryQueryFilter.getId()!=null){
            sb.append(" AND entry.id = ").append(entryQueryFilter.getId()).append(" ");
        }

        if(entryQueryFilter.getIdCreditsPeriod()!=null){
            sb.append(" AND creditsPeriod.id = ").append(entryQueryFilter.getIdCreditsPeriod()).append(" ");
        }

        if(entryQueryFilter.hasCredits!=null && entryQueryFilter.hasCredits.booleanValue()==true){
            sb.append(" AND entry.numberOfCredits > 0 ").append(" ");
        }

        if(entryQueryFilter.getEmploymentQueryFilter()!=null) {
            EmploymentQueryFilter employmentQueryFilter = entryQueryFilter.getEmploymentQueryFilter();

            String cuil = employmentQueryFilter.getCuil();
            if(cuil!=null) {
                sb.append(" AND employment.person.cuil = '").append(cuil).append("'");
            }

            String codigoCentro = employmentQueryFilter.getCodigoCentro();
            if(codigoCentro!=null) {
                sb.append(" AND employment.subDepartment.codigoCentro = '").append(codigoCentro).append("'");
            }

            String codigoSector = employmentQueryFilter.getCodigoSector();
            if(codigoSector!=null) {
                sb.append(" AND employment.subDepartment.codigoSector = '").append(codigoSector).append("'");
            }

            String categoryCode = employmentQueryFilter.getCategoryCode();
            if(categoryCode!=null) {
                sb.append(" AND employment.category.code = '").append(categoryCode).append("'");
            }

            Long departmentId = employmentQueryFilter.getDepartmentId();
            if(departmentId!=null) {
                sb.append(" AND subDepartment.department.id = ").append(departmentId).append(" ");
            }

            Long idEmpleo = employmentQueryFilter.getEmploymentId();
            if(idEmpleo!=null) {
                sb.append(" AND employment.id = '").append(idEmpleo).append("'");
            }

            List<Long> personsIds = employmentQueryFilter.getPersonsIds();
            if(!CollectionUtils.isEmpty(personsIds)){
                if(personsIds.size()==1) {
                    sb.append(" AND person.id = '").append(personsIds.get(0)).append("'");
                }else{
                    sb.append(" AND (1<>1 ");
                    //take into account the limit of 1000 elements in an IN condition in Oracle
                    List<List<Long>> listsOfAgentsIds = org.dpi.util.CollectionUtils.chopped(personsIds, 999);
                    for(List<Long> listOfAgentsIds :listsOfAgentsIds){
                        sb.append(" OR person.id IN ( ")
                        .append(StringUtils.collectionToDelimitedString(listOfAgentsIds, ","))
                        .append(")");

                    }
                    sb.append(")");

                }

            }


            if(!CollectionUtils.isEmpty(employmentQueryFilter.getEmploymentStatuses())){
                sb.append(" AND (");
                for (Iterator iterator = employmentQueryFilter.getEmploymentStatuses().iterator(); iterator.hasNext();) {
                    EmploymentStatus employmentStatus = (EmploymentStatus) iterator.next();
                    sb.append(" employment.status = '"+employmentStatus.name()+"' ");
                    if(iterator.hasNext()){
                        sb.append(" OR ");
                    }
                }
                sb.append(" ) ");				

            }

        }
        return sb.toString();
    }*/



    public static void buildWhereClause(CreditsEntryQueryFilter creditsEntryQueryFilter,
            List<String> wheres, List<String> paramNames, List<Object> values) {

        if (creditsEntryQueryFilter != null) {

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsPeriodNames())){
                wheres.add("  creditsPeriod.name IN ( :creditsPeriodsNames )  ");
                paramNames.add("creditsPeriodsNames");
                values.add(creditsEntryQueryFilter.getCreditsPeriodNames());

            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryTypes())){


                wheres.add("  entry.creditsEntryType IN (:creditsEntryType) ");
                paramNames.add("creditsEntryType");
                values.add(creditsEntryQueryFilter.getCreditsEntryTypes());

            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getGrantedStatuses())){

                wheres.add(" entry.grantedStatus IN (:grantedStatus) ");
                paramNames.add("grantedStatus");
                values.add(creditsEntryQueryFilter.getGrantedStatuses());

            }


            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryIds())){

                wheres.add(" entry.id IN (:creditsEntryIds) ");

                paramNames.add("creditsEntryIds");
                values.add(creditsEntryQueryFilter.getCreditsEntryIds());
            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsPeriodIds())){

                wheres.add(" creditsPeriod.id IN (:creditsPeriodIds) ");

                paramNames.add("creditsPeriodIds");
                values.add(creditsEntryQueryFilter.getCreditsPeriodIds());
            }
            
            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getPersonIds())){
                wheres.add("  person.id IN ( :personIds )  ");
                paramNames.add("personIds");
                values.add(creditsEntryQueryFilter.getPersonIds());

            }

            if(creditsEntryQueryFilter.hasCredits!=null && creditsEntryQueryFilter.hasCredits.booleanValue()==true){

                wheres.add(" entry.numberOfCredits > :hasCredits");

                paramNames.add("hasCredits");
                values.add(0);
            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getNotInDepartmentCodes())){
                wheres.add("  department.code NOT IN ( :notInDepartmentCodes )  ");
                paramNames.add("notInDepartmentCodes");
                values.add(creditsEntryQueryFilter.getNotInDepartmentCodes());

            }
            

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



            }


        }
    }


    @Override
    public PageList<CreditsEntry> findCreditsEntries(final CreditsEntryQueryFilter creditsEntryQueryFilter){

        List<String> wheres = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();

        StringBuffer queryBuilder = new StringBuffer();


        queryBuilder.append(" FROM CreditsEntryImpl entry ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH entry.employment employment ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.person person ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH subDepartment.department department ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.category category ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH entry.creditsPeriod creditsPeriod ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.previousEmployment previousEmployment ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH previousEmployment.category previousCategory ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");



        buildWhereClause(creditsEntryQueryFilter,wheres,paramNames,values);


        String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

        String queryWithOrdering = queryWithoutOrdering;
        if (creditsEntryQueryFilter != null && creditsEntryQueryFilter.getOrderBy() != null) {
            queryWithOrdering += " ORDER BY LOWER(" + creditsEntryQueryFilter.getOrderBy() + ")"
                    + (creditsEntryQueryFilter.getOrderDirection().equals(OrderDirection.DESCENDING) ? " DESC " : " ASC ");
        } else {
            queryWithOrdering += " ORDER BY creditsPeriod.name ASC ,"+
                    "          department.name ASC, "+
                    "          LOWER(person.apellidoNombre) ASC";

        }



        return new PageList<CreditsEntry>(
                findByNamedParam(" Select distinct entry  " + queryWithOrdering, paramNames, values, creditsEntryQueryFilter != null ? creditsEntryQueryFilter
                        .getStartIndex() : null, creditsEntryQueryFilter != null ? creditsEntryQueryFilter.getMaxResults() : null), countByNamedParam(
                                " SELECT count(distinct entry) " + queryWithoutOrdering, paramNames, values));


    }
}

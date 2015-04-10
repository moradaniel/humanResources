package org.dpi.creditsPeriod;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class CreditsPeriodDaoHibImpl extends BaseDAOHibernate implements CreditsPeriodDao
{
    public CreditsPeriodDaoHibImpl() {
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("unchecked")
    public List<CreditsPeriod> findAll()
    {
        Chronometer timer = new Chronometer();

        if (log.isDebugEnabled()) log.debug("attempting to find all CreditsPeriod");

        List<CreditsPeriod> list = getHibernateTemplate().find("from CreditsPeriodImpl order by name");

        if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " CreditsPeriod in " + timer.printElapsedTime());

        return list;
    }	


    @SuppressWarnings( "unchecked" )
    public List<CreditsPeriod> find(final CreditsPeriodQueryFilter creditsPeriodQueryFilter){
        Chronometer timer = new Chronometer();

        if (log.isDebugEnabled()) log.debug("attempting to find CreditsPeriod with filter '" + creditsPeriodQueryFilter.toString() + "'" );

        List<CreditsPeriod> list = getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session sess)
                    throws HibernateException, SQLException  {	


                List<String> wheres = new ArrayList<String>();
                List<String> paramNames = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();

                StringBuffer queryBuilder = new StringBuffer();

                queryBuilder.append("Select creditsPeriod ");

                queryBuilder.append(" FROM CreditsPeriodImpl creditsPeriod ");
                queryBuilder.append(" LEFT OUTER JOIN FETCH creditsPeriod.previousCreditsPeriod previousCreditsPeriod ");

                buildWhereClause(creditsPeriodQueryFilter,wheres,paramNames,values);


                String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                        org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

                String queryWithOrdering = queryWithoutOrdering;

                queryWithOrdering += " ORDER BY creditsPeriod.name desc ";

                int startIndex = 0;
                Integer maxResults = null;

                return findByNamedParam(queryWithOrdering, paramNames, values, startIndex, maxResults);

            }
        });

        //if (log.isDebugEnabled()) log.debug("successfully retrieved  with codigo '" + codigo+ "' in " + timer.printElapsedTime());
        return list;
    }

    public static void buildWhereClause(CreditsPeriodQueryFilter creditsPeriodQueryFilter,
            List<String> wheres, List<String> paramNames, List<Object> values) {

        if(creditsPeriodQueryFilter!=null) {
            String name = creditsPeriodQueryFilter.getName();
            if(!StringUtils.isEmpty(name)) {
                wheres.add(" creditsPeriod.name = :name ");
                paramNames.add("name");
                values.add(name);
            }


            Date startDate = creditsPeriodQueryFilter.getStartDate();
            if(startDate!=null) {
                wheres.add(" creditsPeriod.startDate >= :startDate ");
                paramNames.add("startDate");
                values.add(startDate);
            }

            Date endDate = creditsPeriodQueryFilter.getEndDate();
            if(endDate!=null) {
                wheres.add(" creditsPeriod.endDate <= :endDate ");
                paramNames.add("endDate");
                values.add(endDate);
            }

            if(!CollectionUtils.isEmpty(creditsPeriodQueryFilter.getStatuses())){
                wheres.add(" creditsPeriod.status IN (:status) ");
                paramNames.add("status");
                values.add(creditsPeriodQueryFilter.getStatuses());
            }


        }
    }



    @Override
    public CreditsPeriod findById(Long id) {
        Chronometer timer = new Chronometer();

        if (log.isDebugEnabled()) log.debug("attempting to find CreditsPeriod with id: '" + id + "'");

        List list = getHibernateTemplate().find("from CreditsPeriodImpl where id=?", id);

        CreditsPeriod creditsPeriod = (list.size() > 0) ? (CreditsPeriod)list.get(0) : null;

        if (creditsPeriod == null) {
            log.warn("Unable to find CreditsPeriod with id: '" + id + "'");
            return null;
        }

        if (log.isDebugEnabled()) log.debug("successfully retrieved CreditsPeriod with id: '" + id + "' in " + timer.printElapsedTime());

        return creditsPeriod;
    }


}

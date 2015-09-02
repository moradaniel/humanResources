package org.dpi.creditsManagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryDaoHibImpl;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentService;
import org.dpi.departmentCreditsEntry.CreditsEntryTransactionType;
import org.dpi.departmentCreditsEntry.DepartmentCreditsEntryDaoHibImpl;
import org.dpi.departmentCreditsEntry.DepartmentCreditsEntryQueryFilter;
import org.dpi.departmentCreditsEntry.DepartmentCreditsEntryType;
import org.dpi.employment.EmploymentQueryFilter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;

public class CreditsManagerServiceImpl extends BaseDAOHibernate implements CreditsManagerService{

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "creditsPeriodService")
    private CreditsPeriodService creditsPeriodService;
    
    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    Map<String,Integer> creditosPorCargaInicial = new HashMap<String,Integer>();

    Map<String,Integer> creditosARestarPorIngreso = new HashMap<String,Integer>();

    Map<String,Integer> creditosASumarPorBaja = new HashMap<String,Integer>();

    Map<String, Map<String, Integer>> creditosPorAscenso = new HashMap<String,Map<String, Integer>>();

    //String MAX_CATEGORIA_PARA_PROFESIONALES_SIN_CONSUMO_DE_CREDITOS = "21";



    public CreditsManagerServiceImpl() {
        creditosPorCargaInicial.put("12", 60);
        creditosPorCargaInicial.put("13", 60);
        creditosPorCargaInicial.put("14", 56);
        creditosPorCargaInicial.put("15", 52);
        creditosPorCargaInicial.put("16", 48);
        creditosPorCargaInicial.put("17", 42);
        creditosPorCargaInicial.put("18", 36);
        creditosPorCargaInicial.put("19", 28);
        creditosPorCargaInicial.put("20", 20);
        creditosPorCargaInicial.put("21", 12);
        creditosPorCargaInicial.put("22", 6);
        creditosPorCargaInicial.put("23", 4);
        creditosPorCargaInicial.put("24", 2);


        //------------------------------------
        creditosARestarPorIngreso.put("12", 1000);
        creditosARestarPorIngreso.put("13", 1010);
        creditosARestarPorIngreso.put("14", 1030);
        creditosARestarPorIngreso.put("15", 1061);
        creditosARestarPorIngreso.put("16", 1103);
        creditosARestarPorIngreso.put("17", 1158);
        creditosARestarPorIngreso.put("18", 1227);
        creditosARestarPorIngreso.put("19", 1314);
        creditosARestarPorIngreso.put("20", 1420);
        creditosARestarPorIngreso.put("21", 1548);
        creditosARestarPorIngreso.put("22", 1710);
        creditosARestarPorIngreso.put("23", 1916);
        creditosARestarPorIngreso.put("24", 2182);


        //-------------------------------------

        creditosASumarPorBaja.put("12", 750);
        creditosASumarPorBaja.put("13", 758);
        creditosASumarPorBaja.put("14", 773);
        creditosASumarPorBaja.put("15", 796);
        creditosASumarPorBaja.put("16", 827);
        creditosASumarPorBaja.put("17", 869);
        creditosASumarPorBaja.put("18", 920);
        creditosASumarPorBaja.put("19", 986);
        creditosASumarPorBaja.put("20", 1065);
        creditosASumarPorBaja.put("21", 1161);
        creditosASumarPorBaja.put("22", 1283);
        creditosASumarPorBaja.put("23", 1437);
        creditosASumarPorBaja.put("24", 1637);

        //-------------------------------------



        creditosPorAscenso.put("12", new HashMap<String,Integer>());
        creditosPorAscenso.get("12").put("13", 10);
        creditosPorAscenso.get("12").put("14", 30);
        creditosPorAscenso.get("12").put("15", 61);
        creditosPorAscenso.get("12").put("16", 103);
        creditosPorAscenso.get("12").put("17", 158);
        creditosPorAscenso.get("12").put("18", 227);
        creditosPorAscenso.get("12").put("19", 314);
        creditosPorAscenso.get("12").put("20", 420);
        creditosPorAscenso.get("12").put("21", 548);
        creditosPorAscenso.get("12").put("22", 710);
        creditosPorAscenso.get("12").put("23", 916);
        creditosPorAscenso.get("12").put("24", 1182);

        creditosPorAscenso.put("13", new HashMap<String,Integer>());
        creditosPorAscenso.get("13").put("14", 20);
        creditosPorAscenso.get("13").put("15", 51);//
        creditosPorAscenso.get("13").put("16", 93);//
        creditosPorAscenso.get("13").put("17", 148);
        creditosPorAscenso.get("13").put("18", 217);
        creditosPorAscenso.get("13").put("19", 304);//
        creditosPorAscenso.get("13").put("20", 410);
        creditosPorAscenso.get("13").put("21", 538);
        creditosPorAscenso.get("13").put("22", 700);//
        creditosPorAscenso.get("13").put("23", 906);
        creditosPorAscenso.get("13").put("24", 1172);

        creditosPorAscenso.put("14", new HashMap<String,Integer>());
        creditosPorAscenso.get("14").put("15", 31);
        creditosPorAscenso.get("14").put("16", 73);
        creditosPorAscenso.get("14").put("17", 128);
        creditosPorAscenso.get("14").put("18", 197);
        creditosPorAscenso.get("14").put("19", 284);
        creditosPorAscenso.get("14").put("20", 390);
        creditosPorAscenso.get("14").put("21", 518);
        creditosPorAscenso.get("14").put("22", 680);
        creditosPorAscenso.get("14").put("23", 886);
        creditosPorAscenso.get("14").put("24", 1152);

        creditosPorAscenso.put("15", new HashMap<String,Integer>());
        creditosPorAscenso.get("15").put("16", 42 );
        creditosPorAscenso.get("15").put("17", 97);
        creditosPorAscenso.get("15").put("18", 166);
        creditosPorAscenso.get("15").put("19", 253);
        creditosPorAscenso.get("15").put("20", 359);//
        creditosPorAscenso.get("15").put("21", 487);
        creditosPorAscenso.get("15").put("22", 649);
        creditosPorAscenso.get("15").put("23", 855);
        creditosPorAscenso.get("15").put("24", 1121);

        creditosPorAscenso.put("16", new HashMap<String,Integer>());
        creditosPorAscenso.get("16").put("17", 55);
        creditosPorAscenso.get("16").put("18", 124);
        creditosPorAscenso.get("16").put("19", 211);
        creditosPorAscenso.get("16").put("20", 317);//
        creditosPorAscenso.get("16").put("21", 445);
        creditosPorAscenso.get("16").put("22", 607);
        creditosPorAscenso.get("16").put("23", 813);
        creditosPorAscenso.get("16").put("24", 1079);

        creditosPorAscenso.put("17", new HashMap<String,Integer>());
        creditosPorAscenso.get("17").put("18", 69);
        creditosPorAscenso.get("17").put("19", 156);
        creditosPorAscenso.get("17").put("20", 262);
        creditosPorAscenso.get("17").put("21", 390);
        creditosPorAscenso.get("17").put("22", 552);
        creditosPorAscenso.get("17").put("23", 758);
        creditosPorAscenso.get("17").put("24", 1024);

        creditosPorAscenso.put("18", new HashMap<String,Integer>());
        creditosPorAscenso.get("18").put("19", 87);
        creditosPorAscenso.get("18").put("20", 193);
        creditosPorAscenso.get("18").put("21", 321);
        creditosPorAscenso.get("18").put("22", 483);
        creditosPorAscenso.get("18").put("23", 689);
        creditosPorAscenso.get("18").put("24", 955);

        creditosPorAscenso.put("19", new HashMap<String,Integer>());
        creditosPorAscenso.get("19").put("20", 106);//
        creditosPorAscenso.get("19").put("21", 234);
        creditosPorAscenso.get("19").put("22", 396);
        creditosPorAscenso.get("19").put("23", 602);
        creditosPorAscenso.get("19").put("24", 868);

        creditosPorAscenso.put("20", new HashMap<String,Integer>());
        creditosPorAscenso.get("20").put("21", 128);
        creditosPorAscenso.get("20").put("22", 290);//
        creditosPorAscenso.get("20").put("23", 496);
        creditosPorAscenso.get("20").put("24", 762);

        creditosPorAscenso.put("21", new HashMap<String,Integer>());
        creditosPorAscenso.get("21").put("22", 162);//
        creditosPorAscenso.get("21").put("23", 368);
        creditosPorAscenso.get("21").put("24", 634);

        creditosPorAscenso.put("22", new HashMap<String,Integer>());
        creditosPorAscenso.get("22").put("23", 206);
        creditosPorAscenso.get("22").put("24", 472);//

        creditosPorAscenso.put("23", new HashMap<String,Integer>());
        creditosPorAscenso.get("23").put("24", 266);


    }


    public int getCreditosPorCargaInicial(String categoryCode){
        Integer creditos = creditosPorCargaInicial.get(categoryCode);
        if(creditos==null){
            creditos=0;
        }
        return creditos;

    }


    public int getCreditosPorBaja(String categoryCode){
        Integer creditos = creditosASumarPorBaja.get(categoryCode);
        if(creditos==null){
            creditos=0;
        }
        return creditos;

    }


    public int getCreditosPorAscenso(String currentCategoryCode, String newCategoryCode){
        Integer creditos = 0;
        creditos = this.creditosPorAscenso.get(currentCategoryCode).get(newCategoryCode);	

        if(creditos==null){
            creditos=0;
        }
        return creditos;
    }

    public int getCreditosPorIngreso(String categoryCode){
        Integer creditos = this.creditosARestarPorIngreso.get(categoryCode);
        if(creditos==null){
            creditos=0;
        }
        return creditos;

    }


    @SuppressWarnings("unchecked")
    @Override
    public 	Long getCreditosPorCargaInicialDeReparticion(final Long creditsPeriodId,final long departmentId){
        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
        employmentQueryFilter.setDepartmentId(departmentId);
        creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.CargaInicialAgenteExistente);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
        return 	getCreditsEntriesSum(creditsEntryQueryFilter);

        /*
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {

				Chronometer timer = new Chronometer();

				if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with id: '" + departmentId + "'");

				String queryStr = "select sum(entry.cantidadCreditos) " +
						" from CreditsEntryImpl entry " +
						" INNER JOIN entry.employment employment " +
						" INNER JOIN employment.subDepartment subDepartment " +
						" INNER JOIN subDepartment.department department "+
						" INNER JOIN employment.person person "+
						" where department.id='"+departmentId+"'" +
						" AND entry.creditsEntryType = '"+CreditsEntryType.CargaInicialAgenteExistente.name()+"'"+
						" AND entry.creditsPeriod.id = '"+creditsPeriodId+"'";

				Query query = sess.createQuery(queryStr);

				Long totalAmount = (Long) query.uniqueResult();


				if (log.isDebugEnabled()) log.debug("successfully retrieved department with id: '" + departmentId + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});*/
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getCreditosPorBajasDeReparticion(final Long creditsPeriodId,final long departmentId) {

        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
        employmentQueryFilter.setDepartmentId(departmentId);
        creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.BajaAgente);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
        return 	getCreditsEntriesSum(creditsEntryQueryFilter);
    }	


    //TODO this should be read from database as a DepartmentCreditEntry of type RETENTION
    //so this logic should be moved to an UpdatetainedCreditsByDepartment method
    public Long getRetainedCreditsByDepartment(final Long creditsPeriodId,final long departmentId) {

        //TODO this is just a quick workaround for 2014, we have to handle historic retained credits, for historic periods
        //for now we retain 0 for historic periods
        if(creditsPeriodService.getCurrentCreditsPeriod().getId().longValue() != creditsPeriodId) {
            return 0l;
        }


        //TODO this is just a quick workaround, we need attributes to be added to departments
        //for example departments_attributes.eligibleForCreditsRetention = false
        //for now all departments with codes starting with "4" are not retaining credits 
        Department department = departmentService.findById(departmentId);
        if(department.getCode().startsWith("4")) {
            return 0l;
        }
        
        long retainedCredits = 0; 

        long creditsBecauseOfDepartmentDeactivations = getCreditosPorBajasDeReparticion(creditsPeriodId, departmentId);

        long fixedRetainedCredits = 0;
        long baseLimit = 0;
        float percentage = 0;

        if(creditsBecauseOfDepartmentDeactivations <= 0) {
            return 0l;
        }else
          if(creditsBecauseOfDepartmentDeactivations > 0  && creditsBecauseOfDepartmentDeactivations <=     773) {
            baseLimit = 0;
            fixedRetainedCredits = 0;
            percentage = 0.10f;
          }else
           if(creditsBecauseOfDepartmentDeactivations >= 774  && creditsBecauseOfDepartmentDeactivations <=     827) {
                    baseLimit = 774;
                    fixedRetainedCredits = 77;
                    percentage = 0.14f;
           }else
           if(creditsBecauseOfDepartmentDeactivations >= 828  && creditsBecauseOfDepartmentDeactivations <=     920) {
                        baseLimit = 828;
                        fixedRetainedCredits = 115;
                        percentage = 0.18f;
           }else
           if(creditsBecauseOfDepartmentDeactivations >= 921  && creditsBecauseOfDepartmentDeactivations <=     1065) {
                baseLimit = 921;
                fixedRetainedCredits = 165;
                percentage = 0.22f;
            }else
           if(creditsBecauseOfDepartmentDeactivations >= 1066  && creditsBecauseOfDepartmentDeactivations <=     1283) {
                baseLimit = 1066;
                fixedRetainedCredits = 234;
                percentage = 0.26f;
            }else
           if(creditsBecauseOfDepartmentDeactivations >= 1284  && creditsBecauseOfDepartmentDeactivations <=     1637) {
                baseLimit = 1284;
                fixedRetainedCredits = 333;
                percentage = 0.28f;
            }
            else
                if(creditsBecauseOfDepartmentDeactivations >= 1638) {
                    baseLimit = 1638;
                    fixedRetainedCredits = 458;
                    percentage = 0.30f;
    
                }     

        retainedCredits = fixedRetainedCredits + Math.round(percentage * (creditsBecauseOfDepartmentDeactivations - baseLimit));


        return retainedCredits;
    }


    public Long getTotalCreditosDisponiblesAlInicioPeriodo(final Long creditsPeriodId,final long departmentId) {

        Long creditosDisponiblesInicioPeriodo = getCreditosDisponiblesAlInicioPeriodo(creditsPeriodId,departmentId);

        Long creditosAcreditadosPorBajaDurantePeriodoActual = getCreditosPorBajasDeReparticion(creditsPeriodId, departmentId);

        Long retainedCreditsPeriodoActual = getRetainedCreditsByDepartment(creditsPeriodId, departmentId);

       
        Long totalCreditosReparticionAjustes_Debito_PeriodoActual = this.getCreditosReparticionAjustesDebitoPeriodo(creditsPeriodId,departmentId);
        
        Long totalCreditosReparticionAjustes_Credito_PeriodoActual = this.getCreditosReparticionAjustesCreditoPeriodo(creditsPeriodId,departmentId);
                
        Long totalCreditosReasignadosDeRetencion_PeriodoActual = this.getCreditosReparticion_ReasignadosDeRetencion_Periodo(creditsPeriodId,departmentId);
        

        Long currentPeriodTotalAvailableCredits = creditosDisponiblesInicioPeriodo 
                                                 +creditosAcreditadosPorBajaDurantePeriodoActual 
                                                 -retainedCreditsPeriodoActual
                                                 -totalCreditosReparticionAjustes_Debito_PeriodoActual
                                                 +totalCreditosReparticionAjustes_Credito_PeriodoActual
                                                 +totalCreditosReasignadosDeRetencion_PeriodoActual;
        
        return currentPeriodTotalAvailableCredits;

    }

    @SuppressWarnings("unchecked")
    @Override
    public 	Long getCreditsEntriesSum(final CreditsEntryQueryFilter creditsEntryQueryFilter){
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session sess)
                    throws HibernateException, SQLException  {

                Chronometer timer = new Chronometer();

                if (log.isDebugEnabled()) log.debug("attempting to calculate credits for Reparticion with id: '" + creditsEntryQueryFilter.getEmploymentQueryFilter().getDepartmentId() + "'");

                StringBuffer sb = new StringBuffer();
                sb.append(" select sum(entry.numberOfCredits) ")
                .append(" from CreditsEntryImpl entry ")
                .append(" INNER JOIN entry.employment employment " )
                .append(" INNER JOIN employment.subDepartment subDepartment " )
                .append(" INNER JOIN subDepartment.department department ");

                String where = " WHERE 1=1 "+buildWhereClause(creditsEntryQueryFilter);

                sb.append(where);

                /*" where department.id = :departmentId " +
						" AND entry.creditsEntryType IN (:creditsTypes) "+
						" AND entry.grantedStatus IN (:grantedStatuses) "+
						" AND entry.creditsPeriod.id = :creditsPeriod ";*/
                Query query = sess.createQuery(sb.toString());

                setNamedParameters(query,creditsEntryQueryFilter);

                Long totalAmount = (Long) query.uniqueResult();



                if (log.isDebugEnabled()) log.debug("successfully calculated credits for Reparticion with id: '" + creditsEntryQueryFilter.getEmploymentQueryFilter().getDepartmentId() + "' in " + timer.printElapsedTime());
                if(totalAmount==null){
                    totalAmount=new Long(0);
                }
                return totalAmount;
            }
        });
    }


    private String buildWhereClause(CreditsEntryQueryFilter creditsEntryQueryFilter) {
        StringBuffer sb = new StringBuffer();

        if(creditsEntryQueryFilter!=null) {
            EmploymentQueryFilter employmentQueryFilter = creditsEntryQueryFilter.getEmploymentQueryFilter();
            if(employmentQueryFilter!=null) {

                Long idReparticion = employmentQueryFilter.getDepartmentId();
                if(idReparticion!=null) {
                    sb.append(" AND department.id = :idReparticion ");
                }
            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryTypes())) {
                sb.append(" AND entry.creditsEntryType IN ( :creditsEntryTypes ) ");
            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getGrantedStatuses())) {
                sb.append(" AND entry.grantedStatus IN (:grantedStatuses) ");
            }


            if(creditsEntryQueryFilter.getIdCreditsPeriod()!=null && creditsEntryQueryFilter.getIdCreditsPeriod()>0) {
                sb.append(" AND entry.creditsPeriod.id = :creditsPeriod ");
            }



        }
        return sb.toString();
    }


    public void setNamedParameters(Query query,		CreditsEntryQueryFilter creditsEntryQueryFilter) {

        if(creditsEntryQueryFilter!=null) {
            EmploymentQueryFilter employmentQueryFilter = creditsEntryQueryFilter.getEmploymentQueryFilter();
            if(employmentQueryFilter!=null) {

                Long idReparticion = employmentQueryFilter.getDepartmentId();
                if(idReparticion!=null) {
                    query.setLong("idReparticion", idReparticion);
                }
            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryTypes())) {
                query.setParameterList("creditsEntryTypes", creditsEntryQueryFilter.getCreditsEntryTypes());
            }

            if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getGrantedStatuses())) {
                query.setParameterList("grantedStatuses", creditsEntryQueryFilter.getGrantedStatuses());
            }


            if(creditsEntryQueryFilter.getIdCreditsPeriod()!=null && creditsEntryQueryFilter.getIdCreditsPeriod()>0) {
                query.setLong("creditsPeriod", creditsEntryQueryFilter.getIdCreditsPeriod());
            }



        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public 	Long getTotalCreditos(final CreditsEntryQueryFilter creditsEntryQueryFilter){
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session sess)
                    throws HibernateException, SQLException  {

                Chronometer timer = new Chronometer();

                Long departmentId = creditsEntryQueryFilter.getEmploymentQueryFilter().getDepartmentId();

                if (log.isDebugEnabled()) log.debug("attempting to find CreditEntries with id: '" + departmentId + "'");

                List<String> wheres = new ArrayList<String>();
                List<String> paramNames = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();

                StringBuffer queryBuilder = new StringBuffer();
                
                queryBuilder.append("select sum(entry.numberOfCredits) ");
                queryBuilder.append(" from CreditsEntryImpl entry ");
                queryBuilder.append(" INNER JOIN entry.employment employment ");
                queryBuilder.append(" INNER JOIN employment.subDepartment subDepartment ");
                queryBuilder.append(" INNER JOIN subDepartment.department department ");
                queryBuilder.append(" INNER JOIN entry.creditsPeriod creditsPeriod ");
                
                
                CreditsEntryDaoHibImpl.buildWhereClause(creditsEntryQueryFilter,wheres,paramNames,values);
                
                
                String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                        org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

                String queryWithOrdering = queryWithoutOrdering;
                
                Query queryObject = sess.createQuery(queryWithOrdering);
                
                if (values != null) {
                    for (int i = 0; i < values.size(); i++) {
                        applyNamedParameterToQuery(queryObject, paramNames.get(i), values.get(i), null);
                    }
                }
                
                
                Long totalAmount = (Long) queryObject.uniqueResult();
                
                if (log.isDebugEnabled()) log.debug("successfully retrieved department with id: '" + departmentId + "' in " + timer.printElapsedTime());
                if(totalAmount==null){
                    totalAmount=new Long(0);
                }
                return totalAmount;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getCreditosDisponiblesSegunSolicitado(Long creditsPeriodId,final long departmentId){

        Long totalCreditosDisponiblesAlInicioPeriodo = getTotalCreditosDisponiblesAlInicioPeriodo(creditsPeriodId, departmentId);
        
        Long totalPorIngresosOAscensosSegunSolicitado = this.getCreditosPorIngresosOAscensosSolicitados(creditsPeriodId, departmentId);

        return totalCreditosDisponiblesAlInicioPeriodo - totalPorIngresosOAscensosSegunSolicitado;

    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getCreditosDisponiblesSegunOtorgado(Long creditsPeriodId, final long departmentId){


        Long totalCreditosDisponiblesAlInicioPeriodo = getTotalCreditosDisponiblesAlInicioPeriodo(creditsPeriodId, departmentId);

        Long totalPorIngresosOAscensosSegunOtorgado = this.getCreditosPorIngresosOAscensosOtorgados(creditsPeriodId, departmentId);
        
        return totalCreditosDisponiblesAlInicioPeriodo - totalPorIngresosOAscensosSegunOtorgado;

    }


    @Override
    public Long getCreditosPorIngresosOAscensosSolicitados(Long creditsPeriodId, Long departmentId) {
        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
        employmentQueryFilter.setDepartmentId(departmentId);

        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
        creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);

        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado); //Un entry Otorgado tambien es solicitado(fue solicitado en algun momento)
        return getTotalCreditos(creditsEntryQueryFilter);
    }

    @Override
    public Long getCreditosPorIngresosOAscensosOtorgados(
            Long creditsPeriodId, Long departmentId) {
        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
        employmentQueryFilter.setDepartmentId(departmentId);

        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
        creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
        return getTotalCreditos(creditsEntryQueryFilter);
    }


    @Override
    public Long getCreditosDisponiblesAlInicioPeriodo(Long creditsPeriodId, Long departmentId) {

        CreditsPeriod creditsPeriod = creditsPeriodService.findById(creditsPeriodId);

        //If period does not have previous period return 0
        if(creditsPeriod.getPreviousCreditsPeriod()==null){
            return 0l;
        }

        //get previous period
        CreditsPeriod previousPeriod = creditsPeriod.getPreviousCreditsPeriod();

        Long totalPorCargaInicialPeriodoAnterior = this.getCreditosPorCargaInicialDeReparticion(previousPeriod.getId(),departmentId);

        Long totalPorBajasPeriodoAnterior = this.getCreditosPorBajasDeReparticion(previousPeriod.getId(),departmentId);

        Long retainedCreditsPeriodoAnterior = this.getRetainedCreditsByDepartment(previousPeriod.getId(), departmentId);
        
        Long totalCreditosDisponiblesSegunOtorgadoPeriodoAnterior = this.getCreditosPorIngresosOAscensosOtorgados(previousPeriod.getId(),departmentId);

        Long totalCreditosReparticionAjustes_Debito_PeriodoAnterior = this.getCreditosReparticionAjustesDebitoPeriodo(previousPeriod.getId(),departmentId);
        
        Long totalCreditosReparticionAjustes_Credito_PeriodoAnterior = this.getCreditosReparticionAjustesCreditoPeriodo(previousPeriod.getId(),departmentId);
                
        Long totalCreditosReasignadosDeRetencion_PeriodoAnterior = this.getCreditosReparticion_ReasignadosDeRetencion_Periodo(previousPeriod.getId(),departmentId);
        
        long creditosDisponiblesAlInicioPeriodoActual = totalPorCargaInicialPeriodoAnterior
                +totalPorBajasPeriodoAnterior
                -totalCreditosDisponiblesSegunOtorgadoPeriodoAnterior
                -retainedCreditsPeriodoAnterior
                -totalCreditosReparticionAjustes_Debito_PeriodoAnterior
                +totalCreditosReparticionAjustes_Credito_PeriodoAnterior
                +totalCreditosReasignadosDeRetencion_PeriodoAnterior;
        
       

        long creditosDisponiblesAlInicioPeriodoAnterior = getCreditosDisponiblesAlInicioPeriodo(previousPeriod.getId(), departmentId);

        return creditosDisponiblesAlInicioPeriodoAnterior + creditosDisponiblesAlInicioPeriodoActual;

    }


    @Override
    public Long getCreditosReparticion_ReasignadosDeRetencion_Periodo(Long creditsPeriodId, Long departmentId) {
        
        DepartmentCreditsEntryQueryFilter departmentCreditsEntryQueryFilter = new DepartmentCreditsEntryQueryFilter();
        departmentCreditsEntryQueryFilter.setCreditsPeriodId(creditsPeriodId);
        departmentCreditsEntryQueryFilter.setDepartmentId(departmentId);
        
        departmentCreditsEntryQueryFilter.addDepartmentCreditsEntryType(DepartmentCreditsEntryType.ReassignedFromRetention);
        departmentCreditsEntryQueryFilter.addCreditsEntryTransactionType(CreditsEntryTransactionType.Credit);
        return getTotalDepartmentCreditEntries(departmentCreditsEntryQueryFilter);
    }


    @Override
    public Long getCreditosReparticionAjustesDebitoPeriodo(Long creditsPeriodId, Long departmentId) {
        
        DepartmentCreditsEntryQueryFilter departmentCreditsEntryQueryFilter = new DepartmentCreditsEntryQueryFilter();
        departmentCreditsEntryQueryFilter.setCreditsPeriodId(creditsPeriodId);
        departmentCreditsEntryQueryFilter.setDepartmentId(departmentId);
        
        departmentCreditsEntryQueryFilter.addDepartmentCreditsEntryType(DepartmentCreditsEntryType.Adjustment);
        departmentCreditsEntryQueryFilter.addCreditsEntryTransactionType(CreditsEntryTransactionType.Debit);
        return getTotalDepartmentCreditEntries(departmentCreditsEntryQueryFilter);
        
    }
    
    @Override
    public Long getCreditosReparticionAjustesCreditoPeriodo(Long creditsPeriodId, Long departmentId) {
        
        DepartmentCreditsEntryQueryFilter departmentCreditsEntryQueryFilter = new DepartmentCreditsEntryQueryFilter();
        departmentCreditsEntryQueryFilter.setCreditsPeriodId(creditsPeriodId);
        departmentCreditsEntryQueryFilter.setDepartmentId(departmentId);
        
        departmentCreditsEntryQueryFilter.addDepartmentCreditsEntryType(DepartmentCreditsEntryType.Adjustment);
        departmentCreditsEntryQueryFilter.addCreditsEntryTransactionType(CreditsEntryTransactionType.Credit);
        return getTotalDepartmentCreditEntries(departmentCreditsEntryQueryFilter);
        
    }



    public CreditsPeriodService getCreditsPeriodService() {
        return creditsPeriodService;
    }


    public void setCreditsPeriodService(CreditsPeriodService creditsPeriodService) {
        this.creditsPeriodService = creditsPeriodService;
    }
    
    public DepartmentService getDepartmentService() {
        return departmentService;
    }


    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public  Long getTotalDepartmentCreditEntries(final DepartmentCreditsEntryQueryFilter departmentCreditsEntryQueryFilter){
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session sess)
                    throws HibernateException, SQLException  {

                Chronometer timer = new Chronometer();

                Long departmentId = departmentCreditsEntryQueryFilter.getDepartmentId();

                if (log.isDebugEnabled()) log.debug("attempting to find DepartmentCreditEntries with id: '" + departmentId + "'");

                List<String> wheres = new ArrayList<String>();
                List<String> paramNames = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();

                StringBuffer queryBuilder = new StringBuffer();
                
                queryBuilder.append("select sum(entry.numberOfCredits) ");
                queryBuilder.append(" from DepartmentCreditsEntryImpl entry ");
                //queryBuilder.append(" INNER JOIN entry.employment employment ");
                //queryBuilder.append(" INNER JOIN employment.subDepartment subDepartment ");
                queryBuilder.append(" INNER JOIN entry.department department ");
                queryBuilder.append(" INNER JOIN entry.creditsPeriod creditsPeriod ");
                
                
                DepartmentCreditsEntryDaoHibImpl.buildWhereClause(departmentCreditsEntryQueryFilter,wheres,paramNames,values);
                
                
                String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                        org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

                String queryWithOrdering = queryWithoutOrdering;
                
                Query queryObject = sess.createQuery(queryWithOrdering);
                
                if (values != null) {
                    for (int i = 0; i < values.size(); i++) {
                        applyNamedParameterToQuery(queryObject, paramNames.get(i), values.get(i), null);
                    }
                }
                
                
                Long totalAmount = (Long) queryObject.uniqueResult();
                
                if (log.isDebugEnabled()) log.debug("successfully retrieved department with id: '" + departmentId + "' in " + timer.printElapsedTime());
                if(totalAmount==null){
                    totalAmount=new Long(0);
                }
                return totalAmount;
            }
        });
    }


}

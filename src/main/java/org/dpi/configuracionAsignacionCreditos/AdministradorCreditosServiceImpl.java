package org.dpi.configuracionAsignacionCreditos;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dpi.agente.CondicionAgente;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.empleo.EmpleoQueryFilter;
import org.dpi.empleo.EstadoEmpleo;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.dpi.movimientoCreditos.MovimientoCreditosDaoHibImpl;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;

public class AdministradorCreditosServiceImpl extends DataAccessHibImplAbstract implements AdministradorCreditosService{

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	Map<String,Integer> creditosPorCargaInicial = new HashMap<String,Integer>();
	
	Map<String,Integer> creditosARestarPorIngreso = new HashMap<String,Integer>();
	
	Map<String,Integer> creditosASumarPorBaja = new HashMap<String,Integer>();
	
	Map<String, Map<String, Integer>> creditosPorAscenso = new HashMap<String,Map<String, Integer>>();
	
	String MAX_CATEGORIA_PARA_PROFESIONALES_SIN_CONSUMO_DE_CREDITOS = "21";
	
	public AdministradorCreditosServiceImpl() {
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

	
	public int getCreditosPorCargaInicial(String codigoCategoria){
		Integer creditos = creditosPorCargaInicial.get(codigoCategoria);
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	

	public int getCreditosPorBaja(String codigoCategoria){
		Integer creditos = creditosASumarPorBaja.get(codigoCategoria);
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	
	
	public int getCreditosPorAscenso(CondicionAgente condicionAgente, String codigoCategoriaActual, String codigoCategoriaNueva){
		Integer creditos = 0;
		if(condicionAgente!=null && condicionAgente == CondicionAgente.Profesional){
			Integer intCodigoCategoria = Integer.parseInt(codigoCategoriaNueva);
			if(intCodigoCategoria<=Integer.parseInt(MAX_CATEGORIA_PARA_PROFESIONALES_SIN_CONSUMO_DE_CREDITOS)){
				creditos=0;
			}else{
				creditos = this.creditosPorAscenso.get(MAX_CATEGORIA_PARA_PROFESIONALES_SIN_CONSUMO_DE_CREDITOS).get(codigoCategoriaNueva);
			}
		}else{
			creditos = this.creditosPorAscenso.get(codigoCategoriaActual).get(codigoCategoriaNueva);	
		}
		
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	
	public int getCreditosPorIngreso(String codigoCategoria){
		Integer creditos = this.creditosARestarPorIngreso.get(codigoCategoria);
		if(creditos==null){
			creditos=0;
		}
		return creditos;
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService#getCreditosDisponibles(long)
	 */
	/*public int getCreditosDisponibles(long reparticionId){
		return 0;
	}*/
	
	
	
	/**
	 * si un agente es dado de baja ya no suma por carga inicial
	 */
	@SuppressWarnings("unchecked")
	@Override
	public 	Long getCreditosPorCargaInicialDeReparticion(final CreditsPeriod creditsPeriod,final long reparticionId){
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {
				
				Chronometer timer = new Chronometer();

				if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with id: '" + reparticionId + "'");

				String queryStr = "select sum(movimiento.cantidadCreditos) " +
						" from MovimientoCreditosImpl movimiento " +
						" INNER JOIN movimiento.empleo empleo " +
						" INNER JOIN empleo.centroSector centroSector " +
						" INNER JOIN centroSector.reparticion reparticion "+
						" INNER JOIN empleo.agente agente "+
						" where reparticion.id='"+reparticionId+"'" +
						" AND movimiento.tipoMovimientoCreditos = '"+TipoMovimientoCreditos.CargaInicialAgenteExistente.name()+"'"+
						" AND movimiento.creditsPeriod.id = '"+creditsPeriod.getId()+"'";
						/*" AND agente.cuil not in "+
							" (select agente2.cuil " +
							" from MovimientoCreditosImpl movimiento2 " +
							" INNER JOIN movimiento2.empleo empleo2 " +
							" INNER JOIN empleo2.centroSector centroSector2 " +
							" INNER JOIN centroSector2.reparticion reparticion2 "+
							" INNER JOIN empleo2.agente agente2 "+
							" where reparticion2.id='"+reparticionId+"'" +
							" AND movimiento2.tipoMovimientoCreditos = '"+TipoMovimientoCreditos.BajaAgente.name()+"')";*/
				
				Query query = sess.createQuery(queryStr);
			
				Long totalAmount = (Long) query.uniqueResult();
				
				
				if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with id: '" + reparticionId + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public 	Long getCreditosPorBajasDeReparticion(final CreditsPeriod creditsPeriod, final long reparticionId){
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {
				
				Chronometer timer = new Chronometer();

				if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with id: '" + reparticionId + "'");

				String queryStr = "select sum(movimiento.cantidadCreditos) " +
						" from MovimientoCreditosImpl movimiento " +
						" INNER JOIN movimiento.empleo empleo " +
						" INNER JOIN empleo.centroSector centroSector " +
						" INNER JOIN centroSector.reparticion reparticion "+
						" where reparticion.id='"+reparticionId+"'" +
						" AND movimiento.tipoMovimientoCreditos = '"+TipoMovimientoCreditos.BajaAgente.name()+"'"+
						" AND movimiento.creditsPeriod.id = '"+creditsPeriod.getId()+"'";
				Query query = sess.createQuery(queryStr);
			
				Long totalAmount = (Long) query.uniqueResult();
				
				
				if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with id: '" + reparticionId + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public 	Long getTotalCreditos(final MovimientoCreditosQueryFilter movimientoCreditosQueryFilter){
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
				throws HibernateException, SQLException  {
				
				Chronometer timer = new Chronometer();
				
				String reparticionId = movimientoCreditosQueryFilter.getEmpleoQueryFilter().getReparticionId();

				if (log.isDebugEnabled()) log.debug("attempting to find Movimientos with id: '" + reparticionId + "'");
				
				String where = " WHERE 1=1 " + MovimientoCreditosDaoHibImpl.buildWhereClause(movimientoCreditosQueryFilter);

				StringBuffer sb = new StringBuffer();
				sb.append("select sum(movimiento.cantidadCreditos) ");
				sb.append(" from MovimientoCreditosImpl movimiento ");
				sb.append(" INNER JOIN movimiento.empleo empleo ");
				sb.append(" INNER JOIN empleo.centroSector centroSector ");
				sb.append(" INNER JOIN centroSector.reparticion reparticion ");
				sb.append(" INNER JOIN movimiento.creditsPeriod creditsPeriod ");
				
				
				sb.append(where);
				
/*						+
						" where reparticion.id='"+reparticionId+"'" +
						" AND " +
						" (movimiento.tipoMovimientoCreditos = '"+TipoMovimientoCreditos.IngresoAgente.name()+"' OR movimiento.tipoMovimientoCreditos = '"+TipoMovimientoCreditos.AscensoAgente.name()+"')";
	*/			
				Query query = sess.createQuery(sb.toString());
			
				Long totalAmount = (Long) query.uniqueResult();
				
				
				if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with id: '" + reparticionId + "' in " + timer.printElapsedTime());
				if(totalAmount==null){
					totalAmount=new Long(0);
				}
				return totalAmount;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getCreditosDisponiblesSegunSolicitado(CreditsPeriod creditsPeriod,final long reparticionId){

		Long creditosDisponiblesAlInicioPeriodo = getCreditosDisponiblesAlInicioPeriodo(creditsPeriod,reparticionId);
		
		Long creditosAcreditadosPorBajasDelPeriodo = getCreditosPorBajasDeReparticion(creditsPeriod,reparticionId);
		
		Long totalPorIngresosOAscensosSegunSolicitado = this.getCreditosPorIngresosOAscensosSolicitados(creditsPeriod, reparticionId);
		
		return creditosDisponiblesAlInicioPeriodo + creditosAcreditadosPorBajasDelPeriodo -totalPorIngresosOAscensosSegunSolicitado;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getCreditosDisponiblesSegunOtorgado(CreditsPeriod creditsPeriod, final long reparticionId){
		
		Long creditosDisponiblesAlInicioPeriodo = getCreditosDisponiblesAlInicioPeriodo(creditsPeriod,reparticionId);
		
		Long creditosAcreditadosPorBajasDelPeriodo = getCreditosPorBajasDeReparticion(creditsPeriod,reparticionId);
		
		Long totalPorIngresosOAscensosSegunOtorgado = this.getCreditosPorIngresosOAscensosOtorgados(creditsPeriod, reparticionId);
		
		return creditosDisponiblesAlInicioPeriodo + creditosAcreditadosPorBajasDelPeriodo - totalPorIngresosOAscensosSegunOtorgado;
		
	}




	@Override
	public Long getCreditosDisponiblesAlInicioDelPeriodo(final CreditsPeriod creditsPeriod,Long reparticionId) {
		Long creditosPorCargaInicialDeReparticion = getCreditosPorCargaInicialDeReparticion(creditsPeriod,reparticionId);
		Long creditosPorBaja = getCreditosPorBajasDeReparticion(creditsPeriod,reparticionId);
		
		return creditosPorCargaInicialDeReparticion+creditosPorBaja;
	}


	@Override
	public Long getCreditosPorIngresosOAscensosSolicitados(	CreditsPeriod creditsPeriod, Long reparticionId) {
		EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setReparticionId(String.valueOf(reparticionId));
		
		//todos los estados
		empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		
		
		
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.setEmpleoQueryFilter(empleoQueryFilter);
		movimientoCreditosQueryFilter.setIdCreditsPeriod(creditsPeriod.getId());
		
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);
		movimientoCreditosQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		movimientoCreditosQueryFilter.addGrantedStatus(GrantedStatus.Otorgado); //Un movimiento Otorgado tambien es solicitado(fue solicitado en algun momento)
		return getTotalCreditos(movimientoCreditosQueryFilter);
	}
	
	@Override
	public Long getCreditosPorIngresosOAscensosOtorgados(
			CreditsPeriod creditsPeriod, Long reparticionId) {
		EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setReparticionId(String.valueOf(reparticionId));
		//todos los estados
		empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.setEmpleoQueryFilter(empleoQueryFilter);
		movimientoCreditosQueryFilter.setIdCreditsPeriod(creditsPeriod.getId());
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);
		movimientoCreditosQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
		return getTotalCreditos(movimientoCreditosQueryFilter);
	}


	@Override
	public Long getCreditosDisponiblesAlInicioPeriodo(
			CreditsPeriod creditsPeriod, Long reparticionId) {
		
		//If period does not have previous period return 0
		if(creditsPeriod.getPreviousCreditsPeriod()==null){
			return 0l;
		}
		
		//get previous period
		CreditsPeriod previousPeriod = creditsPeriod.getPreviousCreditsPeriod();

		Long totalPorCargaInicial = this.getCreditosPorCargaInicialDeReparticion(previousPeriod,reparticionId);
		
		Long totalPorBajas = this.getCreditosPorBajasDeReparticion(previousPeriod,reparticionId);
		
		Long totalcreditosDisponiblesSegunOtorgadoPeriodoActual = this.getCreditosPorIngresosOAscensosOtorgados(previousPeriod,reparticionId);
		
		return totalPorCargaInicial+totalPorBajas-totalcreditosDisponiblesSegunOtorgadoPeriodoActual;
	}


}

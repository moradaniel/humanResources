package org.dpi.empleo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteImpl;
import org.dpi.agente.AgenteService;
import org.dpi.categoria.Categoria;
import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.dpi.movimientoCreditos.MovimientoCreditosImpl;
import org.dpi.movimientoCreditos.MovimientoCreditosService;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;



public class EmploymentCreditsEntriesServiceImpl implements EmploymentCreditsEntriesService
{
	
	private static Logger log = LoggerFactory.getLogger(EmploymentCreditsEntriesServiceImpl.class);

	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "movimientoCreditosService")
	private MovimientoCreditosService movimientoCreditosService;
	
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	@Resource(name = "categoriaService")
	private CategoriaService categoriaService;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	
	@Resource(name = "agenteService")
	private AgenteService agenteService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	
	private ApplicationContext applicationContext;
	
	public EmploymentCreditsEntriesServiceImpl() {

	}

	@Override
	public void ascenderAgente(Empleo currentEmployment, String codigoCategoriaNueva){
		
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to ascender agente:");/*+ movimiento.getId()+
					" Type:"+movimiento.getTipoMovimientoCreditos().name()+
					" Agent name:"+ movimiento.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+movimiento.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		Agente employee = currentEmployment.getAgente();
		List<Long> employeeIds = new ArrayList<Long>();
		employeeIds.add(employee.getId());
		Set<Long>resultEmployeeIds = movimientoCreditosService.haveMovimientosSolicitados(employeeIds, currentEmployment.getCentroSector().getReparticion().getId(), currentCreditsPeriod.getId());
		
		if(resultEmployeeIds.contains(employee.getId())){
			return;
		}

		Categoria categoriaNueva = categoriaService.findByCodigo(codigoCategoriaNueva);
		Empleo newEmployment = new EmpleoImpl();
		newEmployment.setAgente(currentEmployment.getAgente());
		newEmployment.setCategoria(categoriaNueva);
		newEmployment.setCentroSector(currentEmployment.getCentroSector());
		newEmployment.setOccupationalGroup(currentEmployment.getOccupationalGroup());
		newEmployment.setFechaInicio(new Date());
		newEmployment.setEstado(EmploymentStatus.PENDIENTE);
		
		
		//crear un movimiento de tipo ascenso 
		MovimientoCreditosImpl movimientoAscenso = new MovimientoCreditosImpl();
		movimientoAscenso.setTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		int cantidadCreditosPorAscenso = administradorCreditosService.getCreditosPorAscenso(employee.getCondicion(),currentEmployment.getCategoria().getCodigo(),codigoCategoriaNueva);
		movimientoAscenso.setGrantedStatus(GrantedStatus.Solicitado);
		movimientoAscenso.setCreditsPeriod(currentCreditsPeriod);

		
		movimientoAscenso.setCantidadCreditos(cantidadCreditosPorAscenso);
		
		
		//setear empleo a movimiento  y agregar movimiento a empleo
		newEmployment.addMovimientoCreditos(movimientoAscenso);
		
		movimientoAscenso.setEmpleo(newEmployment);

		//set previous employment
		newEmployment.setEmpleoAnterior(currentEmployment);
		
		
		//guardar movimiento y empleo
		employmentService.saveOrUpdate(newEmployment);
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: ascender agente - "+
					" centrosector: "+newEmployment.getCentroSector().toString()+
					" Employee :"+ newEmployment.getAgente().toString()+
					" creditsEntry: "+movimientoAscenso.toString());	
		}
	}
	
	@Override
	public void darDeBaja(Empleo empleo) {
		
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to dar de baja:");/*+ movimiento.getId()+
					" Type:"+movimiento.getTipoMovimientoCreditos().name()+
					" Agent name:"+ movimiento.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+movimiento.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}	
		
		
		//ponerle fecha fin la fecha actual
		empleo.setFechaFin(new Date());
		empleo.setEstado(EmploymentStatus.BAJA);
		
		//crear un movimiento de tipo baja 
		MovimientoCreditosImpl movimientoBaja = new MovimientoCreditosImpl();
		movimientoBaja.setTipoMovimientoCreditos(TipoMovimientoCreditos.BajaAgente);
		int cantidadCreditosPorBaja = administradorCreditosService.getCreditosPorBaja(empleo.getCategoria().getCodigo());

		
		movimientoBaja.setCantidadCreditos(cantidadCreditosPorBaja);
		
		
		//setear empleo a movimiento  y agregar movimiento a empleo
		empleo.addMovimientoCreditos(movimientoBaja);
		
		movimientoBaja.setEmpleo(empleo);
		
		//guardar movimiento y empleo
		employmentService.saveOrUpdate(empleo);
		
	}
	
	
	@Override
	public void ingresarPropuestaAgente(String codigoCategoriaPropuesta,Long centroSectorId) {
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to ingresar propuesta agente:");/*+ movimiento.getId()+
					" Type:"+movimiento.getTipoMovimientoCreditos().name()+
					" Agent name:"+ movimiento.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+movimiento.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		// crear agente nn
		Agente nuevoAgentePropuesto = createPendingAgent();
		
		agenteService.save(nuevoAgentePropuesto);
		
		//crear empleo
		Empleo nuevoEmpleoPropuesto = new EmpleoImpl();
		nuevoEmpleoPropuesto.setAgente(nuevoAgentePropuesto);
		//setear categoria propuesta al empleo
		nuevoEmpleoPropuesto.setCategoria(categoriaService.findByCodigo(codigoCategoriaPropuesta));
		//al empleo ponerlo en estado pendiente
		nuevoEmpleoPropuesto.setEstado(EmploymentStatus.PENDIENTE);
		
		nuevoEmpleoPropuesto.setFechaInicio(new Date());
		
		//buscar centro sector
		CentroSector centroSector = centroSectorService.findById(centroSectorId);
		nuevoEmpleoPropuesto.setCentroSector(centroSector);
		
		//Crear movimiento de ingreso
		MovimientoCreditos movimientoCreditosIngreso = new MovimientoCreditosImpl();
		movimientoCreditosIngreso.setTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);
		movimientoCreditosIngreso.setGrantedStatus(GrantedStatus.Solicitado);
		movimientoCreditosIngreso.setCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod());
		
		movimientoCreditosIngreso.setEmpleo(nuevoEmpleoPropuesto);
		nuevoEmpleoPropuesto.addMovimientoCreditos(movimientoCreditosIngreso);
		
		int creditosPorIngreso = administradorCreditosService.getCreditosPorIngreso(codigoCategoriaPropuesta);
		movimientoCreditosIngreso.setCantidadCreditos(creditosPorIngreso);
		
		employmentService.save(nuevoEmpleoPropuesto);
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: ingresar propuesta agente - "+
					" centrosector: "+centroSector.toString()+
					" Employee :"+ nuevoAgentePropuesto.toString()+
					" creditsEntry: "+movimientoCreditosIngreso.toString());	
		}
		
	}
	
	
	private Agente createPendingAgent(){
		AgenteImpl newAgente = new AgenteImpl();
		newAgente.setApellidoNombre("Ingreso Nuevo Propuesto");
		newAgente.setCuil("");
		return newAgente;
	}
	
	
	
	
	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	@Override
	public List<EmploymentVO> buildEmploymentsVO(List<Empleo> empleosActivos, Long reparticionId,
			Account currenUser) {
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		List<Long> posiblesAgentesIds = new ArrayList<Long>();
		for(Empleo employment:empleosActivos){
			posiblesAgentesIds.add(employment.getAgente().getId());
		}
		
		Set<Long> agentesIds = new HashSet<Long>();
		if(canAccountAscenderAgente(currenUser)){
			agentesIds = movimientoCreditosService.haveMovimientosSolicitados(posiblesAgentesIds, reparticionId, currentCreditsPeriod.getId());
		}
		
		List<EmploymentVO> employmentsVO = new ArrayList<EmploymentVO>();
		for(Empleo employment:empleosActivos){
			EmploymentVO employmentVO = new EmploymentVO();
			employmentVO.setEmployment(employment);
			
			employmentVO.setCanAccountAscenderAgente(canAccountAscenderAgente(currenUser) && !agentesIds.contains(employment.getAgente().getId()));

			employmentsVO.add(employmentVO);
		}

			
		return employmentsVO;
	}
	
	public static boolean canAccountAscenderAgente(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "CREATE")){
			return true;
		}
		return false;
	}
	
	public static boolean canAccountIngresarAgente(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "CREATE")){
			return true;
		}
		return false;
	}

}

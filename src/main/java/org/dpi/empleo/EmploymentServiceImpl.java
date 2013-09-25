package org.dpi.empleo;

import java.util.List;

import javax.annotation.Resource;

import org.dpi.agente.AgenteService;
import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;



public class EmploymentServiceImpl implements EmploymentService
{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final EmploymentDao empleoDao;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	@Resource(name = "categoriaService")
	private CategoriaService categoriaService;
	
	@Resource(name = "agenteService")
	private AgenteService agenteService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	private ApplicationContext applicationContext;
	
	public EmploymentServiceImpl(final EmploymentDao empleoDao) {
		this.empleoDao = empleoDao;
	}

	
	public List<Empleo> find(EmploymentQueryFilter empleoQueryFilter){
		
		return empleoDao.find(empleoQueryFilter);
	}

	public Empleo findById(Long id){
		return empleoDao.findById(id);
	}
	
	public void save(final Empleo empleo) 
	{
		empleoDao.save(empleo);
	}
	
	public void saveOrUpdate(final Empleo empleo) 
	{
		if (empleo.getId() != null) { // it is an update
			 empleoDao.merge(empleo);
			} else { // you are saving a new one
			 empleoDao.saveOrUpdate(empleo);
			}
	}
	
	public void delete(Empleo empleo){
		empleoDao.delete(empleo);
	}

	public EmploymentDao getEmploymentDao()
	{
		return empleoDao;
	}

	public List<Empleo> findAll(){
		return this.empleoDao.findAll();
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	public List<Empleo> findEmpleosInactivos(final EmploymentQueryFilter empleoQueryFilter){
		empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.INACTIVO);
		
		return this.empleoDao.findEmpleosInactivos(empleoQueryFilter);
	}
	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}


	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}


	public void setCategoriaService(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}


	@Override
	public Empleo findPreviousEmpleo(Empleo empleo){
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setCuil(empleo.getAgente().getCuil());
		empleoQueryFilter.setReparticionId(empleo.getCentroSector().getReparticion().getId().toString());
		
		empleoQueryFilter.setEstadosEmpleo( CollectionUtils.arrayToList(EmploymentStatus.values()));
		empleoQueryFilter.setFechaFin(empleo.getFechaInicio());

		return this.empleoDao.findPreviousEmpleo(empleoQueryFilter);
	}


	
	public AgenteService getAgenteService() {
		return agenteService;
	}


	public void setAgenteService(AgenteService agenteService) {
		this.agenteService = agenteService;
	}
	

	
	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}

	
}

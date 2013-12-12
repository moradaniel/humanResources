package utils;

import java.util.List;
import java.util.Map;

import org.dpi.category.Category;
import org.dpi.category.CategoryImpl;
import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorImpl;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryImpl;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.Employment;
import org.dpi.employment.EmploymentImpl;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.employment.EmploymentStatus;
import org.dpi.person.Person;
import org.dpi.person.PersonImpl;
import org.dpi.person.PersonService;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionImpl;
import org.dpi.reparticion.ReparticionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

public class ImportarEntidades {
	
	private static Logger sLog = LoggerFactory.getLogger(ImportarEntidades.class);
	
	CreditsPeriodService creditsPeriodService;
	
	ReparticionService reparticionService;
	
	CentroSectorService centroSectorService;
	
	PersonService personService;
	
	CategoryService categoryService;
	
	EmploymentService employmentService;

	CreditsManagerService creditsManagerService;
	
	NamedParameterJdbcTemplate dpiJdbcTemplate;
	
	TransactionTemplate transactionTemplate;
	
	public static void main( String[] args )
    {
		String[] contextFiles = new String[] {
				"classpath:spring/root-context.xml",
				"classpath:GenericDaoContext.xml",
				"classpath:SecurityContextGeneric.xml"
		};
		
		ApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);

		final ImportarEntidades importadorEntidades = new ImportarEntidades();
		importadorEntidades.setDpiJdbcTemplate((NamedParameterJdbcTemplate)context.getBean("dpiJdbcTemplate"));
		
		importadorEntidades.setCreditsPeriodService((CreditsPeriodService)context.getBean("creditsPeriodService"));
		
		
		importadorEntidades.setReparticionService((ReparticionService)context.getBean("reparticionService"));
		importadorEntidades.setCentroSectorService((CentroSectorService)context.getBean("centroSectorService"));
		importadorEntidades.setAgenteService((PersonService)context.getBean("agenteService"));
		importadorEntidades.setCategoryService((CategoryService)context.getBean("categoriaService"));
		importadorEntidades.setEmploymentService((EmploymentService)context.getBean("employmentService"));
		
		importadorEntidades.setCreditsManagerService((CreditsManagerService)context.getBean("creditsManagerService"));
		
		importadorEntidades.setTransactionTemplate((TransactionTemplate)context.getBean("transactionTemplate"));
		
		
	    importadorEntidades.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status){
				importadorEntidades.procesarImportacion();
				
			}
		});

    }

	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
		
	}

	
	public CreditsManagerService getCreditsManagerService() {
		return this.creditsManagerService;
	}

	
	/**
     * 
     */
	private void procesarImportacion() throws RuntimeException{
		
		String query = "SELECT	* " +
					" FROM	CREDITOS.TODO " +
					" WHERE	(CREDITOS.TODO.ESCALAFON = '6' or CREDITOS.TODO.ESCALAFON = '2')" + //solo escalafon general 02 y 06
					"		AND CREDITOS.TODO.REPARTICION<>'#N/A'";
					//"		AND CREDITOS.TODO.PARA_IMPORTAR='SI' " ;
					//"		AND CREDITOS.TODO.BAJA='BAJA'" ;

		MapSqlParameterSource parameters = new MapSqlParameterSource();

		List<Map<String, Object>> rows = getDpiJdbcTemplate().queryForList(query, parameters);
		
		for (Map<String, Object> row: rows) {
			//importarCategorias(row);
			importarAgentes(row);
			//importarReparticion(row);
			//importarRelacionEntreReparticionyCentroSector(row);
			importarEmpleos(row);
			

		}
		
	}

	private void importarRelacionEntreReparticionyCentroSector(
			Map<String, Object> row) {
		String nombreReparticion = (String)row.get("REPARTICION");
		if(nombreReparticion!=null){
			nombreReparticion = nombreReparticion.trim();
			Reparticion reparticion = reparticionService.findByNombre(nombreReparticion);
			if(reparticion!=null){
				String codigoCentro = (String)row.get("CENTRO");
				String codigoSector = (String)row.get("SECTOR");
				if(codigoCentro!=null && codigoSector!=null){
					
					CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
					if(centroSector==null){
						//si no existe el centroSector todavia, crearlo
						if(codigoCentro!=null && codigoSector!=null){
								centroSector = new CentroSectorImpl();
								String nombreCentro = (String)row.get("NOMBRECENTRO");
								String nombreSector = (String)row.get("NOMBRESECTOR");
								centroSector.setCodigoCentro(codigoCentro);
								centroSector.setCodigoSector(codigoSector);
								centroSector.setNombreCentro(nombreCentro);
								centroSector.setNombreSector(nombreSector);
								sLog.info("Created new centroSector: centro: " + codigoCentro+ 
															" nombreCentro: "+	nombreCentro+
															" sector: "+	codigoSector+ 
															" nombreSector"+nombreSector);
						}
						
					}
					
					reparticion.addCentroSector(centroSector);
					centroSector.setReparticion(reparticion);
					centroSectorService.saveOrUpdate(centroSector);
					reparticionService.saveOrUpdate(reparticion);
					
					sLog.info("Created relation between centro: " + centroSector.getCodigoCentro()+ 
							" nombreCentro: "+	centroSector.getNombreCentro()+
							" sector: "+	centroSector.getCodigoSector()+ 
							" nombreSector"+centroSector.getNombreSector()+
							" and reparticion "+ reparticion.getId()+ ": " +reparticion.getNombre() );

				}
			}
		}

	}

	private void importarReparticion(Map<String, Object> row) {
		String nombreReparticion = (String)row.get("REPARTICION");
		if(nombreReparticion!=null){
			nombreReparticion=nombreReparticion.trim();
			Reparticion theReparticion = null;
			theReparticion = reparticionService.findByNombre(nombreReparticion);
			//si no existe todavia, crearlo
			if(theReparticion==null){
				theReparticion = new ReparticionImpl();
				theReparticion.setNombre(nombreReparticion);
			}
			String codigoReparticion = (String)row.get("CODIGO_REPARTICION");
			if(codigoReparticion!=null && theReparticion.getCode()==null){
				theReparticion.setCode(codigoReparticion);
			}
			
			reparticionService.saveOrUpdate(theReparticion);
			sLog.info("Created new reparticion "+ theReparticion.getId()+ ": " +theReparticion.getNombre() );

		}
	}
	
	
	/*private void importarCentroSectores(Map<String, Object> row) {
		String codigoCentro = (String)row.get("CENTRO");
		String codigoSector = (String)row.get("SECTOR");
		String nombreCentro = (String)row.get("NOMBRECENTRO");
		String nombreSector = (String)row.get("NOMBRESECTOR");
		
		if(codigoCentro!=null && codigoSector!=null){
			CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			//si no existe todavia, crearlo
			if(centroSector==null){
				CentroSectorImpl newCentroSector = new CentroSectorImpl();
				newCentroSector.setCodigoCentro(codigoCentro);
				newCentroSector.setCodigoSector(codigoSector);
				newCentroSector.setNombreCentro(nombreCentro);
				newCentroSector.setNombreSector(nombreSector);
				centroSectorService.saveOrUpdate(newCentroSector);
			}
		}
	}*/

	private void importarAgentes(Map<String, Object> row) {
		String cuil = (String)row.get("CUIL");
		if(cuil!=null){
			Person agente = personService.findByCuil(cuil);
			//si no existe todavia, crearlo
			if(agente==null){
				PersonImpl newAgente = new PersonImpl();
				newAgente.setCuil(cuil);
				String apellidoNombre = (String)row.get("APELLIDONOMBRE");
				newAgente.setApellidoNombre(apellidoNombre);
				personService.saveOrUpdate(newAgente);
			}
		}
	}
	
	private void importarCategorias(Map<String, Object> row) {
		String categoryCode = (String)row.get("CATEGORIA");
		if(categoryCode!=null){
			Category categoria = categoryService.findByCode(categoryCode);
			//si no existe todavia, crearlo
			if(categoria==null){
				CategoryImpl newCategory = new CategoryImpl();
				newCategory.setCode(categoryCode);
				categoryService.saveOrUpdate(newCategory);
				sLog.info("Created new categoria "+ newCategory.getId()+ ": " +newCategory.getCode() );
			}
		}
	}
	
	private void importarEmpleos(Map<String, Object> row) throws RuntimeException{
		String codigoCentro = (String)row.get("CENTRO");
		String codigoSector = (String)row.get("SECTOR");
		
		CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
		creditsPeriodQueryFilter.setName("2012");
		
		List<CreditsPeriod> creditsPeriods = creditsPeriodService.find(creditsPeriodQueryFilter);
		
		CreditsPeriod creditsPeriod = creditsPeriods.get(0); 
		
		if(codigoCentro!=null && codigoSector!=null){
			
			CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			
			String cuil = (String)row.get("CUIL");
			
			System.out.println(cuil);
			
			if(cuil!=null){
				Person person = personService.findByCuil(cuil);
				String categoryCode = (String)row.get("CATEGORIA");
				if(categoryCode!=null){

					Category categoria = categoryService.findByCode(categoryCode);
				
					EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
					empleoQueryFilter.setCuil(cuil);
					empleoQueryFilter.setCodigoCentro(codigoCentro);
					empleoQueryFilter.setCodigoSector(codigoSector);
					empleoQueryFilter.setCategoryCode(categoryCode);
					empleoQueryFilter.addEmploymentStatus(EmploymentStatus.ACTIVO);
					
					
					
					List<Employment> empleos = employmentService.find(empleoQueryFilter);
					
					Employment empleo = null;
					if(CollectionUtils.isEmpty(empleos)){
						//crear una entrada en empleo
						empleo = new EmploymentImpl();
						empleo.setPerson(person);
						empleo.setCentroSector(centroSector);
						empleo.setCategory(categoria);
						empleo.setStatus(EmploymentStatus.ACTIVO);
						
						String esBaja = (String)row.get("BAJA");
						
						if(esBaja.equalsIgnoreCase("ALTA")){ //es Carga Inicial
							CreditsEntryImpl movimientoCargaInicialAgente = new CreditsEntryImpl();
							movimientoCargaInicialAgente.setCreditsEntryType(CreditsEntryType.CargaInicialAgenteExistente);
							movimientoCargaInicialAgente.setGrantedStatus(GrantedStatus.Otorgado);
							movimientoCargaInicialAgente.setCreditsPeriod(creditsPeriod);
							int cantidadCreditosPorCargaInicial = this.creditsManagerService.getCreditosPorCargaInicial(categoryCode);
							
							//empleo.setFechaInicio(creditsPeriod.getStartDate());
							
							movimientoCargaInicialAgente.setCantidadCreditos(cantidadCreditosPorCargaInicial);
							movimientoCargaInicialAgente.setEmployment(empleo);
							
							empleo.setStatus(EmploymentStatus.ACTIVO);
							empleo.addCreditsEntry(movimientoCargaInicialAgente);
							
							employmentService.saveOrUpdate(empleo);
							
						}
						
					}
				
				}			
			}
		}
		
	}
	
	/*
	private void importarEmpleos(Map<String, Object> row) throws RuntimeException{
		String codigoCentro = (String)row.get("CENTRO");
		String codigoSector = (String)row.get("SECTOR");
		
		CreditsPeriod creditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		if(codigoCentro!=null && codigoSector!=null){
			
			CentroSector centroSector = centroSectorService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			
			String cuil = (String)row.get("CUIL");
			
			System.out.println(cuil);
			
			if(cuil!=null){
				Agente agente = agenteService.findByCuil(cuil);
				String codigoCategoria = (String)row.get("CATEGORIA");
				if(codigoCategoria!=null){

					Categoria categoria = categoriaService.findByCodigo(codigoCategoria);
				
					EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
					empleoQueryFilter.setCuil(cuil);
					empleoQueryFilter.setCodigoCentro(codigoCentro);
					empleoQueryFilter.setCodigoSector(codigoSector);
					empleoQueryFilter.setCodigoCategoria(codigoCategoria);
					empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.ACTIVO);
					
					
					
					List<Empleo> empleos = employmentService.find(empleoQueryFilter);
					
					Empleo empleo = null;
					if(CollectionUtils.isEmpty(empleos)){
						//crear una entrada en empleo
						empleo = new EmpleoImpl();
						empleo.setAgente(agente);
						empleo.setCentroSector(centroSector);
						empleo.setCategoria(categoria);
					}else{
						empleo = empleos.get(0);
					}
						
						
						String esBaja = (String)row.get("BAJA");
						
						if(esBaja.equalsIgnoreCase("ALTA")){ //es ingreso nuevo
							CreditsEntryImpl movimientoIngresoAgente = new CreditsEntryImpl();
							movimientoIngresoAgente.setTipoCreditsEntry(TipoCreditsEntry.IngresoAgente);
							movimientoIngresoAgente.setGrantedStatus(GrantedStatus.Otorgado);
							movimientoIngresoAgente.setCreditsPeriod(creditsPeriod);
							
							empleo.setFechaInicio(creditsPeriod.getStartDate());
							
							//int cantidadCreditosPorIngreso = this.administradorCreditosService.getCreditosPorIngreso(codigoCategoria);
							
							movimientoIngresoAgente.setCantidadCreditos(0); //pase a planta de contratados no consume creditos
							movimientoIngresoAgente.setEmpleo(empleo);
							
							empleo.setEstado(EmploymentStatus.ACTIVO);
							empleo.addCreditsEntry(movimientoIngresoAgente);
							
						}else if(esBaja.equalsIgnoreCase("BAJA")){//es baja
							CreditsEntryImpl movimientoBajaAgente = new CreditsEntryImpl();
							movimientoBajaAgente.setTipoCreditsEntry(TipoCreditsEntry.BajaAgente);
							
							int cantidadCreditosPorBaja = this.administradorCreditosService.getCreditosPorBaja(codigoCategoria);
							
							movimientoBajaAgente.setCantidadCreditos(cantidadCreditosPorBaja);
							movimientoBajaAgente.setEmpleo(empleo);
							movimientoBajaAgente.setGrantedStatus(GrantedStatus.Otorgado);
							movimientoBajaAgente.setCreditsPeriod(creditsPeriod);
							
						
							empleo.setFechaFin(creditsPeriod.getStartDate());
							empleo.setEstado(EmploymentStatus.BAJA);
							
							empleo.addCreditsEntry(movimientoBajaAgente);
							
						}
						
						
						employmentService.saveOrUpdate(empleo);
											
				
				}			
			}
		}
		
	}*/
	
	/*
	public AccountDao getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountDao accountService) {
		this.accountService = accountService;
	}
*/
	public NamedParameterJdbcTemplate getDpiJdbcTemplate() {
		return dpiJdbcTemplate;
	}

	public void setDpiJdbcTemplate(NamedParameterJdbcTemplate dpiJdbcTemplate) {
		this.dpiJdbcTemplate = dpiJdbcTemplate;
	}

	
	public ReparticionService getReparticionService() {
		return reparticionService;
	}

	public void setReparticionService(ReparticionService reparticionService) {
		this.reparticionService = reparticionService;
	}
	
	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}

	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public PersonService getAgenteService() {
		return personService;
	}

	public void setAgenteService(PersonService agenteService) {
		this.personService = agenteService;
	}
	
	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoriaService) {
		this.categoryService = categoriaService;
	}
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}

	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

	
	public CreditsPeriodService getCreditsPeriodService() {
		return this.creditsPeriodService;
	}

	public void setCreditsPeriodService(CreditsPeriodService creditsPeriodService) {
		this.creditsPeriodService = creditsPeriodService;
	}

}

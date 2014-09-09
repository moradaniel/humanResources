package utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.dpi.category.Category;
import org.dpi.category.CategoryImpl;
import org.dpi.category.CategoryService;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryImpl;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentImpl;
import org.dpi.department.DepartmentService;
import org.dpi.employment.Employment;
import org.dpi.employment.EmploymentImpl;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.employment.EmploymentStatus;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.occupationalGroup.OccupationalGroupQueryFilter;
import org.dpi.occupationalGroup.OccupationalGroupService;
import org.dpi.person.Person;
import org.dpi.person.PersonImpl;
import org.dpi.person.PersonService;
import org.dpi.subDepartment.SubDepartment;
import org.dpi.subDepartment.SubDepartmentImpl;
import org.dpi.subDepartment.SubDepartmentService;
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

public class EntitiesImporter {
	
	private static Logger sLog = LoggerFactory.getLogger(EntitiesImporter.class);
	
	CreditsPeriodService creditsPeriodService;
	
	DepartmentService departmentService;
	
	SubDepartmentService subDepartmentService;
	
	PersonService personService;
	
	CategoryService categoryService;
	
	OccupationalGroupService occupationalGroupService;
	
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

		final EntitiesImporter importadorEntidades = new EntitiesImporter();
		importadorEntidades.setDpiJdbcTemplate((NamedParameterJdbcTemplate)context.getBean("dpiJdbcTemplate"));
		
		importadorEntidades.setCreditsPeriodService((CreditsPeriodService)context.getBean("creditsPeriodService"));
		
		
		importadorEntidades.setDepartmentService((DepartmentService)context.getBean("departmentService"));
		importadorEntidades.setSubDepartmentService((SubDepartmentService)context.getBean("subDepartmentService"));
		importadorEntidades.setPersonService((PersonService)context.getBean("personService"));
		importadorEntidades.setCategoryService((CategoryService)context.getBean("categoryService"));
		importadorEntidades.setOccupationalGroupService((OccupationalGroupService)context.getBean("occupationalGroupService"));
		
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

	private void setOccupationalGroupService(OccupationalGroupService occupationalGroupService) {
       this.occupationalGroupService = occupationalGroupService;
        
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
					" FROM	CREDITOS.TODO " ;
					//" WHERE	(CREDITOS.TODO.ESCALAFON = '6' or CREDITOS.TODO.ESCALAFON = '2')" + //solo escalafon general 02 y 06
					//"		AND CREDITOS.TODO.REPARTICION<>'#N/A'";
					//"		AND CREDITOS.TODO.PARA_IMPORTAR='SI' " ;
					//"		AND CREDITOS.TODO.BAJA='BAJA'" ;

		MapSqlParameterSource parameters = new MapSqlParameterSource();

		List<Map<String, Object>> rows = getDpiJdbcTemplate().queryForList(query, parameters);
		
		//importSubdepartments();
		
		
		for (Map<String, Object> row: rows) {
			//importarCategorias(row);
			importarAgentes(row);
			//importarReparticion(row);
			//importarRelacionEntreReparticionyCentroSector(row);
			importEmployments(row);
		}
		
	}

	private void importSubdepartments() {
	    String query = "SELECT  * " +
	            " FROM  CREDITOS.NOMSECTOR " ;
	    //" WHERE   (CREDITOS.TODO.ESCALAFON = '6' or CREDITOS.TODO.ESCALAFON = '2')" + //solo escalafon general 02 y 06
	    //"     AND CREDITOS.TODO.REPARTICION<>'#N/A'";
	    //"     AND CREDITOS.TODO.PARA_IMPORTAR='SI' " ;
	    //"     AND CREDITOS.TODO.BAJA='BAJA'" ;

	    MapSqlParameterSource parameters = new MapSqlParameterSource();

	    List<Map<String, Object>> rows = getDpiJdbcTemplate().queryForList(query, parameters);
	    for (Map<String, Object> row: rows) {
	        
	        
            Department department = departmentService.findById(3l);
            if(department!=null){
                String codigoCentro = String.valueOf((BigDecimal)row.get("CENTRO"));
                String codigoSector = String.valueOf((BigDecimal)row.get("SECTOR"));
                String nombreCentro = (String)row.get("NOMCENT");
                String nombreSector = (String)row.get("NOMSECT");
                nombreCentro = nombreCentro.trim();
                if(nombreSector!=null) {
                    nombreSector = nombreSector.trim();
                }else {
                    nombreSector = " ";
                }
                
                if(!nombreCentro.equalsIgnoreCase("MINISTERIO DE EDUCACION")) {
                    continue;
                }
                
                if(codigoCentro!=null && codigoSector!=null){
                    
                    SubDepartment subDepartment = subDepartmentService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
                    if(subDepartment==null){
                        //si no existe el subDepartment todavia, crearlo
                        if(codigoCentro!=null && codigoSector!=null){
                                subDepartment = new SubDepartmentImpl();
                                //String nombreCentro = (String)row.get("NOMCENT");
                                //String nombreSector = (String)row.get("NOMSECT");
                                subDepartment.setCodigoCentro(codigoCentro);
                                subDepartment.setCodigoSector(codigoSector);
                                subDepartment.setNombreCentro(nombreCentro);
                                subDepartment.setNombreSector(nombreSector);
                                sLog.info("Created new subDepartment: centro: " + codigoCentro+ 
                                                            " nombreCentro: "+  nombreCentro+
                                                            " sector: "+    codigoSector+ 
                                                            " nombreSector"+nombreSector);
                        }
                        
                    }
                    
                    department.addSubDepartment(subDepartment);
                    subDepartment.setDepartment(department);
                    subDepartmentService.saveOrUpdate(subDepartment);
                    departmentService.saveOrUpdate(department);
                    
                    sLog.info("Created relation between subDepartment: " + subDepartment.getCodigoCentro()+ 
                            " nombreCentro: "+  subDepartment.getNombreCentro()+
                            " sector: "+    subDepartment.getCodigoSector()+ 
                            " nombreSector"+subDepartment.getNombreSector()+
                            " and department "+ department.getId()+ ": " +department.getName() );

                }


	    }
	    }
	}

    private void importarRelacionEntreReparticionyCentroSector(
			Map<String, Object> row) {
	//	String nombreReparticion = (String)row.get("REPARTICION");
	//	if(nombreReparticion!=null){
		//	nombreReparticion = nombreReparticion.trim();
			Department department = departmentService.findById(3l);
			if(department!=null){
				String codigoCentro = (String)row.get("CODIGOCENTRO");
				String codigoSector = (String)row.get("CODIGOSECTOR");
				if(codigoCentro!=null && codigoSector!=null){
					
				    SubDepartment subDepartment = subDepartmentService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
					if(subDepartment==null){
						//si no existe el subDepartment todavia, crearlo
						if(codigoCentro!=null && codigoSector!=null){
								subDepartment = new SubDepartmentImpl();
								String nombreCentro = (String)row.get("NOMBRECENTRO");
								String nombreSector = (String)row.get("NOMBRESECTOR");
								subDepartment.setCodigoCentro(codigoCentro);
								subDepartment.setCodigoSector(codigoSector);
								subDepartment.setNombreCentro(nombreCentro);
								subDepartment.setNombreSector(nombreSector);
								sLog.info("Created new subDepartment: centro: " + codigoCentro+ 
															" nombreCentro: "+	nombreCentro+
															" sector: "+	codigoSector+ 
															" nombreSector"+nombreSector);
						}
						
					}
					
					department.addSubDepartment(subDepartment);
					subDepartment.setDepartment(department);
					subDepartmentService.saveOrUpdate(subDepartment);
					departmentService.saveOrUpdate(department);
					
					sLog.info("Created relation between subDepartment: " + subDepartment.getCodigoCentro()+ 
							" nombreCentro: "+	subDepartment.getNombreCentro()+
							" sector: "+	subDepartment.getCodigoSector()+ 
							" nombreSector"+subDepartment.getNombreSector()+
							" and department "+ department.getId()+ ": " +department.getName() );

				}
			}
	//	}

	}

	private void importarReparticion(Map<String, Object> row) {
		String nombreReparticion = (String)row.get("REPARTICION");
		if(nombreReparticion!=null){
			nombreReparticion=nombreReparticion.trim();
			Department theReparticion = null;
			theReparticion = departmentService.findByName(nombreReparticion);
			//si no existe todavia, crearlo
			if(theReparticion==null){
				theReparticion = new DepartmentImpl();
				theReparticion.setName(nombreReparticion);
			}
			String codigoReparticion = (String)row.get("CODIGO_REPARTICION");
			if(codigoReparticion!=null && theReparticion.getCode()==null){
				theReparticion.setCode(codigoReparticion);
			}
			
			departmentService.saveOrUpdate(theReparticion);
			sLog.info("Created new reparticion "+ theReparticion.getId()+ ": " +theReparticion.getName() );

		}
	}
	
	
	/*private void importarsubDepartmentes(Map<String, Object> row) {
		String codigoCentro = (String)row.get("CENTRO");
		String codigoSector = (String)row.get("SECTOR");
		String nombreCentro = (String)row.get("NOMBRECENTRO");
		String nombreSector = (String)row.get("NOMBRESECTOR");
		
		if(codigoCentro!=null && codigoSector!=null){
			subDepartment subDepartment = subDepartmentService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			//si no existe todavia, crearlo
			if(subDepartment==null){
				CentroSectorImpl newCentroSector = new CentroSectorImpl();
				newCentroSector.setCodigoCentro(codigoCentro);
				newCentroSector.setCodigoSector(codigoSector);
				newCentroSector.setNombreCentro(nombreCentro);
				newCentroSector.setNombreSector(nombreSector);
				subDepartmentService.saveOrUpdate(newCentroSector);
			}
		}
	}*/

	private void importarAgentes(Map<String, Object> row) {
		String cuil = (String)row.get("CUIL");
		if(cuil!=null){
			Person agente = personService.findByCuil(cuil);
			//si no existe todavia, crearlo
			if(agente==null){
				PersonImpl newPerson = new PersonImpl();
				newPerson.setCuil(cuil);
				String apellidoNombre = (String)row.get("APELLIDONOMBRE");
				newPerson.setApellidoNombre(apellidoNombre);
				personService.saveOrUpdate(newPerson);
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
	
	private void importEmployments(Map<String, Object> row) throws RuntimeException{
		String codigoCentro = (String)row.get("CODIGOCENTRO");
		String codigoSector = (String)row.get("CODIGOSECTOR");
		
		CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
		creditsPeriodQueryFilter.setName("2013");
		
		List<CreditsPeriod> creditsPeriods = creditsPeriodService.find(creditsPeriodQueryFilter);
		
		CreditsPeriod creditsPeriod = creditsPeriods.get(0); 
		
		if(codigoCentro!=null && codigoSector!=null){
			
			SubDepartment subDepartment = subDepartmentService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			
			if(subDepartment!=null) {
			
			String cuil = (String)row.get("CUIL");
			
			System.out.println(cuil);
			
			if(cuil!=null){
				Person person = personService.findByCuil(cuil);
				String categoryCode = (String)row.get("CATEGORY");
                String occupationalGroupCode = (String)row.get("CODIGO_TRAMO");
				if(categoryCode!=null && occupationalGroupCode!=null){

					Category categoria = categoryService.findByCode(categoryCode);
					
					OccupationalGroupQueryFilter occupationalGroupQueryFilter = new OccupationalGroupQueryFilter();
					occupationalGroupQueryFilter.setCode(occupationalGroupCode);
					
					List occList = occupationalGroupService.find(occupationalGroupQueryFilter);
					
					
					OccupationalGroup occupationalGroup = null;
					
					if(occList != null && occList.size()==1) {
					    occupationalGroup = (OccupationalGroup)occList.get(0);
					}else {
					    occupationalGroup = null;
					}
					
					
					EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
					empleoQueryFilter.setCuil(cuil);
					empleoQueryFilter.setCodigoCentro(codigoCentro);
					empleoQueryFilter.setCodigoSector(codigoSector);
					empleoQueryFilter.setCategoryCode(categoryCode);
					empleoQueryFilter.addEmploymentStatus(EmploymentStatus.ACTIVO);
					
					
					
					List<Employment> employments = employmentService.findEmployments(empleoQueryFilter);
					
					Employment employment = null;
					if(CollectionUtils.isEmpty(employments)){
						//crear una entrada en empleo
						employment = new EmploymentImpl();
						employment.setPerson(person);
						employment.setSubDepartment(subDepartment);
						employment.setCategory(categoria);
						employment.setOccupationalGroup(occupationalGroup);
						employment.setStatus(EmploymentStatus.ACTIVO);
						employment.setStartDate(creditsPeriod.getStartDate());
						
						//String esBaja = (String)row.get("BAJA");
						
						//if(esBaja.equalsIgnoreCase("ALTA")){ //es Carga Inicial
							CreditsEntryImpl creditsEntry = new CreditsEntryImpl();
							creditsEntry.setCreditsEntryType(CreditsEntryType.IngresoAgente);
							creditsEntry.setGrantedStatus(GrantedStatus.Otorgado);
							creditsEntry.setCreditsPeriod(creditsPeriod);
							//int cantidadCreditosPorCargaInicial = this.creditsManagerService.getCreditosPorCargaInicial(categoryCode);

							creditsEntry.setNumberOfCredits(0);
							creditsEntry.setEmployment(employment);
							creditsEntry.setNotes("Pase a planta de personal contratado");
							
							employment.addCreditsEntry(creditsEntry);
							
							employmentService.saveOrUpdate(employment);
							
						//}
						
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
			
			CentroSector subDepartment = subDepartmentService.findByCodigoCentroCodigoSector(codigoCentro,codigoSector );
			
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
						empleo.setCentroSector(subDepartment);
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

	
	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	public SubDepartmentService getSubDepartmentService() {
		return subDepartmentService;
	}

	public void setSubDepartmentService(SubDepartmentService subDepartmentService) {
		this.subDepartmentService = subDepartmentService;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
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

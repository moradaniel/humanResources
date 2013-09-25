package org.dpi.empleo;



public class EmploymentVO{
	

	Empleo employment =  new EmpleoImpl();
	
	boolean canAccountAscenderAgente = false;
		
	public EmploymentVO(){
		super();
	}
	
	public Empleo getEmployment() {
		return employment;
	}

	public void setEmployment(Empleo employment) {
		this.employment = employment;
	}

	
	public boolean isCanAccountAscenderAgente() {
		return canAccountAscenderAgente;
	}

	public void setCanAccountAscenderAgente(boolean canAccountAscenderAgente) {
		this.canAccountAscenderAgente = canAccountAscenderAgente;
	}

}

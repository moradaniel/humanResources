package org.dpi.empleo;



public class EmploymentVO{
	

	Empleo employment =  new EmpleoImpl();
	
	boolean canAccountPromotePerson = false;
		
	public EmploymentVO(){
		super();
	}
	
	public Empleo getEmployment() {
		return employment;
	}

	public void setEmployment(Empleo employment) {
		this.employment = employment;
	}

	
	public boolean isCanAccountPromotePerson() {
		return canAccountPromotePerson;
	}

	public void setCanAccountPromotePerson(boolean canAccountPromotePerson) {
		this.canAccountPromotePerson = canAccountPromotePerson;
	}

}

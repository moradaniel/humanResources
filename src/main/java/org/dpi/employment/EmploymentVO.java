package org.dpi.employment;



public class EmploymentVO{
	

	Employment employment =  new EmploymentImpl();
	
	boolean canAccountPromotePerson = false;
		
	public EmploymentVO(){
		super();
	}
	
	public Employment getEmployment() {
		return employment;
	}

	public void setEmployment(Employment employment) {
		this.employment = employment;
	}

	
	public boolean isCanAccountPromotePerson() {
		return canAccountPromotePerson;
	}

	public void setCanAccountPromotePerson(boolean canAccountPromotePerson) {
		this.canAccountPromotePerson = canAccountPromotePerson;
	}

}

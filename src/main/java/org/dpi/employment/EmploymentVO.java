package org.dpi.employment;



public class EmploymentVO{
	

	Employment employment =  new EmploymentImpl();
	
	boolean canAccountPromotePerson = false;
	
	boolean canAccountDeactivatePerson = false;
		
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
	
	public boolean isCanAccountDeactivatePerson() {
		return canAccountDeactivatePerson;
	}

	public void setCanAccountDeactivatePerson(boolean canAccountDeactivatePerson) {
		this.canAccountDeactivatePerson = canAccountDeactivatePerson;
	}	


}

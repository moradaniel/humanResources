package org.dpi.creditsEntry;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;



public class CreditsEntryForm {
	
	private Long creditsEntryId;

	//fields to show in labels
	private String employeeName;
	

	//fields edited by the form
	private GrantedStatus grantedStatus;
	
	
	public CreditsEntryForm() {
	}
	
	public CreditsEntryForm(CreditsEntry creditsEntry) {
		//to retrieve the movimiento from database
		this.creditsEntryId=creditsEntry.getId();
		
		//to show
		this.employeeName=creditsEntry.getEmployment().getPerson().getApellidoNombre();
		
		//to edit
		this.grantedStatus=creditsEntry.getGrantedStatus();
				

	}

	public Long getCreditsEntryId() {
		return creditsEntryId;
	}

	public void setCreditsEntryId(Long creditsEntryId) {
		this.creditsEntryId = creditsEntryId;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public GrantedStatus getGrantedStatus() {
		return grantedStatus;
	}

	public void setGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatus = grantedStatus;
	}
	
}
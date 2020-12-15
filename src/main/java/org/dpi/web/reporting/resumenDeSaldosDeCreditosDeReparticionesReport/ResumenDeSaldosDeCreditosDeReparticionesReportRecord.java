package org.dpi.web.reporting.resumenDeSaldosDeCreditosDeReparticionesReport;

import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.department.Department;

public class ResumenDeSaldosDeCreditosDeReparticionesReportRecord{
	

	CreditsEntry creditsEntry;
	
	Department ministerioDeReparticion;
	
	String currentCategory;

	String proposedCategory;
	
	String occupationalGroup;

	String parentOccupationalGroup;
	
		
	boolean canBeDeleted = false;
	boolean canAccountChangeCreditsEntryStatus = false;
	
	public ResumenDeSaldosDeCreditosDeReparticionesReportRecord(CreditsEntry creditsEntry,Department ministerioDeReparticion){
		this.creditsEntry = creditsEntry;
		this.ministerioDeReparticion = ministerioDeReparticion;
	}
	
	public Department getMinisterioDeReparticion() {
        return ministerioDeReparticion;
    }

    public CreditsEntry getCreditsEntry() {
		return creditsEntry;
	}

	/*public void setCreditsEntry(CreditsEntry creditsEntry) {
		this.creditsEntry = creditsEntry;
	}*/

	public String getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(String currentCategory) {
		this.currentCategory = currentCategory;
	}

	public String getProposedCategory() {
		return proposedCategory;
	}

	public void setProposedCategory(String proposedCategory) {
		this.proposedCategory = proposedCategory;
	}
	
	public boolean isCanBeDeleted() {
		return canBeDeleted;
	}

	public void setCanBeDeleted(boolean canBeDeleted) {
		this.canBeDeleted = canBeDeleted;
	}

	public boolean isCanAccountChangeCreditsEntryStatus() {
		return canAccountChangeCreditsEntryStatus;
	}

	public void setCanAccountChangeCreditsEntryStatus(
			boolean canAccountChangeCreditsEntryStatus) {
		this.canAccountChangeCreditsEntryStatus = canAccountChangeCreditsEntryStatus;
	}

	public String getOccupationalGroup() {
		return occupationalGroup;
	}

	public void setOccupationalGroup(String occupationalGroup) {
		this.occupationalGroup = occupationalGroup;
	}

	public String getParentOccupationalGroup() {
		return parentOccupationalGroup;
	}

	public void setParentOccupationalGroup(String parentOccupationalGroup) {
		this.parentOccupationalGroup = parentOccupationalGroup;
	}

}

package org.dpi.employment;

import java.util.ArrayList;
import java.util.List;





public class EmploymentVO{
	

	Employment employment =  new EmploymentImpl();
	
	List<String> notes = new ArrayList<String>();
	
	boolean canBePromoted = false;
	
	boolean canBeDeactivated = false;
	
	boolean canUndoDeactivation = false;
	
	boolean canPersonBeModified = false;
	
	boolean canPersonBeTransfered = false;

	public EmploymentVO(){
		super();
	}
	
	public Employment getEmployment() {
		return employment;
	}

	public void setEmployment(Employment employment) {
		this.employment = employment;
	}

	
	public boolean isCanBePromoted() {
		return canBePromoted;
	}

	public void setCanBePromoted(boolean canBePromoted) {
		this.canBePromoted = canBePromoted;
	}
	
	public boolean isCanBeDeactivated() {
		return canBeDeactivated;
	}

	public void setCanBeDeactivated(boolean canBeDeactivated) {
		this.canBeDeactivated = canBeDeactivated;
	}	
	
	public boolean isCanUndoDeactivation() {
		return canUndoDeactivation;
	}

	public void setCanUndoDeactivation(boolean canUndoDeactivation) {
		this.canUndoDeactivation = canUndoDeactivation;
	}
		
	public boolean isCanPersonBeModified() {
		return canPersonBeModified;
	}

	public void setCanPersonBeModified(boolean canPersonBeModified) {
		this.canPersonBeModified = canPersonBeModified;
	}
	
	public boolean isCanPersonBeTransfered() {
	     return canPersonBeTransfered;
	}

	public void setCanPersonBeTransfered(boolean canPersonBeTransfered) {
	     this.canPersonBeTransfered = canPersonBeTransfered;
	}
	
	public void addNote(String note){
		this.notes.add(note);
	}
	
	public List<String> getNotes() {
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}



}

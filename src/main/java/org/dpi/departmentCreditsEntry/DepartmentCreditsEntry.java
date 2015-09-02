package org.dpi.departmentCreditsEntry;

import java.io.Serializable;
import java.sql.Date;

import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.department.Department;
import org.dpi.domain.Persistent;
import org.janux.bus.security.Account;


public interface DepartmentCreditsEntry extends Persistent,Serializable{
	
	/*public enum DepartmentCreditsEntryType{
		Assigned
	}*/
    
    public enum GrantedStatus{
        Solicitado, //Requested
        Otorgado //Granted
    }
    
  
	Department getDepartment();
	
	void setDepartment(Department department);

	Department getCreatedByDepartment();
	void setCreatedByDepartment(Department department);
	
	int getNumberOfCredits();

	void setNumberOfCredits(int numberOfCredits);
	
	public DepartmentCreditsEntryType getDepartmentCreditsEntryType();

	public void setDepartmentCreditsEntryType(DepartmentCreditsEntryType departmentCreditsEntryType);
	
	void setNotes(String notes);
	
	String getNotes();

	public CreditsPeriod getCreditsPeriod();
	public void setCreditsPeriod(CreditsPeriod grantedStatus);

	
	public GrantedStatus getGrantedStatus();
	public void setGrantedStatus(GrantedStatus grantedStatus);

    CreditsEntryTransactionType getCreditsEntryTransactionType();

    void setCreditsEntryTransactionType(CreditsEntryTransactionType creditsEntryTransactionType);
	
    public Account getCreatedBy();
    public void setCreatedBy(Account createdBy);
    public Date getCreationDate();
    public void setCreationDate(Date creationDate);
    public Account getModifiedBy();
    public void setModifiedBy(Account modifiedBy);
    public Date getModificationDate();
    public void setModificationDate(Date modificationDate);
}
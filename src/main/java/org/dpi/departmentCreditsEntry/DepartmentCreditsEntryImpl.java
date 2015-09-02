package org.dpi.departmentCreditsEntry;

import java.sql.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.department.Department;
import org.dpi.domain.PersistentAbstract;
import org.janux.bus.security.Account;
import org.janux.util.JanuxToStringStyle;

public class DepartmentCreditsEntryImpl  extends PersistentAbstract implements DepartmentCreditsEntry{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	CreditsEntryTransactionType creditsEntryTransactionType;

    DepartmentCreditsEntryType departmentCreditsEntryType;

	Department department;
	
	Department createdByDepartment;
	
	int numberOfCredits;

	private String notes;

	private CreditsPeriod creditsPeriod;
	
	private GrantedStatus grantedStatus;

	//Audit info TODO implement an auditing framework
    Account createdBy;
    Date creationDate;
    Account modifiedBy;
    Date modificationDate;
    
    
	public DepartmentCreditsEntryImpl() {
		super();
	}
	
	
	@Override
	public Department getDepartment() {
		return this.department;
	}

	@Override
	public void setDepartment(Department employment) {
		this.department=employment;
		
	}
	@Override
	public int getNumberOfCredits() {
		return this.numberOfCredits;
	}

	@Override
	public void setNumberOfCredits(int numberOfCredits) {
		this.numberOfCredits=numberOfCredits;
		
	}
	
	
	public DepartmentCreditsEntryType getDepartmentCreditsEntryType() {
		return departmentCreditsEntryType;
	}

	public void setDepartmentCreditsEntryType(
	        DepartmentCreditsEntryType departmentCreditsEntryType) {
		this.departmentCreditsEntryType = departmentCreditsEntryType;
	}

	@Override
	public void setNotes(String notes) {
		this.notes = notes;
		
	}

	@Override
	public String getNotes() {
		return this.notes;
	}

	@Override
	public CreditsPeriod getCreditsPeriod() {
		return this.creditsPeriod;
	}

	@Override
	public void setCreditsPeriod(CreditsPeriod creditsPeriod) {
		this.creditsPeriod = creditsPeriod;
	}

	
	@Override
	public GrantedStatus getGrantedStatus() {
		return grantedStatus;
	}

	@Override
	public void setGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatus = grantedStatus;
	}

	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("departmentCreditsEntryType", getDepartmentCreditsEntryType().name());
	//	sb.append("grantedStatus", getGrantedStatus().name());
		sb.append("credits", getNumberOfCredits());

		
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfCredits;
		result = prime
				* result
				+ ((departmentCreditsEntryType == null) ? 0 : departmentCreditsEntryType.hashCode());
		result = prime * result
				+ ((creditsPeriod == null) ? 0 : creditsPeriod.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		/*result = prime * result
				+ ((grantedStatus == null) ? 0 : grantedStatus.hashCode());*/
		result = prime * result
				+ ((notes == null) ? 0 : notes.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartmentCreditsEntryImpl other = (DepartmentCreditsEntryImpl) obj;
		if (numberOfCredits != other.numberOfCredits)
			return false;
		if (departmentCreditsEntryType != other.departmentCreditsEntryType)
			return false;
		if (creditsPeriod == null) {
			if (other.creditsPeriod != null)
				return false;
		} else if (!creditsPeriod.equals(other.creditsPeriod))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		/*if (grantedStatus != other.grantedStatus)
			return false;*/
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		return true;
	}


    @Override
    public Department getCreatedByDepartment() {
        return createdByDepartment;
    }


    @Override
    public void setCreatedByDepartment(Department department) {
       this.createdByDepartment = department;
        
    }
    
    @Override
    public CreditsEntryTransactionType getCreditsEntryTransactionType() {
        return creditsEntryTransactionType;
    }

    @Override
    public void setCreditsEntryTransactionType(
            CreditsEntryTransactionType creditsEntryTransactionType) {
        this.creditsEntryTransactionType = creditsEntryTransactionType;
    }
    
    
    public Account getCreatedBy() {
        return createdBy;
    }


    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }


    public Date getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Account getModifiedBy() {
        return modifiedBy;
    }


    public void setModifiedBy(Account modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    public Date getModificationDate() {
        return modificationDate;
    }


    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}

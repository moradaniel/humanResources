package org.dpi.departmentCreditsEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dpi.departmentCreditsEntry.DepartmentCreditsEntry.GrantedStatus;
import org.dpi.util.query.AbstractQueryFilter;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class DepartmentCreditsEntryQueryFilter extends AbstractQueryFilter implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    //EmploymentQueryFilter employmentQueryFilter;

    Long id;
    
    Long departmentId;

    Long creditsPeriodId;
    
    List<String> creditsPeriodNames = new ArrayList<String>();

    List<DepartmentCreditsEntryType> departmentCreditsEntryTypes = new ArrayList<DepartmentCreditsEntryType>();
    
    List<CreditsEntryTransactionType> creditsEntryTransactionTypes = new ArrayList<CreditsEntryTransactionType>();
        

    List<GrantedStatus> grantedStatuses = new ArrayList<GrantedStatus>();

    //Boolean hasCredits = null;


    public DepartmentCreditsEntryQueryFilter(){

    }

    public Long getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Long departmentId) {
      this.departmentId = departmentId;
    }
    
  /*  public EmploymentQueryFilter getEmploymentQueryFilter() {
        return employmentQueryFilter;
    }

    public void setEmploymentQueryFilter(EmploymentQueryFilter employmentQueryFilter) {
        this.employmentQueryFilter = employmentQueryFilter;
    }
*/
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DepartmentCreditsEntryType> getDepartmentCreditsEntryTypes() {
        return departmentCreditsEntryTypes;
    }

    public void setDepartmentCreditsEntryTypes(List<DepartmentCreditsEntryType> departmentCreditsEntryTypes) {
        this.departmentCreditsEntryTypes = departmentCreditsEntryTypes;
    }

    public void addDepartmentCreditsEntryType(DepartmentCreditsEntryType departmentCreditsEntryType) {
        this.departmentCreditsEntryTypes.add(departmentCreditsEntryType);
    }

    public void addCreditsEntryTransactionType(CreditsEntryTransactionType creditsEntryTransactionType) {
        this.creditsEntryTransactionTypes.add(creditsEntryTransactionType);
    }
    
    public List<CreditsEntryTransactionType> getCreditsEntryTransactionTypes() {
        return creditsEntryTransactionTypes;
    }

    public List<GrantedStatus> getGrantedStatuses(){
        return this.grantedStatuses;
    }

    public void setGrantedStatuses(
            List<GrantedStatus> grantedStatuses) {
        this.grantedStatuses = grantedStatuses;
    }

    public void addGrantedStatus(GrantedStatus grantedStatus) {
        this.grantedStatuses.add(grantedStatus);
    }

    public Long getCreditsPeriodId() {
        return creditsPeriodId;
    }

    public void setCreditsPeriodId(Long creditsPeriodId) {
        this.creditsPeriodId = creditsPeriodId;
    }

    /*
    public Boolean isHasCredits() {
        return hasCredits;
    }

    public void setHasCredits(Boolean hasCredits) {
        this.hasCredits = hasCredits;
    }*/
    
    public List<String> getCreditsPeriodNames() {
        return creditsPeriodNames;
    }

    public void setCreditsPeriodNames(List<String> creditsPeriodNames) {
        this.creditsPeriodNames = creditsPeriodNames;
    }
    
    public void addCreditsPeriodName(String creditsPeriodName) {
        this.creditsPeriodNames.add(creditsPeriodName);
    }

    @Override
    public void reset() {
        /*role = null;
	        fromDate = null;
	        toDate = null;
	        activeLoanEncharges = null;
	        consultancyType = null; 
	        posId = null;
	        acId = null;
	        amId = null;*/
    }





}

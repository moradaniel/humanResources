package org.dpi.creditsEntry;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.domain.PersistentAbstract;
import org.dpi.employment.Employment;
import org.janux.util.JanuxToStringStyle;
import org.springframework.util.CollectionUtils;

public class CreditsEntryImpl  extends PersistentAbstract implements CreditsEntry{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CreditsEntryType creditsEntryType;

	Employment employment;
	int numberOfCredits;

	private String notes;

	private CreditsPeriod creditsPeriod;
	
	private GrantedStatus grantedStatus;
	
	protected List<CreditsEntry> subsequentCreditEntries;
	
	public CreditsEntryImpl() {
		super();
	}
	
	
	@Override
	public Employment getEmployment() {
		return this.employment;
	}

	@Override
	public void setEmployment(Employment employment) {
		this.employment=employment;
		
	}
	@Override
	public int getNumberOfCredits() {
		return this.numberOfCredits;
	}

	@Override
	public void setNumberOfCredits(int numberOfCredits) {
		this.numberOfCredits=numberOfCredits;
		
	}
	
	
	public CreditsEntryType getCreditsEntryType() {
		return creditsEntryType;
	}

	public void setCreditsEntryType(
			CreditsEntryType creditsEntryType) {
		this.creditsEntryType = creditsEntryType;
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

	/**
	 * To be able to override this by Unit Tests
     * TODO: can this be removed by using Mocks? 
	 * @param creditsEntryService
	 * @return
	 */
	@Override
	public List<CreditsEntry> getSubsequentCreditsEntries(CreditsEntryService creditsEntryService){
	    return creditsEntryService.findSubsequentEntries(this);
	}
	
	/**
	 * To be able to set subsequentCreditsEntries in unit test
	 * TODO: can this be removed by using Mocks?
	 * @param subsequentCreditsEntries
	 */
	@Override
	public void setSubsequentCreditsEntries(List<CreditsEntry> subsequentCreditsEntries){
	     this.subsequentCreditEntries = subsequentCreditsEntries;
	}
	
	@Override
	public boolean canStatusBeChanged(CreditsEntryService creditsEntryService, CreditsPeriodService creditsPeriodService) {
        
        if(this.getCreditsPeriod().getStatus()==CreditsPeriod.Status.Closed){
            
            //if the creditsPeriod is closed
            
            //Not changeable if: 1) older than 1 year 
            if( this.getCreditsPeriod().isOlderThanOtherPeriodInYears(creditsPeriodService.getCurrentCreditsPeriod(),1)
                //or 2) the credits entry is a BajaAgente
                //if we delete a past BajaAgente the balance for the next period can become negative
                 ||
                 (this.getCreditsEntryType()== CreditsEntryType.BajaAgente)
                 ||
                 //or 3) the period is closed then check if the employment has subsequent entries
                 // subsequent entries have to be undone before changing the status
                 (this.hasSubsequentEntries(creditsEntryService)) 
                 ) {
                return false;
            }
               
            return true;
        }else { 
            //CreditsPeriod is Active

                 if(this.getCreditsEntryType()== CreditsEntryType.CargaInicialAgenteExistente){
                     return false;
                 }else
                 if(this.isIngresoWithoutCredits()){
                     return false;
                 }
                 return true;
            }
    }
	
	public boolean hasSubsequentEntries(CreditsEntryService creditsEntryService) {
	    return !CollectionUtils.isEmpty(this.getSubsequentCreditsEntries(creditsEntryService));
	}
	
	
    
    @Override
    public boolean isIngresoWithoutCredits() {
        return  this.getCreditsEntryType()== CreditsEntryType.IngresoAgente
                && 
                this.getNumberOfCredits() == 0;
    }
    

	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("creditsEntryType", getCreditsEntryType().name());
		sb.append("grantedStatus", getGrantedStatus().name());
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
				+ ((creditsEntryType == null) ? 0 : creditsEntryType.hashCode());
		result = prime * result
				+ ((creditsPeriod == null) ? 0 : creditsPeriod.hashCode());
		result = prime * result
				+ ((employment == null) ? 0 : employment.hashCode());
		result = prime * result
				+ ((grantedStatus == null) ? 0 : grantedStatus.hashCode());
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
		CreditsEntryImpl other = (CreditsEntryImpl) obj;
		if (numberOfCredits != other.numberOfCredits)
			return false;
		if (creditsEntryType != other.creditsEntryType)
			return false;
		if (creditsPeriod == null) {
			if (other.creditsPeriod != null)
				return false;
		} else if (!creditsPeriod.equals(other.creditsPeriod))
			return false;
		if (employment == null) {
			if (other.employment != null)
				return false;
		} else if (!employment.equals(other.employment))
			return false;
		if (grantedStatus != other.grantedStatus)
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		return true;
	}
}

package org.dpi.creditsEntry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.PersistentAbstract;
import org.dpi.employment.Employment;
import org.janux.util.JanuxToStringStyle;

public class CreditsEntryImpl  extends PersistentAbstract implements CreditsEntry{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CreditsEntryType creditsEntryType;

	Employment employment;
	int cantidadCreditos;

	private String observaciones;

	private CreditsPeriod creditsPeriod;
	
	private GrantedStatus grantedStatus;
	
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
	public int getCantidadCreditos() {
		return this.cantidadCreditos;
	}

	@Override
	public void setCantidadCreditos(int cantidadCreditos) {
		this.cantidadCreditos=cantidadCreditos;
		
	}
	
	
	public CreditsEntryType getCreditsEntryType() {
		return creditsEntryType;
	}

	public void setCreditsEntryType(
			CreditsEntryType creditsEntryType) {
		this.creditsEntryType = creditsEntryType;
	}

	@Override
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
		
	}

	@Override
	public String getObservaciones() {
		return this.observaciones;
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
		
		sb.append("creditsEntryType", getCreditsEntryType().name());
		sb.append("grantedStatus", getGrantedStatus().name());
		sb.append("credits", getCantidadCreditos());

		
		return sb.toString();
	}
	
}

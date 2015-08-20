package org.dpi.employment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.subDepartment.SubDepartment;



public class EmploymentTransferImpl  /*extends PersistentAbstract*/ implements EmploymentTransfer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Employment employmentToTransfer;
	SubDepartment destinationSubDepartment;
	
	

	@Override
	public Employment getEmploymentToTransfer() {
		return this.employmentToTransfer;
	}

	@Override
	public void setEmploymentToTransfer(Employment employmentToTransfer) {
		this.employmentToTransfer=employmentToTransfer;
		
	}
	
	@Override
	public SubDepartment getDestination() {
		return this.destinationSubDepartment;
	}

	@Override
	public void setDestination(SubDepartment subDepartment) {
		this.destinationSubDepartment=subDepartment;
		
	}
	
	
	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this);
		
		sb.append(super.toString());
		
		sb.append("employmentToTransfer", getEmploymentToTransfer());
		sb.append("destination", getDestination());
				
		return sb.toString();
	}

}

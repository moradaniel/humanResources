package org.dpi.employment;

import java.io.Serializable;

import org.dpi.subDepartment.SubDepartment;


public interface EmploymentTransfer extends /*Persistent,*/Serializable{
	

	public Employment getEmploymentToTransfer();

	public void setEmploymentToTransfer(Employment employmentToTransfer);

	public SubDepartment getDestination();

	public void setDestination(SubDepartment subDepartment);
	
}
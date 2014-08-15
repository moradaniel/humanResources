package org.dpi.department;

import java.io.Serializable;
import java.util.Set;

import org.dpi.domain.Persistent;
import org.dpi.subDepartment.SubDepartment;

public interface Department extends Persistent, Serializable{
	
	public String getCode();
	public void setCode(String code);
	public String getName();
	public void setName(String name);
	public Set<SubDepartment> getSubDepartments();
	public void setSubDepartments(Set<SubDepartment> subDepartments);
	public void addSubDepartment(SubDepartment subDepartment);

}

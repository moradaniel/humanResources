package org.dpi.department;

import java.io.Serializable;
import java.util.Set;

import org.dpi.creditsPeriod.CreditsPeriod;
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

    public Department getParent();

    public void setParent(Department parent);

    public Set<Department> getChildren();

    public void setChildren(Set<Department> children);
    
    public CreditsPeriod getValidFromPeriod();

    public void setValidFromPeriod(CreditsPeriod validFromPeriod);

    public CreditsPeriod getValidToPeriod();

    public void setValidToPeriod(CreditsPeriod validToPeriod);
}

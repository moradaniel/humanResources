package org.dpi.department;

import java.util.HashSet;
import java.util.Set;

import org.dpi.domain.PersistentAbstract;
import org.dpi.subDepartment.SubDepartment;

public class DepartmentImpl extends PersistentAbstract implements Department {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	
	private Set<SubDepartment> subDepartments = new HashSet<SubDepartment>();

	public DepartmentImpl(){
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Set<SubDepartment> getSubDepartments() {
		return subDepartments;
	}

	@Override
	public void setSubDepartments(Set<SubDepartment> subDepartments) {
		this.subDepartments = subDepartments;
		
	}

	@Override
	public void addSubDepartment(SubDepartment subDepartment) {
		this.subDepartments.add(subDepartment);
	}
}

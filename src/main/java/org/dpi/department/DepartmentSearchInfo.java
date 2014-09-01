package org.dpi.department;


public class DepartmentSearchInfo {

	Long departmentId;
	String departmentName;
    String departmentCode;


    /**
	 * @return the departmentId
	 */
	public Long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId
	 *            the departmentId to set
	 */
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}



	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName
	 *            the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	   public String getDepartmentCode() {
	        return departmentCode;
	    }

	    public void setDepartmentCode(String departmentCode) {
	        this.departmentCode = departmentCode;
	    }

}

package org.dpi.department;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.logging.LoggingConstants;


public class DepartmentAdminInfo implements Serializable
{
	private static final long serialVersionUID = 6837102037351198611L;

	private Long id = null;

	private String name = null;

	private Set<String> users = null;

	
	public DepartmentAdminInfo()
	{}
	
	public DepartmentAdminInfo(Department department)
	{
		this.id = department.getId();
		this.name = department.getName();
	}


	public DepartmentAdminInfo(Long id,String name)
	{
		this.id = id;
		this.name = name;
	}

	/**
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DepartmentAdminInfo)) 
		{
		   return false;
		}
		  
		if (this == obj) 
		{
		     return true;
		}
		   
		final DepartmentAdminInfo thatInfo = (DepartmentAdminInfo) obj;
		   
		return new EqualsBuilder()
		                // .append(this.getCode(), thatInfo.getCode())
		                 .append(this.getName(), thatInfo.getName())
		                 .isEquals();
	}
	
	
	public int hashCode()
	{
	     return new HashCodeBuilder(17, 37).
	       append(this.getId()).
	       append(this.getName()).
	       toHashCode();
	}

	public static class NameComparator implements Comparator<DepartmentAdminInfo>, Serializable
	{
		private static final long serialVersionUID = -1203575935957588349L;

		public int compare(DepartmentAdminInfo info1, DepartmentAdminInfo info2)
		{
			int comp = 0;
			
			/*if (!info1.code.equals(info2.code))
			{*/
				comp = info1.name.compareTo(info2.name);
				
				/*if (comp == 0)
				{
					comp = info1.code.compareTo(info2.code);
				}
			}*/

			return comp;
		}
		
	}

	/*public static class CodeComparator implements Comparator<ReparticionAdminInfo>, Serializable
	{
		private static final long serialVersionUID = 8069477633087743248L;

		public int compare(ReparticionAdminInfo info1, ReparticionAdminInfo info2)
		{
			return info1.code.compareTo(info2.code); // assumes no two should have same code
		}
	}*/

	public static NameComparator nameComparator = new NameComparator();
	//public static CodeComparator codeComparator = new CodeComparator();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, LoggingConstants.STYLE_AUDIT);

		if (this.getId() != null) 
			sb.append("id", this.getId());
		
		/*if (this.getChainCode() != null) 
			sb.append("chainCode", this.getChainCode());*/
		
		if (this.getName() != null) 
			sb.append("name", this.getName());
		/*
		if (this.getStatus() != null) 
			sb.append("status", this.getStatus());
		 */
		if (this.getUsers() != null) 
			sb.append("users", this.getUsers());
		/*
		if (this.getGroupCode() != null) 
			sb.append("groupCode", this.getGroupCode());*/
		
		return sb.toString();
	}


}

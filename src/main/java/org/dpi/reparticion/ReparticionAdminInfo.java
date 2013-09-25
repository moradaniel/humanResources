package org.dpi.reparticion;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.logging.LoggingConstants;


public class ReparticionAdminInfo implements Serializable
{
	private static final long serialVersionUID = 6837102037351198611L;

	private Long id = null;

	private String nombre = null;

	private Set<String> users = null;

	
	public ReparticionAdminInfo()
	{}
	
	public ReparticionAdminInfo(Reparticion reparticion)
	{
		this.id = reparticion.getId();
		this.nombre = reparticion.getNombre();//.getShort(); // TODO: Really should make sure this exists!
	}


	public ReparticionAdminInfo(Long id,String name)
	{
		this.id = id;
		this.nombre = name;
	}

	/**
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ReparticionAdminInfo)) 
		{
		   return false;
		}
		  
		if (this == obj) 
		{
		     return true;
		}
		   
		final ReparticionAdminInfo thatInfo = (ReparticionAdminInfo) obj;
		   
		return new EqualsBuilder()
		                // .append(this.getCode(), thatInfo.getCode())
		                 .append(this.getNombre(), thatInfo.getNombre())
		                 .isEquals();
	}
	
	
	public int hashCode()
	{
	     return new HashCodeBuilder(17, 37).
	       append(this.getId()).
	       append(this.getNombre()).
	       toHashCode();
	}

	public static class NameComparator implements Comparator<ReparticionAdminInfo>, Serializable
	{
		private static final long serialVersionUID = -1203575935957588349L;

		public int compare(ReparticionAdminInfo info1, ReparticionAdminInfo info2)
		{
			int comp = 0;
			
			/*if (!info1.code.equals(info2.code))
			{*/
				comp = info1.nombre.compareTo(info2.nombre);
				
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String name) {
		this.nombre = name;
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
		
		if (this.getNombre() != null) 
			sb.append("name", this.getNombre());
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

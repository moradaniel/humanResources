package org.dpi.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.janux.util.JanuxToStringStyle;

/**
 ***************************************************************************************************
 * Utility class that provides an Integer identity field
 * 	
 * @author  <a href="mailto:philippe.paravicini@janux.org">Philippe Paravicini</a>
 ***************************************************************************************************
 */
public abstract class PersistentAbstract implements Persistent
{
	private Long id = new Long(UNSAVED_ID);

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append("id", getId());
		
		return sb.toString();
	}
	
} // end class PersistentAbstract

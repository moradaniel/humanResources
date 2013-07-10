package org.dpi.domain;

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
} // end class PersistentAbstract

package org.dpi.domain;

/**
 ***************************************************************************************************
 * Defines an integer identifier that can be used to denote persistent classes
 * 	
 * @author  <a href="mailto:philippe.paravicini@janux.org">Philippe Paravicini</a>
 * @version $Revision: 1.3 $ - $Date: 2006-11-14 01:14:01 $
 ***************************************************************************************************
 */
public interface Persistent
{
	public static final int UNSAVED_ID = -1;
	Long getId();
	void setId(Long id);
}

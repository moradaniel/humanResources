package org.janux.bus.persistence;

import java.io.Serializable;

import org.springframework.dao.DataAccessException;

/**
 ***************************************************************************************************
 * Suggested interface for a class used to retrieve and save persistent objects from a persistance
 * storage; this interface has been heavily influenced by the Hibernate interfaces, and was authored
 * without the benefit of studying existing standards, such as JSRs, or other persistance tools,
 * such as TopLink or Apache's ORM Bridge.
 *
 * 
 * @author  <a href="mailto:philippe.paravicini@janux.org">Philippe Paravicini</a>
 * @since 0.1 - 2005-11-17
 ***************************************************************************************************
 */
public interface DataAccessObject
{
	/** 
	 * Updates an already existing object, throws an exception if the object has not yet been saved
	 */
	void update(Object persistentObject) throws DataAccessException;
	
	/** Saves an object into persistent storage */
	Serializable save(Object persistentObject) throws DataAccessException;

	/** 
	 * Used when it is desireable to save objects, whether or not they have been saved before or not;
	 * this method will first check whether the object already exists in storage, and will than call
	 * Save or Update, accordingly
	 */
	void saveOrUpdate(Object persistentObject) throws DataAccessException;

	/** deletes an object from persistent storage */
	void delete(Object persistentObject) throws DataAccessException;

	/** Retrieves an object from persistent storage and refreshes its state in memory */
	void refresh(final Object persistentObject) throws DataAccessException;

	/** 
	 * Copied from the Hibernate docs: Copy the state of the given object onto the persistent object
	 * with the same identifier. If there is no persistent instance currently associated with the
	 * session, it will be loaded. Return the persistent instance. If the given instance is unsaved,
	 * save a copy of and return it as a newly persistent instance. The given instance does not become
	 * associated with the session. The semantics of this method are defined by JSR-220.
	 */
	Object merge(final Object persistentObject);
	
}

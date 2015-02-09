package org.dpi.occupationalGroup;

import java.io.Serializable;

import org.dpi.util.query.AbstractQueryFilter;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class OccupationalGroupQueryFilter extends AbstractQueryFilter implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	Long id;
	
	String code;
	
	boolean onlyLeafsOccupationalGroups = false;


    public OccupationalGroupQueryFilter(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}
	

    public boolean isOnlyLeafsOccupationalGroups() {
        return onlyLeafsOccupationalGroups;
    }

    public void setOnlyLeafsOccupationalGroups(boolean onlyLeafsOccupationalGroups) {
        this.onlyLeafsOccupationalGroups = onlyLeafsOccupationalGroups;
    }
    
    @Override
    public void reset() {
        /*role = null;
            fromDate = null;
            toDate = null;
            activeLoanEncharges = null;
            consultancyType = null; 
            posId = null;
            acId = null;
            amId = null;*/
    }
    
}

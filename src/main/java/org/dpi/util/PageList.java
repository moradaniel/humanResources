package org.dpi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 *
 * @param <T>
 */
public class PageList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;
	
	private Long totalPageCount;
	
	private Long currentPageNumber;
	
	private Long totalItems;
	
	private Integer pageItems; 
	
	public PageList() {
		
	}
    
	public PageList(Collection<? extends T> collection) {
		this.addAll(collection);
		this.totalItems = (long) collection.size();
	}

    public PageList(Collection<? extends T> collection, Long totalItems) {
        super(collection);
        this.totalItems = totalItems;
    }

    public Long getTotalPageCount() {
    	return this.totalPageCount;
    }
	
    public  Long getCurrentPageNumber() {
    	return this.currentPageNumber;
    }
    
    public Long getTotalItems() {
    	return this.totalItems;
    }
    
    public Integer getPageItems() {
    	return this.pageItems;
    }
    
	public void setCurrentPageNumber(Long currentPage) {
		this.currentPageNumber = currentPage;
	}
	
	public void setPageItems(Integer pageItems) {
		this.pageItems = pageItems;
	}
	
	public void setTotalItems(Long totalItems) {
		this.totalItems = totalItems;
	}
	
	public void setTotalPageCount(Long totalPageCount) {
		this.totalPageCount = totalPageCount;

	}
    
}

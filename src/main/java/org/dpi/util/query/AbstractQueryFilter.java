package org.dpi.util.query;

import org.dpi.util.query.QueryBind.OrderDirection;

public abstract class AbstractQueryFilter {

	
    /** The default size of the page for pagination */
	private int pageSize = 10;
	
	/** The number of the first item to get (e.g. starts from element 20) */
	Integer startIndex = 0;

	/** The total number of items to get */
	Integer maxResults = 1;

	/** The field to order by */
	String orderBy = null;

	/** The order type (ascending or descending) */
	OrderDirection orderDirection = OrderDirection.DESCENDING;

	/**
	 * 
	 * @param startIndex
	 *            the index of the first item to be retrieved
	 * @param maxResults
	 *            the number of items to be retrieved
	 * @param orderBy
	 *            the "order by" expression or null if there is no sorting
	 * @param desc
	 *            whether to sort descending (true) or ascending (false)
	 */
	public void setFilterContext(Integer startIndex, Integer maxResults, String orderBy, boolean desc) {
		this.startIndex = startIndex;
		this.maxResults = maxResults;		
		this.orderBy = orderBy;
		orderDirection = desc ? OrderDirection.DESCENDING : OrderDirection.ASCENDING;
	}
	
	
    /**
     * sets the page of data requested
     * @param pageIndex the index of the page (the 1st page's index is 0)
     */
    public void setPage(int pageIndex) {
    	setRange(pageIndex * pageSize, ((pageIndex + 1) * pageSize) - 1);
    }
    
    public void setPageSize(int pageSize) {
    	this.pageSize = pageSize;
    }
    
    public void setRange(int first, int last) {
        this.startIndex = new Integer(first);
        this.maxResults = new Integer(last - first + 1);
    }

	/**
	 * @return the index of the first item to be retrieved
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the number of items to be retrieved
	 */
	public Integer getMaxResults() {
		return maxResults;
	}

	/**
	 * @return the "order by" expression or null if there is no sorting
	 */
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the order direction
	 */
	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	public abstract void reset();

}

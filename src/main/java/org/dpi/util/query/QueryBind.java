package org.dpi.util.query;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.dpi.util.BaseObject;
import org.dpi.util.enums.Order;
import org.dpi.util.locale.DateFormatter;
import org.hibernate.criterion.DetachedCriteria;



/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *  
 * 
 *
 */
public class QueryBind extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Possible items ordering by field.
     * 
     * 
     *
     */
    public enum OrderDirection {

    	ASCENDING,
    	
    	DESCENDING
    }
      
    
    /** The list of fields to order by */
    private LinkedList<String> orderingFields;
    
    /** The list of order types (ascending or descending) */
    private LinkedList<OrderDirection> orderingTypes;
    
    /** The number of the first item to get (e.g. starts from element 20) */
    private Integer firstElement;
    
    /** The total number of items to get */
    private Integer countElement;
    
    /** Start date filter */
    private Date startDate;
    
    /** The field to filter for startDate */
    private DateField startDateField;
    
    /** End date filter */
    private Date endDate;
    
    /** The field to filter for endDate */
    private DateField endDateField;
    
    /** Used to keep the time filtering order (before/after the date or between 2 dates) */
    private Order timeOrder; 
        
    /** The size of the page for pagination */
	private int pageSize;
	    
    /**
     * FIXME M: We should provide just argument constructors, empty query binds have no
     * possible use and all queries accepting a QueryBind should support null QueryBind
     */
    public QueryBind() {
        super();
        orderingFields = new LinkedList<String>();
        orderingTypes = new LinkedList<OrderDirection>();
    }
    
        
    /**
     * creates a QueryBind with the specified pageSize
     */
    public QueryBind(int pageSize) {
        this();
        this.pageSize = pageSize;
		setPage(0);
    }

    /**
     * Set the ordering field and types (the sorting elements) from an existing Query Bind
     * 
     * @param q
     */
    public void setSorting(QueryBind q) {
		orderingFields = q.orderingFields;
		orderingTypes = q.orderingTypes;
    }
    
    /**
     * sets the page of data requested
     * @param pageIndex the index of the page (the 1st page's index is 0)
     */
    public void setPage(int pageIndex) {
    	setRange(pageIndex * pageSize, ((pageIndex + 1) * pageSize) - 1);
    }

    
    public void addOrdering(String field, OrderDirection type) {
    
        // If the field ordering is already present susbsitute the old ordering
        // with the new, otherwise just add a further ordering
        if (this.orderingFields.contains(field)) {
            
            this.orderingFields.add(this.orderingFields.indexOf(field), field);
            this.orderingTypes.add(this.orderingFields.indexOf(field), type);
            
        } else {
            
            this.orderingFields.add(field);
            this.orderingTypes.add(type);
            
        }
    }
    
    public OrderDirection getOrderingFieldType(String field) {
        // Get the asc/desc order type for the field argument passed 
        return this.orderingTypes.get(this.getOrderingFields().indexOf(field));
        
    }
    
    public void removeOrdering(String field, OrderDirection type) {
        this.orderingFields.add(field);
        this.orderingTypes.add(type);
    }

    public void setRange(int first, int last) {
        this.firstElement = new Integer(first);
        this.countElement = new Integer(last - first + 1);
    }

    /**
     * @return Returns the lastElement.
     */
    public Integer getCountElement() {
        return this.countElement;
    }
    
    public int getCountToLastElement() {
    	if (this.countElement == null) {
    		return Integer.MAX_VALUE;
    	} else {
    		return this.countElement.intValue();
    	}
    }

    /**
     * @return Returns the firstElement.
     */
    public int getFirstElement() {
    	if (this.firstElement == null) {
    		return 0;
    	} else {
    		return this.firstElement.intValue();
    	}
    }

    /**
     * @return Returns the orderingFields.
     */
    public LinkedList getOrderingFields() {
        return this.orderingFields;
    }
    
    /**
     * Build a string representing the hql order by clause needed to search in the way that 
     * this QueryBind describes.
     * 
     * @param alias The alias used in the HQL, just needed if the order columns are ambigous for the HQL query.
     * @return The order by String that can be used in a HQL query
     * NOTE: Remember that we need to add the maximum and first count of the HQL query.
     */
    public String buildHQLOrderBy(String alias) {
    	if (orderingFields == null || orderingFields.size() == 0){
    		return "";
    	}
    	String retValue = " ORDER BY ";
    	for (String fieldString : orderingFields) {
    	
    		retValue += ((alias!=null && alias.trim().length()!=0)?alias+".":"")+fieldString;
    	
    	
			if (this.getOrderingFieldType(fieldString).equals(OrderDirection.ASCENDING)){
				retValue += " asc"; 
			} else {
				retValue += " desc";
			}
			retValue += ", ";
		}
    	// Just remove the last comma
    	if (retValue.endsWith(", ")){
    		retValue = retValue.substring(0,retValue.length()-2);
    	}
    	return retValue;
    }
    
    public String buildHQLOrderBy() {
    	return buildHQLOrderBy(null);
    }
    

    
    /**
     * @param searchedClazz The type of object we are searching
     * @return a DetachedCriteria with all the order fields gived in order to search directly
     */
    public DetachedCriteria buildCriteriaOrderBy(Class searchedClazz) {
		DetachedCriteria criteria = DetachedCriteria.forClass(searchedClazz);
        
        for (Iterator iter = this.getOrderingFields().iterator(); iter.hasNext();) {
            
            String orderingField = (String) iter.next();
            
            if (this.getOrderingFieldType(orderingField) == OrderDirection.ASCENDING) 
                criteria.addOrder(org.hibernate.criterion.Order.asc(orderingField));                
             else  
                 criteria.addOrder(org.hibernate.criterion.Order.desc(orderingField)); 
        }
        
        return criteria;
    }

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		if (this.timeOrder != Order.BETWEEN) {
			return this.startDate;
		} else {			
			return endDate;		
		}
	}
	
	public void before(DateField dateField, Date date) {
		this.startDateField = dateField;
		this.startDate = date;
		this.timeOrder = Order.MINOR_THAN;
	}
	
	public void after(DateField dateField, Date date) {
		this.startDateField = dateField;
		this.startDate = date;
		this.timeOrder = Order.GREATER_THAN;
	}
	
	/**
	 * @param dateFrom
	 * @param dateTo
	 */
	public void between(DateField fromDateField, Date dateFrom, DateField toDateField, Date dateTo) {
		this.startDateField = fromDateField;
		this.endDateField = toDateField;
		this.startDate = dateFrom;
		this.endDate = dateTo;
		this.timeOrder = Order.BETWEEN;
	}


	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the timeOrder
	 */
	public Order getTimeOrder() {
		return timeOrder;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getStartDate()).toHashCode();
	}
	

	public String buildHQLTimeConstraint() {
		return buildHQLTimeConstraint("");
	}
    /**
     * @return
     */
    public String buildHQLTimeConstraint(String alias) {
    	if (alias == null){
    		alias = ""; // We avoid null pionters
    	}
    	if (alias.trim().length() >0 ){
    		alias += "."; // We prepare the necessary dot
    	}
    	
    	if (this.timeOrder != null) {
    		
	    	if (this.timeOrder.equals(Order.BETWEEN)) {
	    		
	    		return alias+this.startDateField.getFieldName() + " >= '" + 
	    		DateFormatter.HQLDateFormat(this.startDate) + "' AND " +
	    		alias + this.endDateField.getFieldName() + " <= '" + 
	    		DateFormatter.HQLDateFormat(this.endDate) + "'";
	    		
	    	} else if (this.timeOrder.equals(Order.GREATER_THAN)) {
	    		
	    		return alias+this.startDateField.getFieldName() + " >= '" + DateFormatter.HQLDateFormat(this.startDate) + "'";
	    		 
	    	} else if (this.timeOrder.equals(Order.MINOR_THAN)) {
	    		
	    		return alias+this.startDateField.getFieldName() + " <= '" + DateFormatter.HQLDateFormat(this.startDate) + "'";
	    		
	    	}
	    	
    	} 
  		return "";
    }
    
    /**
     * @return <code>true<code> if the QUeryBind contains some date constraint,
     * <code>false</code> otherwise.
     * 
     */
    public boolean isTimeConstraint() {
    	if (this.timeOrder != null) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * @return <code>true<code> if the QueryBind contains some items limit retrieval 
     * constraint, <code>false</code> otherwise.
     * 
     */
    public boolean isLimitConstraint() {
    	if (this.firstElement != null) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * @return <code>true<code> if the QueryBind contains some order constraint,
     * <code>false</code> otherwise.
     * 
     */
    public boolean isOrderConstraint() {
    	if (this.orderingFields != null) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
}
    


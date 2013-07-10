package org.dpi.web.reporting.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to be used as a value object for representing the rows of any report.
 *
 */
public class GenericReportRecord implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> values= new HashMap<String, Object>();

	public GenericReportRecord() {
	}

	public Object getValue(String name){
		return values.get(name);
	}
	
	public void setValue(String name, Object value){
		values.put(name,value);
	}
}
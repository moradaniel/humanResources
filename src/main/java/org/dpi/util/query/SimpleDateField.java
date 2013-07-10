/**
 * 
 */
package org.dpi.util.query;



/**
 * @author pc
 *
 */
public class SimpleDateField implements DateField {

	private String dateField;
	
	public String getFieldName() {
		return this.dateField;
	}

	public SimpleDateField(String dateField) {
		super();
		this.dateField = dateField;
	}
	
}

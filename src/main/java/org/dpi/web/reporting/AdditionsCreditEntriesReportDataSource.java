package org.dpi.web.reporting;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.dpi.web.reporting.dto.GenericReportRecord;

public class AdditionsCreditEntriesReportDataSource implements JRDataSource
{
	
	private Iterator<GenericReportRecord> recordIterator;
	private GenericReportRecord currentRecord;

	protected int currRowNum = 0;
	
	public enum ReportFieldID {
		PERSON_NUEVO_PERFIL,
		NEW_EMPLOYMENT_PARENT_OCCUPATIONAL_GROUP,
		PERSON_NUEVO_CATEGORIA_PROPUESTA,
		NUMBER_OF_CREDITS
	};
	
	public AdditionsCreditEntriesReportDataSource(List<GenericReportRecord> records)
	{
		this.recordIterator = records.iterator();
	}
	
	public Object getFieldValue(JRField jrField) throws JRException
	{
		return currentRecord.getValue(jrField.getName());
	}
	

	public boolean next() throws JRException
	{
		boolean hasNext = recordIterator.hasNext();
		
		if (hasNext)
		{
			currentRecord = recordIterator.next();
			currRowNum++;
		}
		
		return hasNext;
	}

}

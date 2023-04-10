package org.dpi.department;

import java.util.List;

import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.stats.PeriodSummaryData;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Department objects from
 * persistent storage
 *
 */
public interface DepartmentReportService extends ApplicationContextAware
{
	
    public List<PeriodSummaryData> getPeriodDepartmentsSummaryData(CreditsPeriod creditsPeriod);
    
    public PeriodSummaryData buildPeriodSummaryData(Department department, CreditsPeriod creditsPeriod);
}

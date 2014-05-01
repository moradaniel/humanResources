package org.dpi.web.reporting;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.dpi.web.reporting.jasper.ExporterService;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.dpi.web.reporting.parameters.EmployeeAdditionsPromotionsReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Service for processing Jasper  reports
 * 
 */
//@Service("downloadService")
//@Transactional
public class DownloadService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String TEMPLATE_FOLDER = "/WEB-INF/reports/";
	
	
	@Autowired
	ServletContext servletContext;
	

	@Resource(name = "exporterService")
	private ExporterService exporter;
		
	
	@Resource(name = "employeeAdditionsPromotionsReportReportService")
	private EmployeeAdditionsPromotionsReportReportService employeeAdditionsPromotionsReportReportService;

	public void download(AbstractReportParameters reportParams,HttpServletResponse response) {

		try {
			
			
			Map<String, Object> reportParamMap = new HashMap<String, Object>();
			
						
			
			// 1. Add report parameters
			
			reportParamMap.put("Title", "Report");

			// 2.  Retrieve template

			//InputStream reportStream = this.getClass().getResourceAsStream(TEMPLATE);
			
			InputStream reportStream = servletContext.getResourceAsStream(TEMPLATE_FOLDER+reportParams.getTemplateFileName()) ;
			
			//InputStream reportStream = this.getClass().getResourceAsStream(TEMPLATE);
			
			Map<String, Object> recordsMap = null;
			
			if (reportParams instanceof EmployeeAdditionsPromotionsReportParameters)
			{
				recordsMap =	employeeAdditionsPromotionsReportReportService.getReportData((EmployeeAdditionsPromotionsReportParameters) reportParams);
				
			}
			
			for (Map.Entry<String, Object> entry : recordsMap.entrySet()) {
				reportParamMap.put(entry.getKey(), entry.getValue());
			}

			// 3. Convert template to JasperDesign
			JasperDesign jd = JRXmlLoader.load(reportStream);

			// 4. Compile design to JasperReport
			JasperReport jr = JasperCompileManager.compileReport(jd);

			Locale locale = new Locale("es", "AR");
			reportParamMap.put(JRParameter.REPORT_LOCALE, locale);
			
			// 5. Create the JasperPrint object
			// Make sure to pass the JasperReport, report parameters, and data source
			JasperPrint jp = JasperFillManager.fillReport(jr, reportParamMap, new JREmptyDataSource());

			// 6. Create an output byte stream where data will be written
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// 7. Export report
			exporter.export(reportParams.getOutputFormat(), jp, response, baos);
			


			// 8. Write to reponse stream
			write(response, baos);

		} catch (JRException jre) {
			logger.error("Unable to process download");
			throw new RuntimeException(jre);
		}
	}

	/**
	* Writes the report to the output stream
	*/
	private void write(HttpServletResponse response,
			ByteArrayOutputStream baos) {

		try {
			logger.debug(String.valueOf(baos.size()));

			// Retrieve output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// Write to output stream
			baos.writeTo(outputStream);
			// Flush the stream
			outputStream.flush();

			// Remove download token
			//tokenService.remove(token);

		} catch (Exception e) {
			logger.error("Unable to write report to the output stream");
			throw new RuntimeException(e);
		}
	}
	
}
package org.dpi.web.reporting;

import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReportGenerator implements ReportGenerator{

   public void generate(ReportParameters parameters, OutputStream outputStream) throws Exception{
        Workbook wb = exportEventsToCSV();
        //response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getFileName()+".xls");
        wb.write(outputStream);
    }
   
   //public byte[] exportEventsToCSV() {
   public HSSFWorkbook exportEventsToCSV() {
       //try {

           HSSFWorkbook workbook = new HSSFWorkbook();
           HSSFSheet worksheet = workbook.createSheet("POI Worksheet");
           HSSFCellStyle cellStyle = workbook.createCellStyle();


           //workbook.write(fileOut);

           //return workbook.getBytes();
           return workbook;
       /*} catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }*/
      // return null;
   }


}

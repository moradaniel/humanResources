package org.dpi.common;

  import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

 
  public class DateSerializer extends JsonSerializer<Date> {  
    
	  //public static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      /** String formatters in JDK are not thread-safe */
	  /** using ThreadLocal here so each thread may have one copy of DATE_FORMATTER . */
	  public static final ThreadLocal<DateFormat> DATE_FORMATTER =
	        new ThreadLocal<DateFormat>() {
	           @Override
	           protected SimpleDateFormat initialValue() {
	              return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	           }
	        };
	
    @Override  
    public void serialize(Date value_p, JsonGenerator gen, SerializerProvider prov_p)  
      throws IOException, JsonProcessingException  
    {  
        
      String formattedDate = DATE_FORMATTER.get().format(value_p);  
      gen.writeString(formattedDate);  
    }  
  }  
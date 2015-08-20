package org.dpi.employment;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EmploymentSerializer extends JsonSerializer<EmploymentImpl> {

	@Override
	public void serialize(EmploymentImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		
		
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        //jgen.writeObjectField("startDate", value.getStartDate()==null ?"" : format.format(value.getStartDate()));
        jgen.writeObjectField("startDate", value.getStartDate());
        //jgen.writeStringField("endDate", value.getEndDate()==null ?"" :format.format(value.getEndDate()));
        jgen.writeObjectField("endDate", value.getEndDate());
        jgen.writeStringField("status",value.getStatus()==null ?"" :value.getStatus().name());
        jgen.writeObjectField("person",value.getPerson());
        jgen.writeObjectField("category",value.getCategory());
        jgen.writeObjectField("subDepartment",value.getSubDepartment());
        jgen.writeObjectField("occupationalGroup",value.getOccupationalGroup());
        
        
        
        jgen.writeEndObject();

	}
	
}

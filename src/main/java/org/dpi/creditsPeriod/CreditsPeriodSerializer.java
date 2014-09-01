package org.dpi.creditsPeriod;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CreditsPeriodSerializer extends JsonSerializer<CreditsPeriodImpl> {

	@Override
	public void serialize(CreditsPeriodImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeObjectField("name", value.getName());
        
        jgen.writeEndObject();

	}
	
}

package org.dpi.department;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DepartmentSerializer extends JsonSerializer<DepartmentImpl> {

	@Override
	public void serialize(DepartmentImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("code", value.getCode());
        jgen.writeStringField("name", value.getName());

        jgen.writeEndObject();
        
	}

}

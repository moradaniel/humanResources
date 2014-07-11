package org.dpi.category;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CategorySerializer extends JsonSerializer<CategoryImpl> {

	@Override
	public void serialize(CategoryImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("code", value.getCode());
 	
        jgen.writeEndObject();
        
	}

}

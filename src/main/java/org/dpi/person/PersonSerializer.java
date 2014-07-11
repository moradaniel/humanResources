package org.dpi.person;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PersonSerializer extends JsonSerializer<PersonImpl> {

	@Override
	public void serialize(PersonImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("apellidoNombre", value.getApellidoNombre());
        jgen.writeStringField("cuil", value.getCuil());
    	
        jgen.writeEndObject();
        
	}

}

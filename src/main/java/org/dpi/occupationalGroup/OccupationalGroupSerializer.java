package org.dpi.occupationalGroup;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class OccupationalGroupSerializer extends JsonSerializer<OccupationalGroupImpl> {

	@Override
	public void serialize(OccupationalGroupImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
        jgen.writeStartObject();
        jgen.writeStringField("code", value.getCode());
        jgen.writeStringField("name", value.getName());
        
        //start parentoOccupationalGroup object
        OccupationalGroup parentoOccupationalGroup = value.getParentOccupationalGroup();
        jgen.writeObjectFieldStart("parentOccupationalGroup"); 
        jgen.writeStringField("code", parentoOccupationalGroup.getCode());
        jgen.writeStringField("name", parentoOccupationalGroup.getName());
        jgen.writeEndObject(); 
        //end parentoOccupationalGroup object
        
        
        jgen.writeEndObject();

	}
	
}

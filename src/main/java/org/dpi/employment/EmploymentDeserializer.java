package org.dpi.employment;

import java.io.IOException;

import org.dpi.person.PersonImpl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class EmploymentDeserializer extends JsonDeserializer<EmploymentImpl> {

    @Override
    public EmploymentImpl deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        
        EmploymentImpl employment = new EmploymentImpl();
        employment.setId(Long.parseLong(node.get("id").asText()));
        
        PersonImpl person = new PersonImpl();
        person.setApellidoNombre(node.get("person").get("apellidoNombre").asText());
        person.setCuil(node.get("person").get("cuil").asText());
        
        employment.setPerson(person);
        return employment;
    }
}
package org.dpi.centroSector;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CentroSectorSerializer extends JsonSerializer<CentroSectorImpl> {

	@Override
	public void serialize(CentroSectorImpl value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("codigoCentro", value.getCodigoCentro());
        jgen.writeStringField("codigoSector", value.getCodigoSector());
        jgen.writeStringField("nombreCentro", value.getNombreCentro());
        jgen.writeStringField("nombreSector", value.getNombreSector());

        jgen.writeEndObject();
        
	}

}

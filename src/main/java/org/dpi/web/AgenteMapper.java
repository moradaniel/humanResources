package org.dpi.web;

import java.util.ArrayList;
import java.util.List;

import org.dpi.agente.Agente;
import org.dpi.util.PageList;
import org.dpi.web.response.AgenteDto;


public class AgenteMapper {

	public static AgenteDto map(Agente agente) {
			AgenteDto dto = new AgenteDto();
			dto.setId(agente.getId());
			dto.setApellidoNombre(agente.getApellidoNombre());
			dto.setCuil(agente.getCuil());
			return dto;
	}
	
	public static List<AgenteDto> map(PageList<Agente> agentes) {
		List<AgenteDto> dtos = new ArrayList<AgenteDto>();
		for (Agente agente: agentes) {
			dtos.add(map(agente));
		}
		return dtos;
	}
}

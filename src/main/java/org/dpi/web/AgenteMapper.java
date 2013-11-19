package org.dpi.web;

import java.util.ArrayList;
import java.util.List;

import org.dpi.person.Person;
import org.dpi.util.PageList;
import org.dpi.web.response.PersonDto;


public class AgenteMapper {

	public static PersonDto map(Person agente) {
			PersonDto dto = new PersonDto();
			dto.setId(agente.getId());
			dto.setApellidoNombre(agente.getApellidoNombre());
			dto.setCuil(agente.getCuil());
			return dto;
	}
	
	public static List<PersonDto> map(PageList<Person> agentes) {
		List<PersonDto> dtos = new ArrayList<PersonDto>();
		for (Person agente: agentes) {
			dtos.add(map(agente));
		}
		return dtos;
	}
}

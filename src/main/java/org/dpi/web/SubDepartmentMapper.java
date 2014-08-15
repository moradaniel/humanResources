package org.dpi.web;

import java.util.ArrayList;
import java.util.List;

import org.dpi.subDepartment.SubDepartment;
import org.dpi.util.PageList;
import org.dpi.web.response.SubDepartmentDto;


public class SubDepartmentMapper {

	public static SubDepartmentDto map(SubDepartment subDepartment) {
			SubDepartmentDto dto = new SubDepartmentDto();
			dto.setId(subDepartment.getId());
			dto.setCodigoCentro(subDepartment.getCodigoCentro());
			dto.setNombreCentro(subDepartment.getNombreCentro());
			dto.setCodigoSector(subDepartment.getCodigoSector());
			dto.setNombreSector(subDepartment.getNombreSector());
			return dto;
	}
	
	public static List<SubDepartmentDto> map(PageList<SubDepartment> subDepartments) {
		List<SubDepartmentDto> dtos = new ArrayList<SubDepartmentDto>();
		for (SubDepartment subDepartment: subDepartments) {
			dtos.add(map(subDepartment));
		}
		return dtos;
	}
}

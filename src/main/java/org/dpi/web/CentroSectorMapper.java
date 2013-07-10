package org.dpi.web;

import java.util.ArrayList;
import java.util.List;

import org.dpi.centroSector.CentroSector;
import org.dpi.util.PageList;
import org.dpi.web.response.CentroSectorDto;


public class CentroSectorMapper {

	public static CentroSectorDto map(CentroSector centroSector) {
			CentroSectorDto dto = new CentroSectorDto();
			dto.setId(centroSector.getId());
			dto.setCodigoCentro(centroSector.getCodigoCentro());
			dto.setNombreCentro(centroSector.getNombreCentro());
			dto.setCodigoSector(centroSector.getCodigoSector());
			dto.setNombreSector(centroSector.getNombreSector());
			return dto;
	}
	
	public static List<CentroSectorDto> map(PageList<CentroSector> centroSectores) {
		List<CentroSectorDto> dtos = new ArrayList<CentroSectorDto>();
		for (CentroSector centroSector: centroSectores) {
			dtos.add(map(centroSector));
		}
		return dtos;
	}
}

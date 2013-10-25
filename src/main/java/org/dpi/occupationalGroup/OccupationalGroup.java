package org.dpi.occupationalGroup;

import java.io.Serializable;
import java.util.Set;

import org.dpi.categoria.Categoria;
import org.dpi.domain.Persistent;


public interface OccupationalGroup extends Persistent,Serializable{

	
	public String getCode();
	
	public void setCode(String code);

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public OccupationalGroup getParentOccupationalGroup();

	public void setParentOccupationalGroup(OccupationalGroup parentOccupationalGroup);

	public Set<OccupationalGroup> getChildrenOccupationalGroup();

	public void setChildrenOccupationalGroup(
			Set<OccupationalGroup> childrenOccupationalGroup);
	
	
	public Categoria getMinimumCategory();
	
	public void setMinimumCategory(Categoria minimumCategory);

	public Categoria getMaximumCategory();

	public void setMaximumCategory(Categoria maximumCategory);
	

}
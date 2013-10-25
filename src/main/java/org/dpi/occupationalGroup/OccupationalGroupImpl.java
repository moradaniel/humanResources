package org.dpi.occupationalGroup;

import java.util.HashSet;
import java.util.Set;

import org.dpi.categoria.Categoria;
import org.dpi.domain.PersistentAbstract;

public class OccupationalGroupImpl extends PersistentAbstract implements OccupationalGroup{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String code;
	String name;
	String description;
	
	Categoria minimumCategory;
	
	Categoria maximumCategory;
	
	OccupationalGroup parentOccupationalGroup;
		
	Set<OccupationalGroup> childrenOccupationalGroup = new HashSet<OccupationalGroup>();


	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public OccupationalGroup getParentOccupationalGroup() {
		return parentOccupationalGroup;
	}

	@Override
	public void setParentOccupationalGroup(OccupationalGroup parentOccupationalGroup) {
		this.parentOccupationalGroup = parentOccupationalGroup;
	}

	@Override
	public Set<OccupationalGroup> getChildrenOccupationalGroup() {
		return childrenOccupationalGroup;
	}

	@Override
	public void setChildrenOccupationalGroup(
			Set<OccupationalGroup> childrenOccupationalGroup) {
		this.childrenOccupationalGroup = childrenOccupationalGroup;
	}
	
	@Override
	public Categoria getMinimumCategory() {
		return minimumCategory;
	}

	@Override
	public void setMinimumCategory(Categoria minimumCategory) {
		this.minimumCategory = minimumCategory;
	}

	@Override
	public Categoria getMaximumCategory() {
		return maximumCategory;
	}

	@Override
	public void setMaximumCategory(Categoria maximumCategory) {
		this.maximumCategory = maximumCategory;
	}

}

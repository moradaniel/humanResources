package org.dpi.occupationalGroup;

import java.util.HashSet;
import java.util.Set;

import org.dpi.category.Category;
import org.dpi.domain.PersistentAbstract;

public class OccupationalGroupImpl extends PersistentAbstract implements OccupationalGroup{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String code;
	String name;
	String description;
	
	Category minimumCategory;
	
	Category maximumCategory;
	
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
	public Category getMinimumCategory() {
		return minimumCategory;
	}

	@Override
	public void setMinimumCategory(Category minimumCategory) {
		this.minimumCategory = minimumCategory;
	}

	@Override
	public Category getMaximumCategory() {
		return maximumCategory;
	}

	@Override
	public void setMaximumCategory(Category maximumCategory) {
		this.maximumCategory = maximumCategory;
	}

}

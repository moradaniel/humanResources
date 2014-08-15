package org.dpi.common;

import java.util.Date;

import org.dpi.category.Category;
import org.dpi.category.CategoryImpl;
import org.dpi.category.CategorySerializer;
import org.dpi.employment.EmploymentImpl;
import org.dpi.employment.EmploymentSerializer;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.occupationalGroup.OccupationalGroupImpl;
import org.dpi.occupationalGroup.OccupationalGroupSerializer;
import org.dpi.person.Person;
import org.dpi.person.PersonImpl;
import org.dpi.person.PersonSerializer;
import org.dpi.subDepartment.SubDepartment;
import org.dpi.subDepartment.SubDepartmentImpl;
import org.dpi.subDepartment.SubDepartmentSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;



/**
 * TODO find a way to register this mapper using Spring
 *
 */

public class CustomObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
		super();

		SimpleModule module = new SimpleModule();
		
		module.addSerializer(Date.class, new DateSerializer());
		module.addDeserializer(Date.class, new DateDeserializer());
		
		module.addSerializer(EmploymentImpl.class, new EmploymentSerializer());
		//module.addDeserializer(EmploymentImpl.class, new EmploymentDeserializer());
		
		module.addSerializer(PersonImpl.class, new PersonSerializer());
		module.addSerializer(CategoryImpl.class, new CategorySerializer());
		module.addSerializer(OccupationalGroupImpl.class, new OccupationalGroupSerializer());
		module.addSerializer(SubDepartmentImpl.class, new SubDepartmentSerializer());
		
		module.addAbstractTypeMapping(Person.class, PersonImpl.class);
		module.addAbstractTypeMapping(Category.class, CategoryImpl.class);
		module.addAbstractTypeMapping(OccupationalGroup.class,OccupationalGroupImpl.class);
		module.addAbstractTypeMapping(SubDepartment.class,SubDepartmentImpl.class);
		

		this.registerModule(module);
	}


}

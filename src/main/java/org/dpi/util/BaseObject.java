package org.dpi.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;


/**
 * Base class for Model objects.  All classes should implements toString method
 * to facilitate testing
 *
 * <p>
 * <a href="BaseObject.java.html"><i>View Source</i></a>
 * </p>
 *
 */
public abstract class BaseObject implements Serializable {
	
    public final static ToStringStyle STRING_STYLE = ToStringStyle.DEFAULT_STYLE;

	private static final String LAZY_MESSAGE = "CAUTION: This property is not initialized yet!";
    
    private static Stack<BaseObject> VISITED_TOSTRING_INSTANCES = new Stack<BaseObject>();
    
//    public String toString() {
//    	return super.toString();
//    	
//    	if (!BaseObject.objectVisited4toString(this)) { // If the object is not already visited we push the current object to take care about that fact
//    		BaseObject.VISITED_TOSTRING_INSTANCES.push(this);
//    	} else { // If was visited before, we avoid deadlock by returning a simple string
//    		return "[CAUTION! Object already visited]";
//    	}
//        
//		Map<String,Object> map = new HashMap<String,Object>();
//		Class currentClass = this.getClass();
//		while (!currentClass.equals(BaseObject.class)){
//			map.putAll(BaseObject.getAllDeclaredFieldNamesOfClass(this, currentClass));
//			currentClass = currentClass.getSuperclass();
//		}
//		
//		ToStringBuilder builder = new ToStringBuilder(this, es.tecnocasa.util.BaseObject.STRING_STYLE)               
//	        .append("ClassName", this.getClass().toString());
//
//		if (map != null && map.keySet() != null){
//			builder = new ToStringBuilder(this, es.tecnocasa.util.BaseObject.STRING_STYLE);
//			for (Object currentKey : map.keySet()) {
//				try {
//					Object value = map.get(currentKey);
//					builder.append(currentKey+" :", value);
//				} catch (Exception ignored){}
//			}
//		}
//		
//		BaseObject.VISITED_TOSTRING_INSTANCES.pop();
//    	
//        return builder.toString();
//    }
    
    @Override
    public abstract boolean equals(Object o);
    
    @Override
	public int hashCode() {
    	return 0;
    }
    
    /**
     * Test if the object don't contain any information, every attribute is empty
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
    	if (isEmptyValue(object)){
    		return true;
    	} 
    	
		int numElems = elements(object);
		if (numElems >= 0){
			return numElems == 0;
		}
    	
		Map<String,Object> map = new HashMap<String,Object>();
		Class currentClass = object.getClass();
		while (!currentClass.equals(BaseObject.class) && !currentClass.equals(Object.class)){
			map.putAll(BaseObject.getAllDeclaredFieldNamesOfClass(object, currentClass));
			currentClass = currentClass.getSuperclass();
		}

		// Now we have in the map all the 
		if (map != null && map.keySet() != null){
			for (Object currentKey : map.keySet()) {
				try {
					Object value = map.get(currentKey);
					if (!isEmptyValue(value)){
						// If the value has something (different from a lazy message) 
						// it means that the object is not empty 
						return false;
					}
				} catch (Exception ignored){}
			}
		}
        return true;
    }
    
    /**
	 * This method return the number of elements that the object has as a
	 * collection or array. If is not a collection or an array just return -1,
	 * to know the others that is not a collection
	 * 
	 * @param o
	 * @return
	 */
    private static int elements(Object o){
    	int result = -1;
    	if (o instanceof Collection){
    		Collection c = (Collection)o;
    		return c.size();
    	} else if (o instanceof Object[]){
    		Object[] array = (Object[]) o;
    		return array.length;
    	}
    	return result;
    }
    
    /**
     * @param o
     * @return Return true if the object can be considered as a null value
     */
    private static boolean isEmptyValue(Object o){
    	if (o == null) {
    		return true;
    	} else {
    		int numElems = elements(o);
    		if (numElems >= 0){
    			return numElems == 0;
    		}
    		return (o.toString().trim().length() == 0 || BaseObject.LAZY_MESSAGE
					.equals(o));
    	}
    }
    
    public static boolean isNotEmpty(Object o){
    	if (o != null){
    		if (o instanceof Collection){
        		Collection c = (Collection)o;
        		return (c.size() != 0);
        	} else if (o instanceof Object[]){
        		Object[] array = (Object[]) o;
        		return array.length != 0;
        	}
    	}
    	
    	return !BaseObject.isEmpty(o);
    }

    
    /**
     * This method search all the field declared in the class passed as a parameter (doesn't return the inherited methods)
     * @param c
     * @return if there is a LazyInitializationException we just return a CAUTION exception
     */
    private static Map<String,Object> getAllDeclaredFieldNamesOfClass(Object o, Class c){
		Field[] fields = c.getDeclaredFields();
		Map<String,Object> map = new HashMap<String,Object>();
		for (Field field : fields) {
			if (ignoredProperties.contains(field.getName())){
				continue;
			}
			String propertyValue = null;
			try {
				if (Hibernate.isPropertyInitialized(o, field.getName())) {
					propertyValue = BeanUtils.getProperty(o,field.getName());
				} else {
					propertyValue = BaseObject.LAZY_MESSAGE;
				}
			} catch (LazyInitializationException  e) {
				propertyValue = BaseObject.LAZY_MESSAGE;
			} catch (Exception ignored) {}
			map.put(field.getName(), propertyValue);
		}
		return map;
    }
    

    private static boolean objectVisited4toString(BaseObject o){
    	for (BaseObject currentObject : BaseObject.VISITED_TOSTRING_INSTANCES) {
			if (currentObject == o) {
				return true;
			}
		}
    	return false;
    }
    
    private static Hashtable<String,String> ignoredProperties = new Hashtable<String,String>();
    static {
    	String[] ignoredPropertiesStrings = {"serialVersionUID", "STRING_STYLE", "log"};
    	for (String string : ignoredPropertiesStrings) {
			ignoredProperties.put(string,string);
		}
    }
    
    /**
	 * This is an utility method to clone an object.
	 * 
	 * Clone means, copy ALL the properties for the properties of the objects
	 * (including the objects inside this object) but the hibernate ID. Then
	 * hibernate will understand this as a different object
	 * 
	 * @return
	 */
    public Object cloneObject()  {
    	Object clonedByUtils = null;
		try {
			clonedByUtils = BeanUtils.cloneBean(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
    	return clonedByUtils;
    }
}

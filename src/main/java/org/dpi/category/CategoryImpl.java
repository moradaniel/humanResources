package org.dpi.category;

import org.dpi.domain.PersistentAbstract;

public class CategoryImpl  extends PersistentAbstract implements Category{
	
	String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

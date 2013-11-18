package org.dpi.category;

import java.io.Serializable;

import org.dpi.domain.Persistent;


public interface Category extends Persistent,Serializable{

	String getCode();

	void setCode(String code);

}
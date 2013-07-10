package org.dpi.categoria;

import java.io.Serializable;

import org.dpi.domain.Persistent;


public interface Categoria extends Persistent,Serializable{

	String getCodigo();

	void setCodigo(String codigo);

}
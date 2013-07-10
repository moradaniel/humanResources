package org.dpi.categoria;

import org.dpi.domain.PersistentAbstract;

public class CategoriaImpl  extends PersistentAbstract implements Categoria{
	
	String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}

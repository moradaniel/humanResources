package org.dpi.movimientoCreditos;


public class MovimientoCreditosVO{
	

	MovimientoCreditos movimientoCreditos =  new MovimientoCreditosImpl();
	
	String categoriaActual;

	String categoriaPropuesta;
	
	boolean canAccountBorrarMovimiento = false;
	boolean canAccountCambiarEstadoMovimiento = false;
	
	public MovimientoCreditosVO(){
		super();
	}
	
	public MovimientoCreditos getMovimientoCreditos() {
		return movimientoCreditos;
	}

	public void setMovimientoCreditos(MovimientoCreditos movimientoCreditos) {
		this.movimientoCreditos = movimientoCreditos;
	}

	public String getCategoriaActual() {
		return categoriaActual;
	}

	public void setCategoriaActual(String categoriaActual) {
		this.categoriaActual = categoriaActual;
	}

	public String getCategoriaPropuesta() {
		return categoriaPropuesta;
	}

	public void setCategoriaPropuesta(String categoriaPropuesta) {
		this.categoriaPropuesta = categoriaPropuesta;
	}
	
	public boolean isCanAccountBorrarMovimiento() {
		return canAccountBorrarMovimiento;
	}

	public void setCanAccountBorrarMovimiento(boolean canAccountBorrarMovimiento) {
		this.canAccountBorrarMovimiento = canAccountBorrarMovimiento;
	}

	public boolean isCanAccountCambiarEstadoMovimiento() {
		return canAccountCambiarEstadoMovimiento;
	}

	public void setCanAccountCambiarEstadoMovimiento(
			boolean canAccountCambiarEstadoMovimiento) {
		this.canAccountCambiarEstadoMovimiento = canAccountCambiarEstadoMovimiento;
	}


}

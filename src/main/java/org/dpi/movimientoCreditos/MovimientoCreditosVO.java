package org.dpi.movimientoCreditos;


public class MovimientoCreditosVO{
	

	MovimientoCreditos movimientoCreditos =  new MovimientoCreditosImpl();
	
	String currentCategory;

	String proposedCategory;
	
	String occupationalGroup;

	String parentOccupationalGroup;
	
		
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

	public String getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(String currentCategory) {
		this.currentCategory = currentCategory;
	}

	public String getProposedCategory() {
		return proposedCategory;
	}

	public void setProposedCategory(String proposedCategory) {
		this.proposedCategory = proposedCategory;
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

	public void setCanAccountChangeCreditsEntryStatus(
			boolean canAccountCambiarEstadoMovimiento) {
		this.canAccountCambiarEstadoMovimiento = canAccountCambiarEstadoMovimiento;
	}

	public String getOccupationalGroup() {
		return occupationalGroup;
	}

	public void setOccupationalGroup(String occupationalGroup) {
		this.occupationalGroup = occupationalGroup;
	}

	public String getParentOccupationalGroup() {
		return parentOccupationalGroup;
	}

	public void setParentOccupationalGroup(String parentOccupationalGroup) {
		this.parentOccupationalGroup = parentOccupationalGroup;
	}

}

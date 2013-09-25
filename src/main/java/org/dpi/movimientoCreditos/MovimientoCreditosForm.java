package org.dpi.movimientoCreditos;

import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;



public class MovimientoCreditosForm {
	
	private Long movimientoCreditosId;

	//fields to show in labels
	private String employeeName;
	

	//fields edited by the form
	private GrantedStatus grantedStatus;
	
	
	public MovimientoCreditosForm() {
	}
	
	public MovimientoCreditosForm(MovimientoCreditos movimientoCreditos) {
		//to retrieve the movimiento from database
		this.movimientoCreditosId=movimientoCreditos.getId();
		
		//to show
		this.employeeName=movimientoCreditos.getEmpleo().getAgente().getApellidoNombre();
		
		//to edit
		this.grantedStatus=movimientoCreditos.getGrantedStatus();
				

	}

	public Long getMovimientoCreditosId() {
		return movimientoCreditosId;
	}

	public void setMovimientoCreditosId(Long movimientoCreditosId) {
		this.movimientoCreditosId = movimientoCreditosId;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public GrantedStatus getGrantedStatus() {
		return grantedStatus;
	}

	public void setGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatus = grantedStatus;
	}
	
}
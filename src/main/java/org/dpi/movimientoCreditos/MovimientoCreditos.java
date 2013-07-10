package org.dpi.movimientoCreditos;

import java.io.Serializable;

import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.Persistent;
import org.dpi.empleo.Empleo;


public interface MovimientoCreditos extends Persistent,Serializable{
	
	public enum GrantedStatus{
		Requested,
		Granted
	}

	Empleo getEmpleo();

	void setEmpleo(Empleo empleo);
	
	int getCantidadCreditos();

	void setCantidadCreditos(int cantidadCreditos);
	
	public TipoMovimientoCreditos getTipoMovimientoCreditos();

	public void setTipoMovimientoCreditos(TipoMovimientoCreditos tipoMovimientoCreditos);
	
	void setObservaciones(String observaciones);
	
	String getObservaciones();

	public CreditsPeriod getCreditsPeriod();
	public void setCreditsPeriod(CreditsPeriod grantedStatus);

	public GrantedStatus getGrantedStatus();
	public void setGrantedStatus(GrantedStatus grantedStatus);
	
}
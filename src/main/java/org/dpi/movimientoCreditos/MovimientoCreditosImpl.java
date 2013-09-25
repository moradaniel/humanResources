package org.dpi.movimientoCreditos;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.PersistentAbstract;
import org.dpi.empleo.Empleo;
import org.janux.util.JanuxToStringStyle;

public class MovimientoCreditosImpl  extends PersistentAbstract implements MovimientoCreditos{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TipoMovimientoCreditos tipoMovimientoCreditos;

	Empleo empleo;
	int cantidadCreditos;

	private String observaciones;

	private CreditsPeriod creditsPeriod;
	
	private GrantedStatus grantedStatus;
	
	public MovimientoCreditosImpl() {
		super();
	}
	
	
	@Override
	public Empleo getEmpleo() {
		return this.empleo;
	}

	@Override
	public void setEmpleo(Empleo empleo) {
		this.empleo=empleo;
		
	}
	@Override
	public int getCantidadCreditos() {
		return this.cantidadCreditos;
	}

	@Override
	public void setCantidadCreditos(int cantidadCreditos) {
		this.cantidadCreditos=cantidadCreditos;
		
	}
	
	
	public TipoMovimientoCreditos getTipoMovimientoCreditos() {
		return tipoMovimientoCreditos;
	}

	public void setTipoMovimientoCreditos(
			TipoMovimientoCreditos tipoMovimientoCreditos) {
		this.tipoMovimientoCreditos = tipoMovimientoCreditos;
	}

	@Override
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
		
	}

	@Override
	public String getObservaciones() {
		return this.observaciones;
	}

	@Override
	public CreditsPeriod getCreditsPeriod() {
		return this.creditsPeriod;
	}

	@Override
	public void setCreditsPeriod(CreditsPeriod creditsPeriod) {
		this.creditsPeriod = creditsPeriod;
	}

	@Override
	public GrantedStatus getGrantedStatus() {
		return grantedStatus;
	}

	@Override
	public void setGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatus = grantedStatus;
	}

	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("tipoMovimientoCreditos", getTipoMovimientoCreditos().name());
		sb.append("grantedStatus", getGrantedStatus().name());
		sb.append("creditos", getCantidadCreditos());

		
		return sb.toString();
	}
	
}

package org.dpi.movimientoCreditos;

import java.util.List;

import org.springframework.util.AutoPopulatingList;

public class CambiosMultiplesEstadoMovimientosForm {

	private AutoPopulatingList<MovimientoCreditos> movimientosCreditos = new AutoPopulatingList<MovimientoCreditos>(MovimientoCreditosImpl.class);


	@SuppressWarnings("unchecked")
	public CambiosMultiplesEstadoMovimientosForm(){
	}
	
    public List<MovimientoCreditos> getMovimientos() {
        return movimientosCreditos;
    }
 
    public void setMovimientos(AutoPopulatingList<MovimientoCreditos> movimientosCreditosAscensoVO) {
        this.movimientosCreditos = movimientosCreditosAscensoVO;
    }
    
    public void addMovimiento(MovimientoCreditos movimiento) {
    	this.movimientosCreditos.add(movimiento);
    }

}

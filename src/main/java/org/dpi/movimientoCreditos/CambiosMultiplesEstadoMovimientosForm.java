package org.dpi.movimientoCreditos;

import java.util.List;

import org.springframework.util.AutoPopulatingList;

public class CambiosMultiplesEstadoMovimientosForm {

    //private List<MovimientoCreditosAscensoVO> movimientosCreditosAscensoVO = new LazyList<MovimientoCreditosAscensoVO>();
	//private List<MovimientoCreditosAscensoVO> movimientosCreditosAscensoVO;// = LazyList.getList(new List<MovimientoCreditosAscensoVO>());

	//private AutoPopulatingList<MovimientoCreditosAscensoVO> movimientosCreditosAscensoVO = new AutoPopulatingList<MovimientoCreditosAscensoVO>(MovimientoCreditosAscensoVO.class);
	
	private AutoPopulatingList<MovimientoCreditos> movimientosCreditos = new AutoPopulatingList<MovimientoCreditos>(MovimientoCreditosImpl.class); //<MovimientoCreditosAscensoVO>(new MovimientoEventElementFactory());

	/*
	public class MovimientoEventElementFactory implements ElementFactory<MovimientoCreditosAscensoVO> { 
	    @Override
	    public MovimientoCreditosAscensoVO createElement(int index) throws ElementInstantiationException {            
	    	MovimientoCreditosAscensoVO movimientoCreditosAscensoVO = new MovimientoCreditosAscensoVO(); 
	    	MovimientoCreditosImpl movimientoCreditos =  new MovimientoCreditosImpl();
	    	movimientoCreditosAscensoVO.setMovimientoCreditos(movimientoCreditos);
	       // e.setModContr(""); 
	       // e.setDesc("");          
	        return movimientoCreditosAscensoVO; 
	    } 
	}*/
	
	@SuppressWarnings("unchecked")
	public CambiosMultiplesEstadoMovimientosForm(){
		/*movimientosCreditosAscensoVO= LazyList.decorate(new ArrayList<MovimientoCreditosAscensoVO>(),  
			       FactoryUtils.instantiateFactory(MovimientoCreditosAscensoVO.class));
*/
	}
	
	//setItems(new AutoPopulatingList(new ShoppingBasketItemElementFactory())); 
    
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

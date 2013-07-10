package org.dpi.movimientoCreditos;

import java.util.List;

import org.janux.bus.security.Account;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Movimiento objects from
 * persistent storage
 *
 */
public interface MovimientoCreditosService extends ApplicationContextAware
{

	public List<MovimientoCreditos> find(MovimientoCreditosQueryFilter movimientoCreditosQueryFilter);
	public void delete(MovimientoCreditos movimientoCreditos);
	
	public List<MovimientoCreditosAscensoVO> buildMovimientoCreditosVO(List<MovimientoCreditos> movimientoCreditosReparticion, Account account);
	public void save(final MovimientoCreditos movimientoCreditos);
	public void saveOrUpdate(final MovimientoCreditos movimientoCreditos);
	public void actualizarCreditosPorAscenso();
}

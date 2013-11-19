package org.dpi.movimientoCreditos;

import java.util.List;
import java.util.Set;

import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
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
	
	public List<MovimientoCreditosVO> buildMovimientoCreditosVO(List<MovimientoCreditos> movimientoCreditosReparticion, Account account);
	public void saveOrUpdate(final MovimientoCreditos movimientoCreditos);
	public void actualizarCreditosPorAscenso();
	
	public void changeGrantedStatus(MovimientoCreditos movimiento, GrantedStatus newEstado);
	
	public Set<Long> haveMovimientosSolicitados(List<Long> personsIds, Long idReparticion,Long idCreditsPeriod);
		
}

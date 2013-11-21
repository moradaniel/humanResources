package org.dpi.creditsEntry;

import java.util.List;

import org.springframework.util.AutoPopulatingList;

public class CambiosMultiplesEstadoMovimientosForm {

	private AutoPopulatingList<CreditsEntry> creditsEntries = new AutoPopulatingList<CreditsEntry>(CreditsEntryImpl.class);


	@SuppressWarnings("unchecked")
	public CambiosMultiplesEstadoMovimientosForm(){
	}
	
    public List<CreditsEntry> getMovimientos() {
        return creditsEntries;
    }
 
    public void setMovimientos(AutoPopulatingList<CreditsEntry> creditsEntriesAscensoVO) {
        this.creditsEntries = creditsEntriesAscensoVO;
    }
    
    public void addMovimiento(CreditsEntry movimiento) {
    	this.creditsEntries.add(movimiento);
    }

}

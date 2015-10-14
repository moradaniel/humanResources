package org.dpi.stats;

public class HistoricPeriodSummaryData {
	
	String year;
	long creditosAcreditadosPorCargaInicial;
	long creditosDisponiblesInicioPeriodo;
	long creditosAcreditadosPorBajas;
	long creditosRetenidosPorBajas;
    long creditosConsumidosPorIngresosOAscensosOtorgados;
	long saldoCreditosAlFinalPeriodo;
	long totalCreditosReparticionAjustes_Debito;
    long totalCreditosReparticionAjustes_Credito;
	long totalCreditosReparticion_ReasignadosDeRetencion_Periodo;
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public long getCreditosAcreditadosPorCargaInicial() {
		return creditosAcreditadosPorCargaInicial;
	}
	public void setCreditosAcreditadosPorCargaInicial(
			long creditosAcreditadosPorCargaInicial) {
		this.creditosAcreditadosPorCargaInicial = creditosAcreditadosPorCargaInicial;
	}
	public long getCreditosAcreditadosPorBajas() {
		return creditosAcreditadosPorBajas;
	}
	public void setCreditosAcreditadosPorBajas(long creditosAcreditadosPorBajas) {
		this.creditosAcreditadosPorBajas = creditosAcreditadosPorBajas;
	}
	public long getCreditosConsumidosPorIngresosOAscensosOtorgados() {
		return creditosConsumidosPorIngresosOAscensosOtorgados;
	}
	public void setCreditosConsumidosPorIngresosOAscensosOtorgados(
			long creditosConsumidosPorIngresosOAscensosOtorgados) {
		this.creditosConsumidosPorIngresosOAscensosOtorgados = creditosConsumidosPorIngresosOAscensosOtorgados;
	}
	public long getSaldoCreditosAlFinalPeriodo() {
		return saldoCreditosAlFinalPeriodo;
	}
	public void setSaldoCreditosAlFinalPeriodo(long saldoCreditosAlFinalPeriodo) {
		this.saldoCreditosAlFinalPeriodo = saldoCreditosAlFinalPeriodo;
	}
	public long getCreditosDisponiblesInicioPeriodo() {
		return creditosDisponiblesInicioPeriodo;
	}
	public void setCreditosDisponiblesInicioPeriodo(long creditosDisponiblesInicioPeriodo) {
		this.creditosDisponiblesInicioPeriodo = creditosDisponiblesInicioPeriodo;
	}
	
    public long getTotalCreditosReparticionAjustes_Debito() {
        return totalCreditosReparticionAjustes_Debito;
    }
    
    public void setTotalCreditosReparticionAjustes_Debito(Long totalCreditosReparticionAjustes_Debito) {
        this.totalCreditosReparticionAjustes_Debito = totalCreditosReparticionAjustes_Debito;
    }

    public long getTotalCreditosReparticionAjustes_Credito() {
        return totalCreditosReparticionAjustes_Credito;
    }

    public void setTotalCreditosReparticionAjustes_Credito(Long totalCreditosReparticionAjustes_Credito) {
        this.totalCreditosReparticionAjustes_Credito = totalCreditosReparticionAjustes_Credito;
    }
    
    
    public long getTotalCreditosReparticion_ReasignadosDeRetencion() {
        return totalCreditosReparticion_ReasignadosDeRetencion_Periodo;
    }
    
    public void setTotalCreditosReparticion_ReasignadosDeRetencion( Long totalCreditosReparticion_ReasignadosDeRetencion_Periodo) {
        this.totalCreditosReparticion_ReasignadosDeRetencion_Periodo = totalCreditosReparticion_ReasignadosDeRetencion_Periodo;
        
    }
    public void setCreditosRetenidosPorBajas(Long creditosRetenidosPorBajas) {
        this.creditosRetenidosPorBajas = creditosRetenidosPorBajas;
        
    }
    public long getCreditosRetenidosPorBajas() {
        return creditosRetenidosPorBajas;
    }


}

package org.dpi.stats;

public class HistoricPeriodSummaryData {
	
	String year;
	long creditosAcreditadosPorCargaInicial;
	long creditosDisponiblesInicioPeriodo;
	long creditosAcreditadosPorBajas;
	long creditosConsumidosPorIngresosOAscensosOtorgados;
	long saldoCreditosAlFinalPeriodo;
	
	
	
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
	public void setCreditosDisponiblesInicioPeriodo(
			long creditosDisponiblesInicioPeriodo) {
		this.creditosDisponiblesInicioPeriodo = creditosDisponiblesInicioPeriodo;
	}


}

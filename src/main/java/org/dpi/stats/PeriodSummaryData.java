package org.dpi.stats;

public class PeriodSummaryData {

        private String year;
		private long creditosDisponiblesInicioPeriodo;
    	private long creditosAcreditadosPorBajaDurantePeriodo;
    	private long retainedCredits;
    	private long totalAvailableCredits;
    	private long creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo;
    	private long creditosPorIngresosOAscensosOtorgadosPeriodo;
    	private long creditosDisponiblesSegunSolicitadoPeriodo;
    	private long creditosDisponiblesSegunOtorgadoPeriodo;
    	
        

    	
		public long getCreditosDisponiblesSegunOtorgadoPeriodo() {
			return creditosDisponiblesSegunOtorgadoPeriodo;
		}
		public void setCreditosDisponiblesSegunOtorgadoPeriodo(
				long creditosDisponiblesSegunOtorgadoPeriodo) {
			this.creditosDisponiblesSegunOtorgadoPeriodo = creditosDisponiblesSegunOtorgadoPeriodo;
		}
		public long getCreditosDisponiblesSegunSolicitadoPeriodo() {
			return creditosDisponiblesSegunSolicitadoPeriodo;
		}
		public void setCreditosDisponiblesSegunSolicitadoPeriodo(
				long creditosDisponiblesSegunSolicitadoPeriodo) {
			this.creditosDisponiblesSegunSolicitadoPeriodo = creditosDisponiblesSegunSolicitadoPeriodo;
		}
		public long getCreditosPorIngresosOAscensosOtorgadosPeriodo() {
			return creditosPorIngresosOAscensosOtorgadosPeriodo;
		}
		public void setCreditosPorIngresosOAscensosOtorgadosPeriodo(
				long creditosPorIngresosOAscensosOtorgadosPeriodo) {
			this.creditosPorIngresosOAscensosOtorgadosPeriodo = creditosPorIngresosOAscensosOtorgadosPeriodo;
		}
		public long getCreditosConsumidosPorIngresosOAscensosSolicitadosPeriodo() {
			return creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo;
		}
		public void setCreditosConsumidosPorIngresosOAscensosSolicitadosPeriodo(
				long creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo) {
			this.creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo = creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo;
		}
		public long getCreditosAcreditadosPorBajaDurantePeriodo() {
			return creditosAcreditadosPorBajaDurantePeriodo;
		}
		public void setCreditosAcreditadosPorBajaDurantePeriodo(
				long creditosAcreditadosPorBajaDurantePeriodo) {
			this.creditosAcreditadosPorBajaDurantePeriodo = creditosAcreditadosPorBajaDurantePeriodo;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		public long getCreditosDisponiblesInicioPeriodo() {
			return creditosDisponiblesInicioPeriodo;
		}
		public void setCreditosDisponiblesInicioPeriodo(long creditosDisponiblesInicioPeriodo) {
			this.creditosDisponiblesInicioPeriodo = creditosDisponiblesInicioPeriodo;
		}
		
	    public long getRetainedCredits() {
	        return this.retainedCredits;
	    }
        public void setRetainedCredits(Long retainedCredits) {
            this.retainedCredits = retainedCredits;
            
        }
        
        public long getTotalAvailableCredits() {
            return this.totalAvailableCredits;
        }
        
        public void setTotalAvailableCredits(
                long totalAvailableCredits) {
            this.totalAvailableCredits = totalAvailableCredits;
            
        }
		
	}
package org.dpi.stats;

import org.dpi.department.Department;

public class PeriodSummaryData {

        Department department;
        Department ministerioDeReparticion;

        private String year;
		private long creditosDisponiblesInicioPeriodo;
    	private long creditosAcreditadosPorBajaDurantePeriodo;
    	private long retainedCredits;
    	private long reassignedFromRetentionCredits;
    	//private long adjustmentsCredits;
    	private long totalAvailableCredits;
    	private long creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo;
    	private long creditosPorIngresosOAscensosOtorgadosPeriodo;
    	private long creditosDisponiblesSegunSolicitadoPeriodo;
    	private long creditosDisponiblesSegunOtorgadoPeriodo;
    	
    	private long totalCreditosReparticionAjustes_Debito_Periodo;
        private long totalCreditosReparticionAjustes_Credito_Periodo;
        private long totalCreditosReparticion_ReasignadosDeRetencion_Periodo ;
        private long totalCreditosReparticion_Reubicacion_Periodo;

    	

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
        
        public Department getDepartment() {
            return department;
        }
        
        public void setDepartment(Department department) {
            this.department = department;
        }
        public long getReassignedFromRetentionCredits() {
            return reassignedFromRetentionCredits;
        }
        public void setReassignedFromRetentionCredits(
                long reassignedFromRetentionCredits) {
            this.reassignedFromRetentionCredits = reassignedFromRetentionCredits;
        }
        /*
        public long getAdjustmentsCredits() {
            return adjustmentsCredits;
        }
        public void setAdjustmentsCredits(long adjustmentsCredits) {
            this.adjustmentsCredits = adjustmentsCredits;
        }*/

        public long getTotalCreditosReparticionAjustes_Debito_Periodo() {
            return totalCreditosReparticionAjustes_Debito_Periodo;
        }
        public void setTotalCreditosReparticionAjustes_Debito_Periodo(long totalCreditosReparticionAjustes_Debito_Periodo) {
            this.totalCreditosReparticionAjustes_Debito_Periodo = totalCreditosReparticionAjustes_Debito_Periodo;
        }
        public long getTotalCreditosReparticionAjustes_Credito_Periodo() {
            return totalCreditosReparticionAjustes_Credito_Periodo;
        }
        public void setTotalCreditosReparticionAjustes_Credito_Periodo(long totalCreditosReparticionAjustes_Credito_Periodo) {
            this.totalCreditosReparticionAjustes_Credito_Periodo = totalCreditosReparticionAjustes_Credito_Periodo;
        }
        
        public long getTotalCreditosReparticion_ReasignadosDeRetencion_Periodo() {
            return totalCreditosReparticion_ReasignadosDeRetencion_Periodo;
        }

        public void setTotalCreditosReparticion_ReasignadosDeRetencion_Periodo(Long totalCreditosReparticion_ReasignadosDeRetencion_Periodo) {
            this.totalCreditosReparticion_ReasignadosDeRetencion_Periodo = totalCreditosReparticion_ReasignadosDeRetencion_Periodo;
        }
        
        
        public long getTotalCreditosReparticion_Reubicacion_Periodo() {
            return totalCreditosReparticion_Reubicacion_Periodo;
        }
        public void setTotalCreditosReparticion_Reubicacion_Periodo(
                long totalCreditosReparticion_Reubicacion_Periodo) {
            this.totalCreditosReparticion_Reubicacion_Periodo = totalCreditosReparticion_Reubicacion_Periodo;
        }
        public Department getMinisterioDeReparticion() {
            return ministerioDeReparticion;
        }
        public void setMinisterioDeReparticion(Department ministerioDeReparticion) {
            this.ministerioDeReparticion = ministerioDeReparticion;
        }
		
	}
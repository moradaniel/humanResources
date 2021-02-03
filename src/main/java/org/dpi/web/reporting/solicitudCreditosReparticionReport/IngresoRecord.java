package org.dpi.web.reporting.solicitudCreditosReparticionReport;

public class IngresoRecord {
    String categoria;
    int cantidad;
    int creditosPorIngresoEnCategoriaUnitario;
    int totalCreditosIngresosEnCategoria;

    public IngresoRecord(String categoria, int cantidad,
            int creditosPorIngresoEnCategoriaUnitario,
            int totalCreditosIngresosEnCategoria) {
        super();
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.creditosPorIngresoEnCategoriaUnitario = creditosPorIngresoEnCategoriaUnitario;
        this.totalCreditosIngresosEnCategoria=totalCreditosIngresosEnCategoria;    
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCreditosPorIngresoEnCategoriaUnitario() {
        return creditosPorIngresoEnCategoriaUnitario;
    }

    public int getTotalCreditosIngresosEnCategoria() {
        return totalCreditosIngresosEnCategoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCreditosPorIngresoEnCategoriaUnitario(
            int creditosPorIngresoEnCategoriaUnitario) {
        this.creditosPorIngresoEnCategoriaUnitario = creditosPorIngresoEnCategoriaUnitario;
    }

    public void setTotalCreditosIngresosEnCategoria(
            int totalCreditosIngresosEnCategoria) {
        this.totalCreditosIngresosEnCategoria = totalCreditosIngresosEnCategoria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cantidad;
        result = prime * result
                + ((categoria == null) ? 0 : categoria.hashCode());
        result = prime * result + creditosPorIngresoEnCategoriaUnitario;
        result = prime * result + totalCreditosIngresosEnCategoria;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IngresoRecord other = (IngresoRecord) obj;
        if (cantidad != other.cantidad)
            return false;
        if (categoria == null) {
            if (other.categoria != null)
                return false;
        } else if (!categoria.equals(other.categoria))
            return false;
        if (creditosPorIngresoEnCategoriaUnitario != other.creditosPorIngresoEnCategoriaUnitario)
            return false;
        if (totalCreditosIngresosEnCategoria != other.totalCreditosIngresosEnCategoria)
            return false;
        return true;
    }
    
    
    

}

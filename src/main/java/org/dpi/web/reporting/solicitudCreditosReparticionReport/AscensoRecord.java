package org.dpi.web.reporting.solicitudCreditosReparticionReport;

import java.util.Objects;

public class AscensoRecord {

    String categoriaActual;
    String categoriaPropuesta;
    int cantidad;
    int creditosPorIngresoEnCategoriaUnitario;
    int totalCreditosIngresosEnCategoria;
    
    public AscensoRecord(String categoriaActual, String categoriaPropuesta,
            int cantidad, int creditosPorIngresoEnCategoriaUnitario,
            int totalCreditosIngresosEnCategoria) {
        super();
        this.categoriaActual = categoriaActual;
        this.categoriaPropuesta = categoriaPropuesta;
        this.cantidad = cantidad;
        this.creditosPorIngresoEnCategoriaUnitario = creditosPorIngresoEnCategoriaUnitario;
        this.totalCreditosIngresosEnCategoria = totalCreditosIngresosEnCategoria;
    }

    public String getCategoriaActual() {
        return categoriaActual;
    }

    public void setCategoriaActual(String categoriaActual) {
        this.categoriaActual = categoriaActual;
    }

    public String getCategoriaPropuesta() {
        return categoriaPropuesta;
    }

    public void setCategoriaPropuesta(String categoriaPropuesta) {
        this.categoriaPropuesta = categoriaPropuesta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCreditosPorIngresoEnCategoriaUnitario() {
        return creditosPorIngresoEnCategoriaUnitario;
    }

    public void setCreditosPorIngresoEnCategoriaUnitario(
            int creditosPorIngresoEnCategoriaUnitario) {
        this.creditosPorIngresoEnCategoriaUnitario = creditosPorIngresoEnCategoriaUnitario;
    }

    public int getTotalCreditosIngresosEnCategoria() {
        return totalCreditosIngresosEnCategoria;
    }

    public void setTotalCreditosIngresosEnCategoria(
            int totalCreditosIngresosEnCategoria) {
        this.totalCreditosIngresosEnCategoria = totalCreditosIngresosEnCategoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, categoriaActual, categoriaPropuesta,
                creditosPorIngresoEnCategoriaUnitario,
                totalCreditosIngresosEnCategoria);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AscensoRecord other = (AscensoRecord) obj;
        return cantidad == other.cantidad
                && Objects.equals(categoriaActual, other.categoriaActual)
                && Objects.equals(categoriaPropuesta, other.categoriaPropuesta)
                && creditosPorIngresoEnCategoriaUnitario == other.creditosPorIngresoEnCategoriaUnitario
                && totalCreditosIngresosEnCategoria == other.totalCreditosIngresosEnCategoria;
    }
    
    
    
}

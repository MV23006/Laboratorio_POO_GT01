package org.laboratorio2.dto;

public class ResumenFlotaDTO {
    private final int totalVehiculos;
    private final double costoTotalMantenimiento;
    private final double impuestoTotal;

    public ResumenFlotaDTO(int totalVehiculos, double costoTotalMantenimiento, double impuestoTotal) {
        this.totalVehiculos = totalVehiculos;
        this.costoTotalMantenimiento = costoTotalMantenimiento;
        this.impuestoTotal = impuestoTotal;
    }

    public int getTotalVehiculos() { return totalVehiculos; }
    public double getCostoTotalMantenimiento() { return costoTotalMantenimiento; }
    public double getImpuestoTotal() { return impuestoTotal; }

    @Override
    public String toString() {
        return "ResumenFlotaDTO{" +
                "totalVehiculos=" + totalVehiculos +
                ", costoTotalMantenimiento=" + costoTotalMantenimiento +
                ", impuestoTotal=" + impuestoTotal +
                '}';
    }
}
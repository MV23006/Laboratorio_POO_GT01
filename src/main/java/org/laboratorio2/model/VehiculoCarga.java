package org.laboratorio2.model;

public class VehiculoCarga extends Vehiculo {
    private final double capacidadToneladas;

    public VehiculoCarga(int id, String placa, double costoBase, double capacidadToneladas) {
        super(id, placa, costoBase);
        this.capacidadToneladas = capacidadToneladas;
    }

    public double getCapacidadToneladas() { return capacidadToneladas; }

    @Override
    public double calcularCostoMantenimiento() {
        return costoBase + (capacidadToneladas * 50.0);
    }
}
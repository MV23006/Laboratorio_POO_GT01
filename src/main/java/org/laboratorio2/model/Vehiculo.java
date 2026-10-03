package org.laboratorio2.model;

import java.util.Objects;

public abstract class Vehiculo {
    protected int id;
    protected String placa;
    protected double costoBase;

    public Vehiculo(int id, String placa, double costoBase) {
        this.id = id;
        this.placa = placa;
        this.costoBase = costoBase;
    }

    public Vehiculo() {

    }

    public int getId() { return id; }
    public String getPlaca() { return placa; }
    public double getCostoBase() { return costoBase; }

    public abstract double calcularCostoMantenimiento();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return Objects.equals(placa, vehiculo.placa);
    }

    public double calcularImpuesto() {
        return 0;
    }
}


package org.laboratorio2.config;

public class ConfiguracionFlota {
    private static ConfiguracionFlota instancia;
    private double tasaImpuesto;
    private String moneda;

    private ConfiguracionFlota() {
        this.tasaImpuesto = 0.12;
        this.moneda = "USD";
    }

    public static synchronized ConfiguracionFlota getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionFlota();
        }
        return instancia;
    }

    public double getTasaImpuesto() {
        return tasaImpuesto;
    }

    public void setTasaImpuesto(double tasaImpuesto) {
        this.tasaImpuesto = tasaImpuesto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
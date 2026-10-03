package org.laboratorio2.controller;

import org.laboratorio2.dto.ResumenFlotaDTO;
import org.laboratorio2.model.VehiculoCarga;
import org.laboratorio2.model.VehiculoPasajero;
import org.laboratorio2.service.GestorFlota;

public class Main {
    public static void main(String[] args) {
        String archivoJson = "flota_final.json";
        GestorFlota gestor = new GestorFlota();

        // 1. Agregar vehículos
        gestor.agregar(new VehiculoCarga(1, "C-101", 500.0, 10.0));
        gestor.agregar(new VehiculoCarga(2, "C-102", 600.0, 15.0));
        gestor.agregar(new VehiculoPasajero(3, "P-201", 300.0, 40));
        gestor.agregar(new VehiculoPasajero(4, "P-202", 350.0, 25));

        // 2. Guardar en JSON
        gestor.guardarEnJSON(archivoJson);

        // 3. Limpiar lista local
        gestor.obtenerTodos().clear();

        // 4. Cargar desde JSON
        gestor.cargarDesdeJSON(archivoJson);

        // 5. Imprimir reporte en consola
        ResumenFlotaDTO reporte = gestor.generarReporte();
        System.out.println("==========================================");
        System.out.println("       REPORTE GENERAL DE FLOTA");
        System.out.println("==========================================");
        System.out.println("Total de vehiculos registrados: " + reporte.getTotalVehiculos());
        System.out.println("Costo total de mantenimiento: $" + reporte.getCostoTotalMantenimiento());
        System.out.println("Impuesto total aplicado: $" + reporte.getImpuestoTotal());
        System.out.println("==========================================");
    }
}
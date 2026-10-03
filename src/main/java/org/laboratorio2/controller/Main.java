package org.laboratorio2.controller;

import org.laboratorio2.dto.ResumenFlotaDTO;
import org.laboratorio2.model.VehiculoCarga;
import org.laboratorio2.model.VehiculoPasajeros;
import org.laboratorio2.GestorFlota;

public class Main {
    public static void main() {
        main(null);
    }

    public static void main(String[] args) {
        String archivoJson = "flota_final.json";
        GestorFlota gestor = new GestorFlota();

        // 1. Agregar vehículos
        gestor.agregar(new VehiculoCarga(1, "C-101", 5000, 10));
        gestor.agregar(new VehiculoCarga(2, "C-102", 6000, 12));
        gestor.agregar(new VehiculoPasajeros(3, "P-201", 3000, 40));
        gestor.agregar(new VehiculoPasajeros(4, "P-202", 3500, 50));

        // 2. Guardar en JSON (esto creará el archivo nuevo con el campo tipoVehiculo)
        gestor.guardarEnJSON(archivoJson);

        // 3. Vaciar la lista en memoria (para comprobar que luego cargue del archivo)
        gestor = new GestorFlota(); // O el método que tengas para limpiar la lista

        // 4. Cargar desde JSON
        gestor.cargarDesdeJSON(archivoJson);

        // 5. Generar e imprimir reporte
        ResumenFlotaDTO reporte = gestor.generarReporte();
        System.out.println("====== RESUMEN DE FLOTA ======");
        System.out.println("Costo total mantenimiento: $" + reporte.getCostoTotalMantenimiento());
        System.out.println("Impuesto total aplicado: $" + reporte.getImpuestoTotal());
        System.out.println("==============================");
    }
}
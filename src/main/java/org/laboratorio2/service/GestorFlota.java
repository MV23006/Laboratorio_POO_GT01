package org.laboratorio2.service;

import com.google.gson.*;
import org.laboratorio2.config.ConfiguracionFlota;
import org.laboratorio2.dto.ResumenFlotaDTO;
import org.laboratorio2.model.Vehiculo;
import org.laboratorio2.model.VehiculoCarga;
import org.laboratorio2.model.VehiculoPasajero;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorFlota implements RepositorioGenerico<Vehiculo> {
    private List<Vehiculo> listaVehiculos = new ArrayList<>();

    private Gson crearGson() {
        JsonSerializer<Vehiculo> serializer = (src, typeOfSrc, context) -> {
            JsonObject jsonObj = context.serialize(src).getAsJsonObject();
            jsonObj.addProperty("tipoVehiculo", src.getClass().getSimpleName());
            return jsonObj;
        };

        JsonDeserializer<Vehiculo> deserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement tipoElem = jsonObject.get("tipoVehiculo");

            if (tipoElem == null) {
                throw new JsonParseException("Campo tipoVehiculo no encontrado en el JSON");
            }

            String tipo = tipoElem.getAsString();
            if ("VehiculoCarga".equalsIgnoreCase(tipo)) {
                return context.deserialize(jsonObject, VehiculoCarga.class);
            } else if ("VehiculoPasajero".equalsIgnoreCase(tipo) || "VehiculoPasajeros".equalsIgnoreCase(tipo)) {
                return context.deserialize(jsonObject, VehiculoPasajero.class);
            }
            throw new JsonParseException("Tipo de vehiculo desconocido: " + tipo);
        };

        return new GsonBuilder()
                .registerTypeAdapter(Vehiculo.class, serializer)
                .registerTypeAdapter(Vehiculo.class, deserializer)
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void agregar(Vehiculo elemento) {
        listaVehiculos.add(elemento);
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        return listaVehiculos;
    }

    @Override
    public void guardarEnJSON(String ruta) {
        Gson gson = crearGson();
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(listaVehiculos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cargarDesdeJSON(String ruta) {
        Gson gson = crearGson();
        try (FileReader reader = new FileReader(ruta)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            listaVehiculos.clear();
            for (JsonElement element : jsonArray) {
                Vehiculo vehiculo = gson.fromJson(element, Vehiculo.class);
                listaVehiculos.add(vehiculo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResumenFlotaDTO generarReporte() {
        int totalVehiculos = listaVehiculos.size();
        double costoTotalMantenimiento = 0.0;

        for (Vehiculo v : listaVehiculos) {
            costoTotalMantenimiento += v.calcularCostoMantenimiento();
        }

        double tasaImpuesto = ConfiguracionFlota.getInstance().getTasaImpuesto();
        double impuestoTotal = costoTotalMantenimiento * tasaImpuesto;

        return new ResumenFlotaDTO(totalVehiculos, costoTotalMantenimiento, impuestoTotal);
    }
}
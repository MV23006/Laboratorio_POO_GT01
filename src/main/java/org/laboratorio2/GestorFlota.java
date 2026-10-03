package org.laboratorio2;

import com.google.gson.*;
import org.laboratorio2.dto.ResumenFlotaDTO;
import org.laboratorio2.model.Vehiculo;
import org.laboratorio2.model.VehiculoCarga;
import org.laboratorio2.model.VehiculoPasajeros;
import org.laboratorio2.service.RepositorioGenerico;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestorFlota implements RepositorioGenerico<Vehiculo> {

    private List<Vehiculo> listaVehiculos = new ArrayList<>();

    private static Vehiculo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        // Busca primero 'tipoVehiculo' y si no existe busca 'tipo'
        JsonElement tipoElem = jsonObject.get("tipoVehiculo");
        if (tipoElem == null) {
            tipoElem = jsonObject.get("tipo");
        }

        if (tipoElem == null) {
            throw new JsonParseException("Campo tipoVehiculo no encontrado en el JSON");
        }

        String tipo = tipoElem.getAsString();
        if ("VehiculoCarga".equalsIgnoreCase(tipo)) {
            return context.deserialize(jsonObject, VehiculoCarga.class);
        } else if ("VehiculoPasajeros".equalsIgnoreCase(tipo) || "VehiculoPasajero".equalsIgnoreCase(tipo)) {
            return context.deserialize(jsonObject, VehiculoPasajeros.class);
        }

        throw new JsonParseException("Tipo de vehiculo desconocido: " + tipo);
    }

    private Gson crearGson() {
        JsonSerializer<Vehiculo> serializer = (src, typeOfSrc, context) -> {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("tipoVehiculo", src.getClass().getSimpleName());
            jsonObj.addProperty("id", src.getId());
            jsonObj.addProperty("placa", src.getPlaca());
            jsonObj.addProperty("costoBase", src.getCostoBase());

            if (src instanceof VehiculoCarga carga) {
                jsonObj.addProperty("capacidadToneladas", carga.getCapacidadToneladas());
            } else if (src instanceof VehiculoPasajeros pasajeros) {
                jsonObj.addProperty("numPasajeros", pasajeros.getNumPasajeros());
            }
            return jsonObj;
        };

        JsonDeserializer<Vehiculo> deserializer = GestorFlota::deserialize;
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

    /**
     * @param ruta
     */
    @Override
    public void cargarDesdeJSON(String ruta) {
        Gson gson = crearGson();
        try (FileReader reader = new FileReader(ruta)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                Vehiculo vehiculo = gson.fromJson(element, Vehiculo.class);
                if (vehiculo != null) listaVehiculos.add(vehiculo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public ResumenFlotaDTO generarReporte() {
    int total = (listaVehiculos != null) ? listaVehiculos.size() : 0;
    double costoMantenimiento = 0.0;
    double impuestoTotal = 0.0;

    if (listaVehiculos != null) {
        for (Vehiculo v : listaVehiculos) {
            if (v != null) {
                costoMantenimiento += v.calcularCostoMantenimiento();
                impuestoTotal += v.calcularImpuesto();
            }
        }
    }

    return new ResumenFlotaDTO(total, costoMantenimiento, impuestoTotal);
}

    }

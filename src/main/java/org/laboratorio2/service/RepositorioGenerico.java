package org.laboratorio2.service;

import java.util.List;

public interface RepositorioGenerico<T> {
    void agregar(T elemento);
    List<T> obtenerTodos();
    void guardarEnJSON(String ruta);
    void cargarDesdeJSON(String ruta);
}

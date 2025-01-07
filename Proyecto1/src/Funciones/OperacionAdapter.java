package Funciones;

import Funciones.Operaciones;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class OperacionAdapter implements JsonSerializer<Operaciones.Operacion> {
    @Override
    public JsonElement serialize(Operaciones.Operacion operacion, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonOperacion = new JsonObject();

        // Obtener los atributos de la operación
        String nombreOperacion = operacion.getNombre_operacion();
        List<String> leyesUtilizadas = operacion.getLeyes_utilizada();
        String conjuntoResultante = operacion.getConjunto_resultante();

        if (leyesUtilizadas == null || leyesUtilizadas.isEmpty()) {
            // Si no hay leyes utilizadas, indicar que no se puede simplificar
            jsonOperacion.addProperty(nombreOperacion, "No se puede simplificar la operación");
        } else {
            // Si hay leyes utilizadas, incluirlas junto con el conjunto simplificado
            JsonObject detallesOperacion = new JsonObject();
            detallesOperacion.add("leyes", context.serialize(leyesUtilizadas));
            detallesOperacion.addProperty("conjunto_simplificado", conjuntoResultante);

            jsonOperacion.add(nombreOperacion, detallesOperacion);
        }

        return jsonOperacion;
    }
}

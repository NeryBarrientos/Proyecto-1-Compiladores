package Funciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorConjuntos {
    private static GestorConjuntos instancia;
    private Map<String, Conjunto> conjuntos;

    public GestorConjuntos() {
        this.conjuntos = new HashMap<>();
    }

    public static GestorConjuntos getInstancia() {
        if (instancia == null) {
            instancia = new GestorConjuntos();
        }
        return instancia;
    }

    public void addConjunto(String nombreConjunto, char limiteInferior, char limiteSuperior) {
        Conjunto conjunto = new Conjunto(nombreConjunto, limiteInferior, limiteSuperior);
        conjuntos.put(nombreConjunto, conjunto);
    }
    // Metodo para obtener el objeto Conjunto a partir de un nombre
    public GestorConjuntos.Conjunto obtenerConjunto(String nombreConjunto) {
        Conjunto conjunto = conjuntos.get(nombreConjunto);
        if (conjunto != null) {
            // System.out.println("Conjunto: " + nombreConjunto);
            // System.out.println("Listado de caracteres: " + conjunto.getListadoCaracteres());
            return conjunto;
        } else {
            System.out.println("El conjunto no existe.");
        }
        return conjunto;
    }

    // Nuevo método addConjunto que recibe un nombre y un listado de valores
    public void addConjunto(String nombreConjunto, String listadoValores) {
        Conjunto conjunto = new Conjunto(nombreConjunto, listadoValores);
        conjuntos.put(nombreConjunto, conjunto);
    }
    
    // Metodo deleteConjunto que recibe un nombre y borra ese conjunto
    public void deleteConjunto(String nombreConjunto) {
        conjuntos.remove(nombreConjunto);
    }

    public Conjunto getConjunto(String nombreConjunto) {
        return conjuntos.get(nombreConjunto);
    }

    public void mostrarConjuntos() {
        for (Conjunto conjunto : conjuntos.values()) {
            conjunto.mostrarConjunto();
        }
    }

    // limpiar conjuntos
    public void limpiarConjuntos() {
        conjuntos.clear();
    }

    public static class Conjunto {
        private String nombreConjunto;
        private char limiteInferior;
        private char limiteSuperior;
        private List<Character> listadoCaracteres;

        // Constructor original
        public Conjunto(String nombreConjunto, char limiteInferior, char limiteSuperior) {
            this.nombreConjunto = nombreConjunto;
            this.limiteInferior = limiteInferior;
            this.limiteSuperior = limiteSuperior;
            this.listadoCaracteres = new ArrayList<>();
            generarListadoCaracteres();
        }

        // Nuevo constructor que acepta un listado de valores como String
        public Conjunto(String nombreConjunto, String listadoValores) {
            this.nombreConjunto = nombreConjunto;
            this.listadoCaracteres = new ArrayList<>();
            generarListadoCaracteresDesdeValores(listadoValores);
        }

        private void generarListadoCaracteres() {
            if (limiteInferior < 33 || limiteInferior > 126 || limiteSuperior < 33 || limiteSuperior > 126) {
                throw new IllegalArgumentException("Los límites deben estar en el rango ASCII de 33 a 126.");
            }
            if (limiteInferior > limiteSuperior) {
                throw new IllegalArgumentException("El límite inferior debe ser menor o igual al límite superior.");
            }

            for (char c = limiteInferior; c <= limiteSuperior; c++) {
                listadoCaracteres.add(c);
            }
        }

        // Método para generar listado de caracteres desde un String de valores
        private void generarListadoCaracteresDesdeValores(String listadoValores) {
            String[] valores = listadoValores.split("á"); // Dividir la cadena por comas
            for (String valor : valores) {
                valor = valor.trim(); // Eliminar espacios en blanco
                if (!valor.isEmpty()) {
                    // Convertir cada valor en un carácter y añadirlo a la lista
                    listadoCaracteres.add(valor.charAt(0));
                }
            }
        }

        public String getNombreConjunto() {
            return nombreConjunto;
        }

        public List<Character> getListadoCaracteres() {
            return listadoCaracteres;
        }

        public void mostrarConjunto() {
            System.out.println("Nombre del conjunto: " + nombreConjunto);
            System.out.println("Listado de caracteres: " + listadoCaracteres);
        }
    }
}

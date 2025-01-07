package Funciones;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import proyecto1.Proyecto1;

public class Operaciones {

    private static Operaciones instancia;
    public List<String> leyesUtilizadas = new ArrayList<>();
    public static List<String> elementos_resultantes = new ArrayList<>();
    public List<Operacion> operaciones = new ArrayList<>();
    public GestorConjuntos gestor = GestorConjuntos.getInstancia();
    public List<String> Conjuntos_temporales = new ArrayList<>();
    public Set<String> conjuntos_unicos = new HashSet<>();
    public LinkedHashMap<String, Area> lista = new LinkedHashMap<>();
    public LinkedHashMap<String, Area> Areas_originales = new LinkedHashMap<>();
    public int width = 600, height = 500;
    public int universeX = 0, universeY = 0, universeWidth = 600, universeHeight = 350;
    public String name1 = "", name2 = "", name3 = "", name4 = "";
    public String[] identificadoresArray;
    // Crear una imagen en memoria
    public BufferedImage imagen = new BufferedImage(600, 500, BufferedImage.TYPE_INT_ARGB);
    public Graphics2D g2d = imagen.createGraphics();

    // Método estático para obtener la instancia única
    public static Operaciones getInstancia() {
        if (instancia == null) {
            instancia = new Operaciones();
        }
        return instancia;
    }

    public class Operacion {

        private String nombre_operacion;
        private String conjunto_orignal;
        private String conjunto_resultante;
        private List<String> leyes_utilizada;
        private List<String> elementos_resultantes;
        private String ruta_imagen;

        public Operacion(String nombre_operacion, String conjunto_orignal) {
            this.nombre_operacion = nombre_operacion;
            this.conjunto_orignal = conjunto_orignal;
            this.conjunto_resultante = null;
            this.leyes_utilizada = null;
            this.elementos_resultantes = null;
            this.ruta_imagen = null;
        }

        public String getNombre_operacion() {
            return nombre_operacion;
        }

        public void setNombre_operacion(String nombre_operacion) {
            this.nombre_operacion = nombre_operacion;
        }

        public String getConjunto_orignal() {
            return conjunto_orignal;
        }

        public void setConjunto_orignal(String conjunto_orignal) {
            this.conjunto_orignal = conjunto_orignal;
        }

        public String getConjunto_resultante() {
            return conjunto_resultante;
        }

        public void setConjunto_resultante(String conjunto_resultante) {
            this.conjunto_resultante = conjunto_resultante;
        }

        public List<String> getLeyes_utilizada() {
            return leyes_utilizada;
        }

        public void setLeyes_utilizada(List<String> leyes_utilizada) {
            this.leyes_utilizada = leyes_utilizada;
        }

        public List<String> getElementos_resultantes() {
            return elementos_resultantes;
        }

        public void setElementos_resultantes(List<String> elementos_resultantes) {
            this.elementos_resultantes = elementos_resultantes;
        }

        public String getRuta_imagen() {
            return ruta_imagen;
        }

        public void setRuta_imagen(String ruta_imagen) {
            this.ruta_imagen = ruta_imagen;
        }
    }

    public void agregarOperacion(String nombre_operacion, String conjunto_orignal) {
        Operacion operacion = new Operacion(nombre_operacion, conjunto_orignal);
        operaciones.add(operacion);
    }

    public void borrarOperaciones() {
        operaciones.clear();
    }

    public void imprimirOperaciones() {
        for (Operacion operacion : operaciones) {
            System.out.println();
            System.out.println("Operación: " + operacion.getNombre_operacion());
            System.out.println("Conjunto original: " + operacion.getConjunto_orignal());
            System.out.println("Conjunto resultante: " + operacion.getConjunto_resultante());
            System.out.println("Leyes utilizadas: " + operacion.getLeyes_utilizada());
            System.out.println("Elementos resultantes: " + operacion.getElementos_resultantes());
            System.out.println("Ruta de la imagen: " + operacion.getRuta_imagen());
            System.out.println();
        }
    }

    // Funcion para obtener el objeto de una operacion por su indice
    public Operacion getOperacion_indice(int indice) {
        return operaciones.get(indice);
    }

    // Funcion para obtener el objeto de una operacion por su nombre
    public Operacion getOperacion(String nombre_operacion) {
        for (Operacion operacion : operaciones) {
            if (operacion.getNombre_operacion().equals(nombre_operacion)) {
                return operacion;
            }
        }
        return null;
    }

    // Funcion para hacer set conjunto resultante y leyes utilizadas de una operacion que recibe el nombre
    public void setConjuntoResultante(String nombre_operacion) {
        Operacion operacion = getOperacion(nombre_operacion);
        String conjunto_resultante = simplificar(operacion.getConjunto_orignal());
        conjunto_resultante = corregirParentesis(conjunto_resultante);
        operacion.setConjunto_resultante(conjunto_resultante);
        List<String> leyes_usadas = new ArrayList<>(leyesUtilizadas);
        operacion.setLeyes_utilizada(leyes_usadas);
        leyesUtilizadas.clear();
    }
// (^ ((^ pares) U (^ naturales)))

    public String simplificar(String conjunto) {
        String resultado = conjunto;
        boolean huboCambio;
        Set<String> historialAplicaciones = new HashSet<>();  // Guardará las expresiones generadas

        do {
            huboCambio = false;  // Reiniciar la bandera
            historialAplicaciones.add(resultado);  // Guardar la expresión original en el historial

            // // Ley de absorción: ((cA U cB) & cA) = A
            if (resultado.matches(".*\\(\\((.*?) U (.*?)\\) & \\1\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\(\\((.*?) U (.*?)\\) & \\1\\)", "$1");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Absorción");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                }
            }

            // // Ley de absorción: ((cA & cB) U cA) = A
            if (resultado.matches(".*\\((.*?) & (.*?) U \\1\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\((.*?) & (.*?) U \\1\\)", "$1");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Absorción");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                }
            }

            // Ley Distributiva: (^ (((^ pares) & (^ pares)) U ((^ naturales) & (^ naturales)))) = (^ ((^ (pares & pares)) U (^ (naturales & naturales))))
            if (resultado.matches(".*\\(\\^ \\(\\(\\^.*?\\) *& *\\(\\^.*?\\)\\) *U *\\(\\(\\^.*?\\) *& *\\(\\^.*?\\)\\)\\).*")) {
                // Cambiar la expresión regular para capturar los grupos correctamente con manejo de espacios opcionales
                String nuevoResultado = resultado.replaceAll(
                        "\\(\\^ *\\(\\(\\^ *([^()]+)\\) *& *\\(\\^ *([^()]+)\\)\\) *U *\\(\\(\\^ *([^()]+)\\) *& *\\(\\^ *([^()]+)\\)\\)\\)",
                        "(^ ((^ ($1 & $2)) U (^ ($3 & $4))))"
                );

                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Distributiva");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley de De Morgan: (^ ((^ A) & (^ B))) = (^ (^(A U B)))
            if (resultado.matches(".*\\(\\^ \\(\\(\\^.*?\\) *& *\\(\\^.*?\\)\\)\\).*")) {
                // Cambiar la expresión regular para capturar los grupos correctamente
                String nuevoResultado = resultado.replaceAll(
                        "\\(\\^ \\(\\(\\^ *([^()]+)\\) *& *\\(\\^ *([^()]+)\\)\\)\\)",
                        "(^ (^( $1 U $2 )))"
                );
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("De Morgan");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley de De Morgan: (^ ((^ A) U (^ B))) = (^ (^(A & B)))
            if (resultado.matches(".*\\(\\^ \\(\\(\\^.*?\\) *U *\\(\\^.*?\\)\\)\\).*")) {
                // Cambiar la expresión regular para capturar los grupos correctamente
                String nuevoResultado = resultado.replaceAll(
                        "\\(\\^ \\(\\(\\^ *([^()]+)\\) *U *\\(\\^ *([^()]+)\\)\\)",
                        "(^ (^( $1 & $2 )))"
                );
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("De Morgan");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley de Complemento: (^ (^ conjA)) = (conjA)
            if (resultado.matches(".*\\(\\^ \\(\\^.*?\\)\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\(\\^ \\(\\^(.*?)\\)\\)", "$1");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Complemento");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Aplicar idempotencia para este tipo tambien (((cA & cB) U cA) - cB) = (cA - cB)
            if (resultado.matches(".*\\(\\(\\(c(.*?) & c(.*?)\\) U c\\1\\) - c\\2\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\(\\(c(.*?) & c(.*?)\\) U c\\1\\) - c\\2", "c$1 - c$2");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Idempotencia");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley de idempotencia: A ∩ A = A
            if (resultado.matches(".*\\((.*?) & \\1\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\((.*?) & \\1\\)", "$1");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Idempotencia");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley de idempotencia: A U A = A
            if (resultado.matches(".*\\(\\s*([^()\\s]+)\\s*U\\s*\\1\\s*\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\(\\s*([^()\\s]+)\\s*U\\s*\\1\\s*\\)", "$1");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Idempotencia");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley distributiva: (A U B) & (A U C) = A U (B & C)
            if (resultado.matches(".*\\((.*?) U (.*?)\\) & \\(\\1 U (.*?)\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\((.*?) U (.*?)\\) & \\(\\1 U (.*?)\\)", "$1 U ($2 & $3)");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Distributiva");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

            // Ley distributiva: (A & B) U (A & C) = A & (B U C)
            if (resultado.matches(".*\\((.*?) & (.*?)\\) U \\(\\1 & (.*?)\\).*")) {
                String nuevoResultado = resultado.replaceAll("\\((.*?) & (.*?)\\) U \\(\\1 & (.*?)\\)", "$1 & ($2 U $3)");
                if (!nuevoResultado.equals(resultado) && !historialAplicaciones.contains(nuevoResultado)) {
                    resultado = nuevoResultado;
                    leyesUtilizadas.add("Distributiva");
                    historialAplicaciones.add(nuevoResultado);
                    huboCambio = true;
                    continue;  // Saltar a la siguiente iteración
                }
            }

        } while (huboCambio);  // Repetir hasta que no haya más cambios

        return resultado;
    }

    public String corregirParentesis(String expresion) {
        int balance = 0;
        StringBuilder resultadoCorregido = new StringBuilder();

        // Recorre la expresión para verificar el balance de los paréntesis
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            // Si es un paréntesis de apertura, incrementa el balance
            if (c == '(') {
                balance++;
                resultadoCorregido.append(c);
            } // Si es un paréntesis de cierre, verifica que esté balanceado
            else if (c == ')') {
                if (balance > 0) {
                    balance--;
                    resultadoCorregido.append(c);
                }
                // Si no hay balance, no se agrega este paréntesis de cierre
            } // Agregar otros caracteres normalmente
            else {
                resultadoCorregido.append(c);
            }
        }

        // Si al final hay paréntesis de apertura sin cerrar, eliminarlos
        String resultadoFinal = resultadoCorregido.toString();
        while (balance > 0) {
            int indice = resultadoFinal.lastIndexOf('(');
            if (indice != -1) {
                resultadoFinal = resultadoFinal.substring(0, indice) + resultadoFinal.substring(indice + 1);
                balance--;
            }
        }

        return resultadoFinal;
    }

    public void aplicarOperaciones(String Nombre_operacion) {
        conjuntos_unicos.clear();
        lista.clear();
        Areas_originales.clear();
        Operacion operacion = getOperacion(Nombre_operacion);
        String conjuntoOriginal = operacion.getConjunto_orignal();
        Pattern patternIdentificadores = Pattern.compile("\\b(?!U)\\w+\\b");
        Matcher matcherIdentificadores = patternIdentificadores.matcher(conjuntoOriginal);
        String resultadoFinal = operacion.getConjunto_resultante();

        Set<String> identificadoresUnicos = new HashSet<>();
        // Buscar todos los identificadores en la expresión
        while (matcherIdentificadores.find()) {
            identificadoresUnicos.add(matcherIdentificadores.group());
        }
        // Convertir los Sets a arreglos
        identificadoresArray = new String[identificadoresUnicos.size()];
        identificadoresArray = identificadoresUnicos.toArray(new String[0]);
        System.out.println("Identificadores únicos: " + Arrays.toString(identificadoresArray));
        Area area_total = new Area();
        Ellipse2D.Double[] ellipses = new Ellipse2D.Double[identificadoresArray.length];
        // Configuramos el anti-aliasing para mejor calidad de gráficos
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Dibujamos el rectángulo que representa el universo
        Rectangle2D.Double universo = new Rectangle2D.Double(universeX, universeY, universeWidth, universeHeight);
        g2d.setColor(Color.WHITE); // Fondo gris para el universo
        g2d.fill(universo);

        g2d.setColor(Color.BLACK); // Contorno negro para el universo
        g2d.draw(universo);

        if (identificadoresArray.length == 1) {
            // Coordenadas y dimensiones de los conjuntos A (dibujados como círculos)
            int xA = 100, yA = 100, radius = 150;
            // Creamos el conjunto A
            Ellipse2D.Double conjuntoA = new Ellipse2D.Double(xA, yA, radius, radius);
            // Creamos las áreas correspondientes a los conjuntos
            Area areaA = new Area(conjuntoA);
            Area uni = new Area(universo);
            Areas_originales.put(identificadoresArray[0], areaA);
            Areas_originales.put("Universo",uni);
            // Agregar conjuntoA a el array elipses
            ellipses[0] = new Ellipse2D.Double(xA, yA, radius, radius);
        } else if (identificadoresArray.length == 2) {
            // Coordenadas y dimensiones de los conjuntos A, B (dibujados como círculos)
            int xA = 150, yA = 100, radius = 150;
            int xB = 225, yB = 100;
            // Creamos los conjuntos A, B y 
            Ellipse2D.Double conjuntoA = new Ellipse2D.Double(xA, yA, radius, radius);
            Ellipse2D.Double conjuntoB = new Ellipse2D.Double(xB, yB, radius, radius);
            // Creamos las áreas correspondientes a los conjuntos
            Area areaA = new Area(conjuntoA);
            Area areaB = new Area(conjuntoB);
            Area uni = new Area(universo);
            Areas_originales.put(identificadoresArray[0], areaA);
            Areas_originales.put(identificadoresArray[1], areaB);
            Areas_originales.put("Universo",uni);
            // Agregar conjuntoA a el array elipses
            ellipses[0] = new Ellipse2D.Double(xA, yA, radius, radius);
            ellipses[1] = new Ellipse2D.Double(xB, yB, radius, radius);
        } 
        else if (identificadoresArray.length == 3) {
            // Coordenadas y dimensiones de los conjuntos A, B y C (dibujados como círculos)
            int xA = 150, yA = 100, radius = 150;
            int xB = 225, yB = 100;
            int xC = 190, yC = 170;
            // Creamos los conjuntos A, B y C
            Ellipse2D.Double conjuntoA = new Ellipse2D.Double(xA, yA, radius, radius);
            Ellipse2D.Double conjuntoB = new Ellipse2D.Double(xB, yB, radius, radius);
            Ellipse2D.Double conjuntoC = new Ellipse2D.Double(xC, yC, radius, radius);
            // Creamos las áreas correspondientes a los conjuntos
            Area areaA = new Area(conjuntoA);
            Area areaB = new Area(conjuntoB);
            Area areaC = new Area(conjuntoC);
            Area uni = new Area(universo);
            Areas_originales.put(identificadoresArray[0], areaA);
            Areas_originales.put(identificadoresArray[1], areaB);
            Areas_originales.put(identificadoresArray[2], areaC);
            Areas_originales.put("Universo",uni);
            // Agregar conjuntoA a el array elipses
            ellipses[0] = new Ellipse2D.Double(xA, yA, radius, radius);
            ellipses[1] = new Ellipse2D.Double(xB, yB, radius, radius);
            ellipses[2] = new Ellipse2D.Double(xC, yC, radius, radius);
        } else if (identificadoresArray.length == 4) {
            // Coordenadas y dimensiones de los conjuntos A, B , C y D (dibujados como círculos)
            int xA = 150, yA = 100, radius = 150;
            int xB = 225, yB = 100;
            int xC = 190, yC = 170;
            int xD = 225, yD = 170;
            // Creamos los conjuntos A, B , C y D
            Ellipse2D.Double conjuntoA = new Ellipse2D.Double(xA, yA, radius, radius);
            Ellipse2D.Double conjuntoB = new Ellipse2D.Double(xB, yB, radius, radius);
            Ellipse2D.Double conjuntoC = new Ellipse2D.Double(xC, yC, radius, radius);
            Ellipse2D.Double conjuntoD = new Ellipse2D.Double(xD, yD, radius, radius);
            // Creamos las áreas correspondientes a los conjuntos
            Area areaA = new Area(conjuntoA);
            Area areaB = new Area(conjuntoB);
            Area areaC = new Area(conjuntoC);
            Area areaD = new Area(conjuntoD);
            Area uni = new Area(universo);
            Areas_originales.put(identificadoresArray[0], areaA);
            Areas_originales.put(identificadoresArray[1], areaB);
            Areas_originales.put(identificadoresArray[2], areaC);
            Areas_originales.put(identificadoresArray[3], areaD);
            Areas_originales.put("Universo",uni);
            // Agregar conjuntoA a el array elipses
            ellipses[0] = new Ellipse2D.Double(xA, yA, radius, radius);
            ellipses[1] = new Ellipse2D.Double(xB, yB, radius, radius);
            ellipses[2] = new Ellipse2D.Double(xC, yC, radius, radius);
            ellipses[3] = new Ellipse2D.Double(xD, yD, radius, radius);
        }

        // Navegar a la carpeta deseada
        String userDirectoryPath = System.getProperty("user.dir");
        userDirectoryPath += "\\src\\Imagenes";
        // Verificar si el directorio existe, si no, crearlo
        File directorio = new File(userDirectoryPath);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado: " + userDirectoryPath);
            } else {
                System.out.println("No se pudo crear el directorio: " + userDirectoryPath);
                return;
            }
        }
        System.out.println("Directorio actual: " + userDirectoryPath);
        String nombre_archivo = Proyecto1.archivo;
        System.out.println("Nombre del archivo: " + nombre_archivo);
        String[] parts = nombre_archivo.split("\\\\");
        String nombreArchivo = parts[parts.length - 1];
        // eliminar su extensión
        nombreArchivo = nombreArchivo.replace(".ca", "");
        nombreArchivo = nombreArchivo + "_" + Nombre_operacion;
        System.out.println(nombreArchivo);
        // resultadoFinal = resultadoFinal.replace(" ", "");
        // Se realiza la obtencion de los conjuntos e imagenes
        List<String> conjuntoResultante = aplicarOperacionesRecursivas(resultadoFinal,area_total,ellipses,nombreArchivo);
        operacion.setElementos_resultantes(new ArrayList<>(conjuntoResultante));
        operacion.setRuta_imagen(userDirectoryPath + "\\" + nombreArchivo + ".png");
    }

    public List<String> aplicarOperacionesRecursivas(String resultadoSimplificado,Area area_total, Ellipse2D.Double[] elipses, String nombreArchivo) {
        List<String> conjuntoResultante = new ArrayList<>();
        GestorConjuntos gestor = GestorConjuntos.getInstancia();

        System.out.println("Iniciando aplicarOperacionesRecursivas con: " + resultadoSimplificado);

        // Mientras haya paréntesis internos
        while (resultadoSimplificado.contains("(")) {
            System.out.println("Detectado paréntesis, analizando...");

            int ultimoParentesisAbierto = resultadoSimplificado.lastIndexOf('('); // Buscar el más interno
            int primerParentesisCerrado = resultadoSimplificado.indexOf(')', ultimoParentesisAbierto);

            // Extraer la operación dentro de los paréntesis
            String operacionInterna = resultadoSimplificado.substring(ultimoParentesisAbierto + 1, primerParentesisCerrado);
            operacionInterna = operacionInterna.strip();

            System.out.println("Operación interna extraída: " + operacionInterna);

            // Dividir la operación en partes
            String[] partes = operacionInterna.split(" ");
            System.out.println("Partes de la operación: " + Arrays.toString(partes));

            List<String> resultadoOperacion;

            // Si la simplificacion es un solo elemento, solo se retorna el conjunto
            if (partes.length == 2 && partes[0].equals("^")) {
                // Negación (solo un conjunto)
                String conjunto = partes[1];
                System.out.println("Operación de negación detectada sobre el conjunto: " + conjunto);
                GestorConjuntos.Conjunto c = gestor.obtenerConjunto(conjunto);
                resultadoOperacion = operarConjuntos(c, null, "^");
                area_total = diferenciaSimetricaArea((Area) Areas_originales.get(conjunto).clone());
            } else if (partes.length == 3) {
                // Operación binaria (conj1 operador conj2)
                String conjunto1 = partes[0];
                String operador = partes[1];
                String conjunto2 = partes[2];
                System.out.println("Operación binaria detectada: " + conjunto1 + " " + operador + " " + conjunto2);
                GestorConjuntos.Conjunto c1 = gestor.obtenerConjunto(conjunto1);
                System.out.println("Conjunto 1: " + c1.getListadoCaracteres());
                GestorConjuntos.Conjunto c2 = gestor.obtenerConjunto(conjunto2);
                System.out.println("Conjunto 2: " + c2.getListadoCaracteres());
                resultadoOperacion = operarConjuntos(c1, c2, operador);
                area_total = operar_graficas((Area) Areas_originales.get(conjunto1).clone(),(Area) Areas_originales.get(conjunto2).clone(),operador);
            } else if (partes.length == 1) {
                // No existe operacion y es un solo conjunto
                String conjunto = partes[0];
                System.out.println("Es solo un conjunto: " + conjunto);
                GestorConjuntos.Conjunto c1 = gestor.obtenerConjunto(conjunto);
                resultadoOperacion = new ArrayList<>();
                for (Character c : c1.getListadoCaracteres()) {
                    resultadoOperacion.add(c.toString());
                }
                area_total = (Area) Areas_originales.get(conjunto).clone();
                generar_grafica(identificadoresArray, resultadoSimplificado, elipses, area_total, nombreArchivo);
            } else {
                System.out.println("Operación inválida detectada: " + operacionInterna);
                throw new IllegalArgumentException("Operación inválida: " + operacionInterna);
            }

            // Reemplazar la operación interna con un identificador temporal
            String identificadorTemporal = "TEMP" + ultimoParentesisAbierto;
            System.out.println("Reemplazando operación interna con identificador temporal: " + identificadorTemporal);
            resultadoSimplificado = resultadoSimplificado.substring(0, ultimoParentesisAbierto)
                    + identificadorTemporal
                    + resultadoSimplificado.substring(primerParentesisCerrado + 1);
            String resultado = resultadoOperacion.toString();
            String resultadoSinCorchetes = resultado.substring(1, resultado.length() - 1);
            String lista_valores = resultadoSinCorchetes.replace(", ", "á");
            // recorrer resultadosincorchetes

            // Guardar el resultado de la operación en un mapa temporal
            lista.put(identificadorTemporal, area_total);
            Areas_originales.put(identificadorTemporal, area_total);
            Conjuntos_temporales.add(identificadorTemporal);
            gestor.addConjunto(identificadorTemporal, lista_valores);
            // aqui se debe guardar la operacion en el gestor
            System.out.println("Conjunto temporal creado: " + resultadoSinCorchetes);
            System.out.println("Resultado de la operación: " + resultadoSinCorchetes);
            System.out.println("Expresión actualizada: " + resultadoSimplificado);

            // Si después de reemplazar la operación interna no quedan más paréntesis,
            // no necesitamos continuar recursivamente.
            if (!resultadoSimplificado.contains("(")) {
                System.out.println("No quedan más paréntesis, retornando resultado...");
                // recorer conjuntos temporales
                for (String conjunto : Conjuntos_temporales) {
                    gestor.deleteConjunto(conjunto);
                }
                for (String conjunto : lista.keySet()) {
                    Areas_originales.remove(conjunto);
                }
                area_total = lista.get(identificadorTemporal);
                System.out.println("Area total: " + area_total);
                Conjuntos_temporales.clear();
                for (Ellipse2D.Double elipse : elipses) {
                    System.out.println("Elipse: " + elipse );
                }
                generar_grafica(identificadoresArray, resultado, elipses, area_total, nombreArchivo);
                lista.clear();
                return resultadoOperacion;
            }
        }

        // Evaluar la expresión final ya sin paréntesis, si aún queda algo por evaluar
        if (resultadoSimplificado.contains("TEMP")) {
            System.out.println("Evaluando operación final sin paréntesis con: " + resultadoSimplificado);
            conjuntoResultante = aplicarOperacionesFinales(resultadoSimplificado);
        }
        g2d.setColor(Color.ORANGE);
        g2d.fill(lista.get("conjuntoA"));
        System.out.println("Resultado final de aplicarOperacionesRecursivas: " + conjuntoResultante);
        return conjuntoResultante;
    }

    public List<String> aplicarOperacionesFinales(String resultadoSimplificado) {
        List<String> conjuntoResultante = new ArrayList<>();
        GestorConjuntos gestor = GestorConjuntos.getInstancia();

        System.out.println("Iniciando aplicarOperacionesFinales con: " + resultadoSimplificado);

        // Condición si viene ^ solo tiene 1 conjunto
        for (int i = 0; i < resultadoSimplificado.length(); i++) {
            if (resultadoSimplificado.charAt(i) == ')') {
                for (int j = i; j >= 0; j--) {
                    if (resultadoSimplificado.charAt(j) == '(') {
                        String operacion = resultadoSimplificado.substring(j + 1, i);
                        operacion = operacion.strip();

                        System.out.println("Operación encontrada: " + operacion);
                        String[] partes = operacion.split(" ");
                        System.out.println("Partes de la operación: " + Arrays.toString(partes));

                        if (partes[0].equals("^")) {
                            System.out.println("Operación de negación detectada");
                            String conjunto1 = partes[1];
                            GestorConjuntos.Conjunto c1 = gestor.obtenerConjunto(conjunto1);
                            conjuntoResultante = operarConjuntos(c1, null, "^");
                            break;
                        } else {
                            System.out.println("Operación binaria detectada");
                            String conjunto1 = partes[0];
                            String operador = partes[1];
                            String conjunto2 = partes[2];

                            GestorConjuntos.Conjunto c1 = gestor.obtenerConjunto(conjunto1);
                            System.out.println("Conjunto 1: " + c1.getListadoCaracteres());
                            GestorConjuntos.Conjunto c2 = gestor.obtenerConjunto(conjunto2);
                            System.out.println("Conjunto 2: " + c2.getListadoCaracteres());
                            conjuntoResultante = operarConjuntos(c1, c2, operador);
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("Resultado final de aplicarOperacionesFinales: " + conjuntoResultante);
        return conjuntoResultante;
    }

    public void generar_grafica(String[] identificadoresArray, String resultadoFinal, Ellipse2D.Double[] conjuntos, Area area_total, String nombreArchivo) {
        System.out.println("Dibujando área total: " + area_total);
        System.out.println("Coordenadas de elipse: " + conjuntos[0].x + ", " + conjuntos[0].y);

        g2d.setColor(Color.ORANGE);
        g2d.fill(area_total);
        g2d.draw(area_total);
        
        for (int i=0; i<identificadoresArray.length; i++) {
            g2d.setColor(Color.BLACK);
            g2d.draw(conjuntos[i]);
            g2d.drawString(identificadoresArray[i], (int) conjuntos[i].x, (int) conjuntos[i].y);
        }
        g2d.drawString("Universo", universeX + 10, universeY + 20); // Etiqueta para el universo
        // Definir el directorio de salida
        String directorioSalida = "src/Imagenes";
        File directorio = new File(directorioSalida);
        // Crear la ruta completa del archivo
        // File archivo = new File(directorio, nombreArchivo);
        try {
            ImageIO.write(imagen, "png", new File(directorio,nombreArchivo+".png"));
            System.out.println("La imagen se ha guardado como "+ nombreArchivo +".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public List<String> operarConjuntos(GestorConjuntos.Conjunto conjunto1, GestorConjuntos.Conjunto conjunto2, String operacion) {
        if (operacion.equals("U")) {
            return union(conjunto1, conjunto2);
        } else if (operacion.equals("-")) {
            return diferencia(conjunto1, conjunto2);
        } else if (operacion.equals("&")) {
            return interseccion(conjunto1, conjunto2);
        } else if (operacion.equals("^")) {
            return diferenciaSimetrica(conjunto1);
        }
        return elementos_resultantes;
    }

    public Area operar_graficas(Area conjunto1, Area conjunto2, String operacion) {
        if (operacion.equals("U")) {
            return union_grafica(conjunto1, conjunto2);
        } else if (operacion.equals("-")) {
            return diferencia_grafica(conjunto1, conjunto2);
        } else if (operacion.equals("&")) {
            return interseccion_grafica(conjunto1, conjunto2);
        } else if (operacion.equals("^")) {
            return diferenciaSimetricaArea(conjunto1);
        }
        return new Area();
    }

    public List<String> union(GestorConjuntos.Conjunto conjunto1, GestorConjuntos.Conjunto conjunto2) {
        elementos_resultantes.clear();
        for (Character c : conjunto1.getListadoCaracteres()) {
            elementos_resultantes.add(c.toString());
        }
        for (Character c : conjunto2.getListadoCaracteres()) {
            if (!elementos_resultantes.contains(c.toString())) {
                elementos_resultantes.add(c.toString());
            }
        }
        return elementos_resultantes;
    }

    public Area union_grafica(Area conjunto1, Area conjunto2) {
        Area area_resultante = (Area) conjunto1.clone();
        area_resultante.add(conjunto2);
        return area_resultante;
    }

    public List<String> interseccion(GestorConjuntos.Conjunto conjunto1, GestorConjuntos.Conjunto conjunto2) {
        elementos_resultantes.clear();

        // Verificar si alguno de los dos conjuntos es vacío
        if (conjunto1.getListadoCaracteres().isEmpty() || conjunto2.getListadoCaracteres().isEmpty()) {
            return elementos_resultantes; // Devuelve la lista vacía
        }

        // Realizar la intersección
        for (Character c : conjunto1.getListadoCaracteres()) {
            if (conjunto2.getListadoCaracteres().contains(c)) {
                elementos_resultantes.add(c.toString());
            }
        }
        return elementos_resultantes;
    }

    public Area interseccion_grafica(Area conjunto1, Area conjunto2) {
        Area area_resultante = (Area) conjunto1.clone();
        area_resultante.intersect(conjunto2);
        return area_resultante;
    }
    

    public List<String> diferencia(GestorConjuntos.Conjunto conjunto1, GestorConjuntos.Conjunto conjunto2) {
        elementos_resultantes.clear();
        for (Character c : conjunto1.getListadoCaracteres()) {
            if (!conjunto2.getListadoCaracteres().contains(c)) {
                elementos_resultantes.add(c.toString());
            }
        }
        return elementos_resultantes;
    }

    public Area diferencia_grafica(Area conjunto1, Area conjunto2) {
        Area area_resultante = (Area) conjunto1.clone();
        area_resultante.subtract(conjunto2);
        return area_resultante;
    }

    public List<String> diferenciaSimetrica(GestorConjuntos.Conjunto conjunto1) {
        elementos_resultantes.clear();
        GestorConjuntos gestor = GestorConjuntos.getInstancia();
        // obtener conjunto universo
        GestorConjuntos.Conjunto universo = gestor.obtenerConjunto("Universo");
        for (Character c : universo.getListadoCaracteres()) {
            if (!conjunto1.getListadoCaracteres().contains(c)) {
                elementos_resultantes.add(c.toString());
            }
        }
        return elementos_resultantes;
    }

    public Area diferenciaSimetricaArea(Area area1) {
        Area universo_area = (Area) Areas_originales.get("Universo").clone();
        universo_area.subtract(area1);

        return universo_area;
    }

    public void evaluarOperaciones(String Nombre_operacion, String listado_valores) {
        Operacion operacion = getOperacion(Nombre_operacion);
        String[] partes = listado_valores.split("á");
        List<String> elementos = operacion.getElementos_resultantes();
        System.out.println("Evaluar: " + operacion.getNombre_operacion());
        Proyecto1.addTextToConsole("=====================================");
        Proyecto1.addTextToConsole("Evaluar: " + operacion.getNombre_operacion());
        Proyecto1.addTextToConsole("=====================================");
        for (String elemento : partes) {
            if (elementos.contains(elemento)) {
                Proyecto1.addTextToConsole(elemento + " -> exitoso");
                System.out.println(elemento + " -> exitoso");
            } else {
                Proyecto1.addTextToConsole(elemento + " -> fallido");
                System.out.println(elemento + " -> fallido");
            }
        }
    }

    public void evaluarOperaciones_virguillila(String nombre_operacion, String listado_valores) {
        List<Character> listadoCaracteres = new ArrayList<>();
        Operacion operacion = getOperacion(nombre_operacion);
        String[] partes = listado_valores.split("á");
        List<String> elementos = operacion.getElementos_resultantes();
        System.out.println("Evaluar: " + operacion.getNombre_operacion());
        Proyecto1.addTextToConsole("=====================================");
        Proyecto1.addTextToConsole("Evaluar: " + operacion.getNombre_operacion());
        Proyecto1.addTextToConsole("=====================================");
        if (partes[0].charAt(0) < 33 || partes[0].charAt(0) > 126 || partes[1].charAt(0) < 33 || partes[1].charAt(0) > 126) {
            throw new IllegalArgumentException("Los límites deben estar en el rango ASCII de 33 a 126.");
        }
        if (partes[0].charAt(0) > partes[1].charAt(0)) {
            throw new IllegalArgumentException("El límite inferior debe ser menor o igual al límite superior.");
        }

        for (char c = partes[0].charAt(0); c <= partes[1].charAt(0); c++) {
            listadoCaracteres.add(c);
        }
        for (Character c : listadoCaracteres) {
            if (elementos.contains(c.toString())) {
                Proyecto1.addTextToConsole(c + " -> exitoso");
                System.out.println(c + " -> exitoso");
            } else {
                Proyecto1.addTextToConsole(c + " -> fallido");
                System.out.println(c + " -> fallido");

            }
        }
    }

    public static void crearArchivoJson(List<Operacion> operaciones, String nombreArchivo) {
        // Configurar Gson para usar el adaptador personalizado
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Operacion.class, new OperacionAdapter())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        // Definir el directorio de salida
        String directorioSalida = "src/ArchivosSalida";
        File directorio = new File(directorioSalida);

        // Verificar si el directorio existe, si no, crearlo
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado: " + directorioSalida);
            } else {
                System.out.println("No se pudo crear el directorio: " + directorioSalida);
                return;
            }
        }

        // Crear la ruta completa del archivo
        File archivo = new File(directorio, nombreArchivo);

        // Usar OutputStreamWriter para especificar la codificación UTF-8
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            gson.toJson(operaciones, writer);
            System.out.println("Archivo JSON creado correctamente con UTF-8: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Funciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class funcionalidades {

    public static List<Tokens> tokens = new ArrayList<>();
    public static List<Errores> errores = new ArrayList<>();

    public static class Tokens {

        private String lexema;
        private String tipoToken;
        private int linea;
        private int columna;

        public Tokens(String lexema, String tipoToken, int linea, int columna) {
            this.lexema = lexema;
            this.tipoToken = tipoToken;
            this.linea = linea;
            this.columna = columna;
        }

        public String getLexema() {
            return lexema;
        }

        public void setLexema(String lexema) {
            this.lexema = lexema;
        }

        public String getTipoToken() {
            return tipoToken;
        }

        public void setTipoToken(String tipoToken) {
            this.tipoToken = tipoToken;
        }

        public int getLinea() {
            return linea;
        }

        public void setLinea(int linea) {
            this.linea = linea;
        }

        public int getColumna() {
            return columna;
        }

        public void setColumna(int columna) {
            this.columna = columna;
        }
    }

    public static void agregarToken(String lexema, String tipoToken, int linea, int columna) {
        Tokens token = new Tokens(lexema, tipoToken, linea, columna);
        tokens.add(token);
    }

    public static void borrarTokens() {
        tokens.clear();
    }

    public static void imprimirTokens() {
        for (Tokens token : tokens) {
            System.out.println("Lexema: " + token.getLexema()
                    + ", Tipo: " + token.getTipoToken()
                    + ", Línea: " + token.getLinea()
                    + ", Columna: " + token.getColumna());
        }
    }

    public static void guardarTokensEnHtml() {
        BufferedWriter writer = null;
        try {
            // Navegar a la carpeta deseada
            String userDirectoryPath = System.getProperty("user.dir");

            // Verificar si la carpeta "Reportes" existe, si no, crearla
            String directorioSalida = "src/Funciones/Reportes/";
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
            // Verificar si el archivo ReporteTokens.html existe, si no, crearlo
            File archivo = new File(userDirectoryPath + "/src/Funciones/Reportes/ReporteTokens.html");
            System.out.println("Ruta: " + archivo.getAbsolutePath());
            if (!archivo.exists()) {
                System.out.println("Creando archivo... ReporteTokens.html");
                // mensaje de creacion de archivo
                // JOptionPane.showMessageDialog(null,archivo.getAbsolutePath());
                // JOptionPane.showMessageDialog(null, "Se ha creado el archivo ReporteTokens.html", "Creación de archivo", JOptionPane.INFORMATION_MESSAGE);
                archivo.createNewFile();
            } else {
                System.out.println("El archivo ya existe");
            }

            // Crear el archivo HTML dentro de la carpeta "Tokens"
            File archivoHtml = new File(directorio +"/ReporteTokens.html");
            System.out.println("Ruta: " + archivoHtml.getAbsolutePath());

            // Sobrescribir el archivo HTML y crear la estructura de la tabla
            writer = new BufferedWriter(new FileWriter(archivoHtml));
            writer.write("<html><head><title>Reporte de Tokens</title>");
            writer.write("<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.0.3/dist/tailwind.min.css\" rel=\"stylesheet\"></head><body>");
            writer.write("<h1 class=\"text-2xl font-bold mb-4 text-center\" >Reporte de Tokens</h1>");
            writer.write("<table class=\"table-auto border-collapse border border-gray-500 w-full\">");
            writer.write("<thead><tr class=\"bg-blue-600 text-white\">");
            writer.write("<th class=\"border border-gray-600 p-2\">#</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Lexema</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Tipo</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Línea</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Columna</th>");
            writer.write("</tr></thead><tbody>");

            // Escribir los tokens en la tabla
            int index = 1; // Contador para el número de celda
            for (Tokens token : tokens) {
                writer.write("<tr class=\"text-center\">");
                writer.write("<td class=\"border border-gray-600 p-2\">" + index++ + "</td>"); // Número de celda
                writer.write("<td class=\"border border-gray-600 p-2\">" + token.getLexema() + "</td>");
                writer.write("<td class=\"border border-gray-600 p-2\">" + token.getTipoToken() + "</td>");
                writer.write("<td class=\"border border-gray-600 p-2\">" + token.getLinea() + "</td>");
                writer.write("<td class=\"border border-gray-600 p-2\">" + token.getColumna() + "</td>");
                writer.write("</tr>");
            }

            // Cerrar la tabla y el documento HTML
            writer.write("</tbody></table>");
            writer.write("</body></html>");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Nueva clase interna para manejar errores
    public static class Errores {

        private String tipo;
        private String descripcion;
        private int linea;
        private int columna;

        public Errores(String tipo, String descripcion, int linea, int columna) {
            this.tipo = tipo;
            this.descripcion = descripcion;
            this.linea = linea;
            this.columna = columna;
        }

        // Métodos getters y setters para Errores
        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public int getLinea() {
            return linea;
        }

        public void setLinea(int linea) {
            this.linea = linea;
        }

        public int getColumna() {
            return columna;
        }

        public void setColumna(int columna) {
            this.columna = columna;
        }
    }

    // Método para agregar un error a la lista de errores
    public static void agregarError(String tipo, String descripcion, int linea, int columna) {
        Errores error = new Errores(tipo, descripcion, linea, columna);
        errores.add(error);
    }

    // Método para borrar todos los errores de la lista
    public static void borrarErrores() {
        errores.clear();
    }

    // Método para imprimir todos los errores
    public static void imprimirErrores() {
        for (Errores error : errores) {
            System.out.println("Tipo: " + error.getTipo()
                    + ", Descripción: " + error.getDescripcion()
                    + ", Línea: " + error.getLinea()
                    + ", Columna: " + error.getColumna());
        }
    }

    // Método para guardar los errores en un archivo HTML
    public static void guardarErroresEnHtml() {
        BufferedWriter writer = null;
        try {
            // Navegar a la carpeta deseada
            String userDirectoryPath = System.getProperty("user.dir");

            // Verificar si la carpeta "Reportes" existe, si no, crearla
            String directorioSalida = "src/Funciones/Reportes/";
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
            // Verificar si el archivo ReporteErrores.html existe, si no, crearlo
            File archivo = new File(userDirectoryPath + "/src/Funciones/Reportes/ReporteErrores.html");
            System.out.println("Ruta: " + archivo.getAbsolutePath());
            if (!archivo.exists()) {
                System.out.println("Creando archivo... ReporteErrores.html");
                // mensaje de creacion de archivo
                // JOptionPane.showMessageDialog(null,archivo.getAbsolutePath());
                // JOptionPane.showMessageDialog(null, "Se ha creado el archivo ReporteErrores.html", "Creación de archivo", JOptionPane.INFORMATION_MESSAGE);
                archivo.createNewFile();
            } else {
                System.out.println("El archivo ya existe");
            }

            // Crear el archivo HTML dentro de la carpeta "Tokens"
            File archivoHtml = new File(directorio +"/ReporteErrores.html");
            System.out.println("Ruta: " + archivoHtml.getAbsolutePath());

            // Sobrescribir el archivo HTML y crear la estructura de la tabla
            writer = new BufferedWriter(new FileWriter(archivoHtml));
            writer.write("<html><head><title>Reporte de Errores</title>");
            writer.write("<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.0.3/dist/tailwind.min.css\" rel=\"stylesheet\"></head><body>");
            writer.write("<h1 class=\"text-2xl font-bold mb-4 text-center\" >Reporte de Errores</h1>");
            writer.write("<table class=\"table-auto border-collapse border border-gray-500 w-full\">");
            writer.write("<thead><tr class=\"bg-red-600 text-white\">");
            writer.write("<th class=\"border border-gray-600 p-2\">#</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Tipo</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Descripción</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Línea</th>");
            writer.write("<th class=\"border border-gray-600 p-2\">Columna</th>");
            writer.write("</tr></thead><tbody>");

            // Escribir los errores en la tabla
            int index = 1; // Contador para el número de celda
            for (Errores error : errores) {
                writer.write("<tr class=\"text-center\">");
                writer.write("<td class=\"border border-gray-600 p-2\">" + index++ + "</td>"); // Número de celda
                writer.write("<td class=\"border border-gray-600 p-2\">" + error.getTipo() + "</td>");
                writer.write("<td class=\"border border-gray-600 p-2\">" + error.getDescripcion() + "</td>");
                writer.write("<td class=\"border border-gray-600 p-2\">" + error.getLinea() + "</td>");
                writer.write("<td class=\"border border-gray-600 p-2\">" + error.getColumna() + "</td>");
                writer.write("</tr>");
            }

            // Cerrar la tabla y el documento HTML
            writer.write("</tbody></table>");
            writer.write("</body></html>");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

package proyecto1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import Analizadores.*;
import Funciones.funcionalidades;
import Funciones.Operaciones.Operacion;
import Funciones.GestorConjuntos;
import Funciones.Operaciones;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;

public class Proyecto1 {

    public static String archivo = "";
    public static JTextArea consoleTextArea = new JTextArea(10, 60);
    public static int contador = 0;
    // Crear label para el panel derecho
    public static JLabel rightLabel = new JLabel();
    // Label centrar de operaciones
    public static JLabel centerLabel = new JLabel();

    public static void main(String[] args) {
        GestorConjuntos gestor = GestorConjuntos.getInstancia();
        Operaciones operaciones = Operaciones.getInstancia();

        // Variable global para guardar el nombre del archivo
        JFileChooser accion = null;

        // Crear el marco principal (JFrame)
        JFrame frame = new JFrame("ConjAnalyzer");

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Configurar el tamaño del marco para que ocupe toda la pantalla
        frame.setSize(screenSize.width, screenSize.height);

        // Centrar el marco en la pantalla
        frame.setLocationRelativeTo(null);

        // Iniciar aplicacion maximizada
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Crear una barra de menú (JMenuBar)
        JMenuBar menuBar = new JMenuBar();

        // Crear el menú "Funcionalidades"
        JMenu menuFuncionalidades = new JMenu("Funcionalidades");

        // Crear los elementos del menú
        JMenuItem menuItemNuevoArchivo = new JMenuItem("Nuevo Archivo");
        JMenuItem menuItemAbrirArchivo = new JMenuItem("Abrir Archivo");
        JMenuItem menuItemGuardar = new JMenuItem("Guardar");
        JMenuItem menuItemGuardarComo = new JMenuItem("Guardar como");

        // Añadir los elementos al menú "Funcionalidades"
        menuFuncionalidades.add(menuItemNuevoArchivo);
        menuFuncionalidades.add(menuItemAbrirArchivo);
        menuFuncionalidades.add(menuItemGuardar);
        menuFuncionalidades.add(menuItemGuardarComo);

        // Añadir el menú "Funcionalidades" a la barra de menú
        menuBar.add(menuFuncionalidades);

        // Crear el menú "Herramientas"
        JMenu menuHerramientas = new JMenu("Herramientas");

        // Crear los elementos de "Herramientas"
        JMenuItem menuItemEjecutar = new JMenuItem("Ejecutar");

        // Añadir los elementos al menú "Herramientas"
        menuHerramientas.add(menuItemEjecutar);

        // Añadir el menú "Herramientas" a la barra de menú
        menuBar.add(menuHerramientas);

        // Crear el menú "Reportes"
        JMenu menuReportes = new JMenu("Reportes");

        // Crear los elementos de "Reportes"
        JMenuItem menuItemReporteTokens = new JMenuItem("Reporte de Tokens");
        JMenuItem menuItemReporteErrores = new JMenuItem("Reporte de Errores");

        // Añadir los elementos al menú "Reportes"
        menuReportes.add(menuItemReporteTokens);
        menuReportes.add(menuItemReporteErrores);

        // Añadir el menú "Reportes" a la barra de menú
        menuBar.add(menuReportes);

        // Establecer la barra de menú en el marco principal
        frame.setJMenuBar(menuBar);

        // Crear el panel principal donde se agregarán otros componentes
        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        // Crear el panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());

        // Crear Label para el panel de entrada
        JLabel inputLabel = new JLabel("Entrada:");
        inputPanel.add(inputLabel, BorderLayout.NORTH);

        // Crear el TextArea y envolverlo en un JScrollPane
        // TextArea inputTextArea = new TextArea(10, 60);
        // JScrollPane scrollPane = new JScrollPane(inputTextArea);
        // Crear el TextArea (ahora JTextArea) y envolverlo en un JScrollPane
        JTextArea inputTextArea = new JTextArea(10, 60); // Reemplaza TextArea por JTextArea
        JScrollPane scrollPane = new JScrollPane(inputTextArea);

        // Configuración adicional del JTextArea
        inputTextArea.setLineWrap(true);  // Permitir ajuste de línea
        inputTextArea.setWrapStyleWord(true);  // Ajuste de línea en palabras completas
        // Aumentar tamaño de la fuente
        inputTextArea.setFont(inputTextArea.getFont().deriveFont(16f));

        // Acción para abrir un archivo
        menuItemAbrirArchivo.addActionListener((var e) -> {
            try {
                // Obtener el path del directorio donde está el archivo .java
                String path = Proyecto1.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                File mainFileDirectory = new File(path).getParentFile().getParentFile().getParentFile();

                // Navegar a la carpeta deseada
                String userDirectoryPath = System.getProperty("user.dir");

                System.out.println("Current Directory = \"" + userDirectoryPath + "\\src\\ArchivosPrueba");
                // A

                // Inicializar el JFileChooser con la carpeta predeterminada
                JFileChooser fileChooser = new JFileChooser(userDirectoryPath + "\\src\\ArchivosPrueba");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Permite solo la selección de archivos

                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                        // Guardar el nombre del archivo en una variable global
                        archivo = selectedFile.getAbsolutePath();
                        System.out.println(archivo);
                        inputLabel.setText("Entrada: " + selectedFile.getName());
                        inputTextArea.setText("");  // Limpiar el área de texto
                        String line;
                        while ((line = reader.readLine()) != null) {
                            inputTextArea.append(line + "\n");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Acción para nuevo archivo
        menuItemNuevoArchivo.addActionListener((var e) -> {
            if (!inputTextArea.getText().isEmpty()) { // Verifica si el TextArea no está vacío
                File currentFile = new File(archivo);
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                    StringBuilder fileContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }

                    if (!inputTextArea.getText().equals(fileContent.toString())) { // Compara el contenido del TextArea con el archivo
                        int option = JOptionPane.showConfirmDialog(frame, "¿Desea guardar los cambios en el archivo?", "Nuevo Archivo", JOptionPane.YES_NO_CANCEL_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            // Guardar cambios en el archivo
                            try (FileWriter writer = new FileWriter(currentFile)) {
                                writer.write(inputTextArea.getText());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            inputLabel.setText("Entrada: "); // Reiniciar el Label
                            inputTextArea.setText(""); // Limpiar el TextArea
                            archivo = ""; // Limpiar Variable Archivo
                        } else if (option == JOptionPane.NO_OPTION) {
                            inputLabel.setText("Entrada: "); // Reiniciar el Label
                            inputTextArea.setText(""); // Limpiar el TextArea
                            archivo = ""; // Limpiar Variable Archivo
                        }
                    } else {
                        inputLabel.setText("Entrada: "); // Reiniciar el Label
                        inputTextArea.setText(""); // Limpiar el TextArea
                        archivo = ""; // Limpiar Variable Archivo
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Accion para Guardar el Archivo
        menuItemGuardar.addActionListener((var e) -> {
            if (archivo != null && !archivo.isEmpty()) { // Verifica si la variable archivo tiene un valor
                try {
                    String currentText = inputTextArea.getText();
                    String fileContent = new String(Files.readAllBytes(Paths.get(archivo)));

                    if (!currentText.equals(fileContent)) { // Verifica si el contenido ha cambiado
                        try (FileWriter writer = new FileWriter(archivo)) {
                            writer.write(currentText); // Guarda el contenido del TextArea en el archivo
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(frame, "Archivo guardado exitosamente.", "Guardar Archivo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "No hay cambios para guardar.", "Guardar Archivo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No hay un archivo abierto para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Accion para guardar como
        menuItemGuardarComo.addActionListener((var e) -> {
            if (inputTextArea.getText().isEmpty()) { // Verifica si el TextArea está vacío
                JOptionPane.showMessageDialog(frame, "No hay contenido para guardar.", "Guardar Como", JOptionPane.WARNING_MESSAGE);
            } else {
                try {

                    // Navegar a la carpeta deseada
                    String userDirectoryPath = System.getProperty("user.dir");

                    // Inicializar el JFileChooser con la carpeta predeterminada
                    JFileChooser fileChooser = new JFileChooser(userDirectoryPath + "\\src\\ArchivosPrueba");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Permite solo la selección de archivos

                    // Establecer la extensión por defecto
                    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return f.isDirectory() || f.getName().toLowerCase().endsWith(".ca");
                        }

                        @Override
                        public String getDescription() {
                            return "Archivos CA (*.ca)";
                        }
                    });

                    int option = fileChooser.showSaveDialog(frame);
                    if (option == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (!selectedFile.getAbsolutePath().endsWith(".ca")) {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".ca");
                        }

                        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(selectedFile), "UTF-8")) {
                            writer.write(inputTextArea.getText()); // Guarda el contenido del TextArea en el archivo
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        archivo = selectedFile.getAbsolutePath(); // Actualiza la ruta del archivo actual
                        inputLabel.setText("Entrada: " + selectedFile.getName()); // Actualiza el nombre del archivo en la interfaz
                        JOptionPane.showMessageDialog(frame, "Archivo guardado exitosamente.", "Guardar Como", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Acción para ejecutar el archivo
        menuItemEjecutar.addActionListener((var e) -> {
            try {
                gestor.limpiarConjuntos();
                operaciones.borrarOperaciones();
                gestor.addConjunto("Universo", '!', '~');
                consoleTextArea.setText("");  // Limpiar la consola
                // Obtener el texto del área de texto y pasarlo al lexer
                Reader sr = new StringReader(inputTextArea.getText());
                lex scanner = new lex(sr);

                // Crear y ejecutar el parser
                Parser parser = new Parser(scanner);
                parser.parse();
                JOptionPane.showMessageDialog(frame, "Archivo ejecutado exitosamente.", "Archivo Ejecutado", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Se ejecutó el archivo");
                // gestor.addConjunto("ConjuntoA", '#', ')');
                // gestor.addConjunto("ConjuntoB", '#', '%');
                gestor.mostrarConjuntos();
                operaciones.imprimirOperaciones();
                // variable archivo = C:\Users\Pc2\Desktop\Usac\SegundoSemestre2024\Compi1\OLC1_Proyecto1_201807086\Proyecto1\src\ArchivosPrueba\intermedio.ca
                // obtener el nombre del archivo
                String[] parts = archivo.split("\\\\");
                String nombreArchivo = parts[parts.length - 1];
                // eliminar su extensión
                nombreArchivo = nombreArchivo.replace(".ca", "");
                System.out.println(nombreArchivo);
                Operacion operacion_actual = operaciones.getOperacion_indice(contador);
                System.out.println(operacion_actual.getNombre_operacion());
                operaciones.crearArchivoJson(operaciones.operaciones, nombreArchivo + ".json");
                ImageIcon icon = new ImageIcon(operacion_actual.getRuta_imagen());
                rightLabel.setIcon(icon);
                centerLabel.setText(operacion_actual.getConjunto_resultante());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Acion para reporte de tokens
        menuItemReporteTokens.addActionListener((var e) -> {
            // Funciones.funcionalidades.imprimirTokens();
            Funciones.funcionalidades.guardarTokensEnHtml();
            // Abrir el archivo HTML en el navegador predeterminado
            try {
                // Navegar a la carpeta deseada
                String userDirectoryPath = System.getProperty("user.dir");

                // Definir el directorio de salida
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
                // Crear la ruta completa del archivo
                File htmlFile = new File(directorio, "/ReporteTokens.html");
                System.out.println(htmlFile.getAbsolutePath());
                // Inicializar el JFileChooser con la carpeta predeterminada
                // File htmlFile = new File(userDirectoryPath + "\\src\\Funciones\\Reportes\\ReporteTokens.html");
                java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "Archivo Reportes Generado Exitosamente.", "Reporte Tokens", JOptionPane.INFORMATION_MESSAGE);
        });

        // Acción para reporte de errores
        menuItemReporteErrores.addActionListener((var e) -> {
            // Funciones.funcionalidades.imprimirErrores();
            Funciones.funcionalidades.guardarErroresEnHtml();
            // Abrir el archivo HTML en el navegador predeterminado
            try {
                // Navegar a la carpeta deseada
                String userDirectoryPath = System.getProperty("user.dir");

                // Verificar si el directorio de reportes existe, si no, crearlo
                File reportesDirectory = new File(userDirectoryPath + "\\src\\Funciones\\Reportes");
                if (!reportesDirectory.exists()) {
                    reportesDirectory.mkdir();
                }

                // Inicializar el JFileChooser con la carpeta predeterminada
                File htmlFile = new File(userDirectoryPath + "\\src\\Funciones\\Reportes\\ReporteErrores.html");
                java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "Archivo Reportes Generado Exitosamente.", "Reporte Errores", JOptionPane.INFORMATION_MESSAGE);
        });

        // Añadir el JScrollPane al inputPanel
        inputPanel.add(scrollPane, BorderLayout.CENTER);

        // Crear un segundo panel (derecho) para otros componentes
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Añadir el label al panel derecho
        rightPanel.add(rightLabel, BorderLayout.NORTH);
        // rightPanel.setBackground(Color.ORANGE);

        // Crear panel inferior para el botón "anterior", label central y botón "siguiente"
        JPanel bottomPanel_image = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(20, 20, 20, 20);  // Margen de 10 en todos los lados
        // bottomPanel_image.setBackground(Color.ORANGE);

        // Botón "Anterior"
        JButton previousButton = new JButton("Anterior");
        gbc.gridx = 0;  // Posición en la columna
        gbc.gridy = 0;  // Posición en la fila
        gbc.anchor = GridBagConstraints.WEST;  // Alinear a la izquierda
        gbc.fill = GridBagConstraints.NONE;  // No expandi
        gbc.ipadx = 10;  // Aumentar el tamaño del botón
        bottomPanel_image.add(previousButton, gbc);

        // Accion para el boton anterior
        previousButton.addActionListener((var e) -> {
            if (contador > 0) {
                contador--;
                Operacion operacion_actual = operaciones.getOperacion_indice(contador);
                ImageIcon icon = new ImageIcon(operacion_actual.getRuta_imagen());
                rightLabel.setIcon(icon);
                System.out.println(operacion_actual.getConjunto_resultante());
                centerLabel.setText(operacion_actual.getConjunto_resultante());
            } else {
                // Obtener tamaño de la lista de operaciones
                int tamaño = operaciones.operaciones.size();
                System.out.println(tamaño);
                if (tamaño > 0) {
                    contador = tamaño - 1;
                    Operacion operacion_actual = operaciones.getOperacion_indice(contador);
                    ImageIcon icon = new ImageIcon(operacion_actual.getRuta_imagen());
                    rightLabel.setIcon(icon);
                    System.out.println(operacion_actual.getConjunto_resultante());
                    centerLabel.setText(operacion_actual.getConjunto_resultante());
                }
            }

        });

        // Label central
        gbc.gridx = 2;  // Siguiente columna
        gbc.anchor = GridBagConstraints.CENTER;  // Centrar el label
        gbc.fill = GridBagConstraints.NONE;  // No expandi
        bottomPanel_image.add(centerLabel, gbc);

        // Botón "Siguiente"
        JButton nextButton = new JButton("Siguiente");
        gbc.gridx = 4;  // Siguiente columna
        gbc.anchor = GridBagConstraints.EAST;  // Alinear a la derecha
        gbc.fill = GridBagConstraints.NONE;  // No expandi
        bottomPanel_image.add(nextButton, gbc);

        // Accion para el boton siguiente
        nextButton.addActionListener((var e) -> {
            // Obtener tamaño de la lista de operaciones
            int tamaño = operaciones.operaciones.size();
            System.out.println(tamaño);
            if (contador < tamaño - 1) {
                contador++;
                Operacion operacion_actual = operaciones.getOperacion_indice(contador);
                ImageIcon icon = new ImageIcon(operacion_actual.getRuta_imagen());
                rightLabel.setIcon(icon);
                System.out.println(operacion_actual.getConjunto_resultante());
                centerLabel.setText(operacion_actual.getConjunto_resultante());
            } else {
                contador = 0;
                Operacion operacion_actual = operaciones.getOperacion_indice(contador);
                ImageIcon icon = new ImageIcon(operacion_actual.getRuta_imagen());
                rightLabel.setIcon(icon);
                System.out.println(operacion_actual.getConjunto_resultante());
                centerLabel.setText(operacion_actual.getConjunto_resultante());
            }
        });

        // Añadir el panel inferior al panel derecho
        rightPanel.add(bottomPanel_image, BorderLayout.SOUTH);

        // Crear un JSplitPane horizontal para dividir la pantalla en dos partes
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, rightPanel);
        horizontalSplitPane.setDividerLocation(screenSize.width / 2); // Establecer la posición inicial del divisor

        // Crear un panel vacío para la parte inferior de la pantalla
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Crear el TextArea para la "consola" y envolverlo en un JScrollPane
        JScrollPane consoleScrollPane = new JScrollPane(consoleTextArea);

        // Configuración adicional del JTextArea para que actúe como una consola
        consoleTextArea.setLineWrap(true);  // Permitir ajuste de línea
        consoleTextArea.setWrapStyleWord(true);  // Ajuste de línea en palabras completas
        consoleTextArea.setEditable(false);  // Hacer que no se pueda editar directamente
        // Aumentar tamaño de la fuente
        consoleTextArea.setFont(consoleTextArea.getFont().deriveFont(16f));

        // Añadir el JScrollPane que contiene la consola al bottomPanel
        bottomPanel.add(consoleScrollPane, BorderLayout.CENTER);

        // Crear un JSplitPane vertical para dividir la pantalla en parte superior e inferior
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, bottomPanel);
        verticalSplitPane.setDividerLocation(screenSize.height / 2); // Establecer la posición inicial del divisor

        // Añadir el JSplitPane vertical al mainPanel
        mainPanel.add(verticalSplitPane, BorderLayout.CENTER);

        // Configurar el comportamiento al cerrar la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hacer visible la ventana
        frame.setVisible(true);
    }

    // Método para añadir texto a la consola
    public static void addTextToConsole(String text) {
        consoleTextArea.append(text + "\n");  // Agregar el texto con una nueva línea
        consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());  // Desplazar hacia el final
    }
}

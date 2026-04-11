package com.compilador.compilador;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CompiladorController {

    @FXML public CodeArea editorCodigo;
    @FXML public TextArea panelErrores;
    @FXML public TableView<filaTabla> tablaSimbolos;
    @FXML public TableColumn<filaTabla, String> colSegmento;
    @FXML public TableColumn<filaTabla, String> colTipo;

    @FXML
    public void initialize() {
        tablaSimbolos.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colSegmento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSegmento()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));

        editorCodigo.textProperty().addListener((obs, oldText, newText) -> {
            if (aplicandoEstilo) return;
            Platform.runLater(() -> {
                aplicandoEstilo = true;
                resaltar(newText);
                aplicandoEstilo = false;
            });
        });
    }

    @FXML
    public void abrirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo KScript");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Codigo de KScript", "*.txt"));
        File archivo = fileChooser.showOpenDialog(new Stage());
        if (archivo != null) {
            try {
                String contenido = new String(
                        java.nio.file.Files.readAllBytes(archivo.toPath())
                );
                editorCodigo.replaceText(contenido);
                panelErrores.setText("Archivo cargado: " + archivo.getName());
            } catch (Exception e) {
                panelErrores.setText("Error al leer el archivo: " + e.getMessage());
            }
        }
    }

    public boolean aplicandoEstilo = false;

    public String[] palabraReservadas = {
            "inicio","fin","entero","flotante","cadena","si",
            "sino","mientras","para","imprime","leer","funcion","retorna",
            "verdadero","falso"};

    public String[] error = {
            "ERROR", "error", "Error"
    };

    public boolean comparador(String palabra){
        for (int i=0; i<palabraReservadas.length; i++){
            if (palabraReservadas[i].equals(palabra))
                return true;
        }
        return false;
    }

    //resaltado
    public void resaltar(String texto){
        if (texto.isEmpty()) return;
        editorCodigo.setStyle(0, texto.length(), java.util.Collections.emptyList());
        int i = 0;
        while (i < texto.length()){
            if (Character.isLetter(texto.charAt(i))){
                int inicio = i;
                while (i < texto.length() && Character.isLetter(texto.charAt(i))){
                    i++;
                }
                String palabra = texto.substring(inicio,i);
                if (comparador(palabra)){
                    editorCodigo.setStyle(inicio, i, java.util.Collections.singleton("keyword"));
                }
            }

            else {
                i++;
            }
        }
    }

    @FXML
    public void compilarButton() {
        tablaDeSimbolos tabsim = new tablaDeSimbolos();
        try {
            tabsim.crearTabla(palabraReservadas);
            tokenizador tok = new tokenizador(palabraReservadas);
            ArrayList<String[]> tokens = tok.token(editorCodigo.getText());
            ObservableList<filaTabla> datos = FXCollections.observableArrayList();
            //toma los tokens del editor
            for (int i=0; i<tokens.size();i++){
                datos.add(new filaTabla(tokens.get(i)[0], tokens.get(i)[1]));
            }
            tablaSimbolos.setItems(datos);
            panelErrores.setText("Compilación exitosa. Tokens generados: " + tokens.size());

            movTokenDat(tokens);

            error(tokens); //llamar al error
        } catch (IOException e) {
            panelErrores.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void salir() {
        System.exit(0);
    }

    public void movTokenDat(ArrayList<String[]> tokens) {
        tablaDeSimbolos tabsim = new tablaDeSimbolos();
        for (int i = 0; i < tokens.size(); i++) {
            String segmento = tokens.get(i)[0];
            String token = tokens.get(i)[1];
            try{
                tabsim.agregarToken(segmento, token);
            } catch (IOException e) {
                panelErrores.setText("Error al agregar token: " + e.getMessage());
            }

        }
    }

    public void error(ArrayList<String[]> tokens){
        StringBuilder error = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i)[1].equals("Desconocido")) {
                error.append("Error: Token desconocido '").append(tokens.get(i)[0]).append("'\n");
            }
        }
        panelErrores.setText(error.toString());
    }


}
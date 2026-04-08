package com.compilador.compilador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class CompiladorController {


    @FXML private CodeArea editorCodigo;
    @FXML private TextArea panelErrores;

    @FXML
    private void abrirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo KScript");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Codigo de KScript", "*.txt")
        );
        File archivo = fileChooser.showOpenDialog(new Stage());
        if (archivo != null) {
            panelErrores.setText("Archivo cargado: " + archivo.getName());
        }
    }

    public boolean aplicandoEstilo = false;

    @FXML
    public void initialize() {
        editorCodigo.textProperty().addListener((obs, oldText, newText) -> {

            if (aplicandoEstilo) return;

            Platform.runLater(() -> {
                aplicandoEstilo = true;
                resaltar(newText);
                aplicandoEstilo = false;
            });
        });
    }

    public String[] palabraReservadas = {
            "inicio","fin","entero","flotante","cadena","si",
            "sino","mientras","para","imprime","leer","funcion","retorna",
            "verdadero","falso"};

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
            } else {
                i++;
            }
        }
    }

    @FXML
    private void compilar() {
        panelErrores.setText("Compilando...");
    }

    @FXML
    private void salir() {
        System.exit(0);
    }
}
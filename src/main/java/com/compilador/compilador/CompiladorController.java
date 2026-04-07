package com.compilador.compilador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class CompiladorController {

    @FXML private TextArea editorCodigo;
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

    @FXML
    private void compilar() {
        panelErrores.setText("Compilando...");
    }

    @FXML
    private void salir() {
        System.exit(0);
    }
}
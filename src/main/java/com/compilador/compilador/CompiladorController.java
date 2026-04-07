package com.compilador.compilador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CompiladorController {

    @FXML private TextArea editorCodigo;
    @FXML private TextArea panelErrores;

    @FXML
    private void abrirArchivo() {

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
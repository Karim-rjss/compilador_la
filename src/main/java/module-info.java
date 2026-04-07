module com.compilador.compilador {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.compilador.compilador to javafx.fxml;
    exports com.compilador.compilador;
}
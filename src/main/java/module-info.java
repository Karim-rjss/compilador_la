module com.compilador.compilador {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;


    opens com.compilador.compilador to javafx.fxml;
    exports com.compilador.compilador;
}
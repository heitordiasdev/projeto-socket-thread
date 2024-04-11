module com.example.projetosd {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens br.com.catolica.projetosd to javafx.fxml;
    exports br.com.catolica.projetosd;
}
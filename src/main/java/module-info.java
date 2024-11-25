module com.proj.messengerserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.proj.messengerserver.client to javafx.fxml;
    exports com.proj.messengerserver.client;
    exports com.proj.messengerserver.server;
    opens com.proj.messengerserver.server to javafx.fxml;
}
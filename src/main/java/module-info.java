module com.projex.javafx_chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.projex.javafx_chat.server to javafx.fxml;
    exports com.projex.javafx_chat.server;
    exports com.projex.javafx_chat.client;
    opens com.projex.javafx_chat.client to javafx.fxml;
}
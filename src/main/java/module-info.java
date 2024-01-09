module com.example.pepejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.pepejavafx to javafx.fxml;
    exports com.example.pepejavafx;
    exports com.example.pepejavafx.filters;
    opens com.example.pepejavafx.filters to javafx.fxml;
}
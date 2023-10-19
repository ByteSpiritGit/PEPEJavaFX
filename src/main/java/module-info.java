module com.example.pepejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pepejavafx to javafx.fxml;
    exports com.example.pepejavafx;
}
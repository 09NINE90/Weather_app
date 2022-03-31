module com.example.kostya {
    requires javafx.controls;
    requires javafx.fxml;
    requires jsoup;


    opens com.example.weather to javafx.fxml;
    exports com.example.weather;
}
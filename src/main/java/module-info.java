module lab.lab.lab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens lab to javafx.fxml;
    exports lab;
}
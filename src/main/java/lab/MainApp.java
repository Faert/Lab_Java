package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.net.Socket;
import java.net.InetAddress;


public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("MainPlate.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("IvlevLab");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public static void main(String[] args) {
        /*
        try {
            Socket socket = new Socket("ya.ru", 80);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }*/

        launch();
    }
}
package PrimaryMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("WelcomeMenu.fxml"));
            Scene sc = new Scene(root);
            primaryStage.setScene(sc);
            primaryStage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void stop(){
//        decryptor.stopDecrypt();
    }


    public static void main(String[] args) {
        launch(Main.class);
    }
}

package PrimaryMenu;

import UBoat.UBoatMachineMenuController;
import UBoat.UBoatToServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UBoatRegistrationMenuController implements Initializable {




    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private ImageView enigmaTitle;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField uBoatName;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));
    }

    public void nextBtnClicked(ActionEvent event) throws IOException {
        boolean success = UBoatToServer.register(event, uBoatName.getText());

        if (success) {
            switchToUBoat(event);
        }

        else {
            uBoatName.setText("");
            uBoatName.setPromptText("Name already exits...");
        }
    }


    public void switchToUBoat(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader((UBoatMachineMenuController.class).getResource("UBoatMachineMenu.fxml"));
        root = loader.load();
        UBoatMachineMenuController uBoatMachineMenuController = loader.getController();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle(uBoatName.getText());
        stage.show();
    }
}

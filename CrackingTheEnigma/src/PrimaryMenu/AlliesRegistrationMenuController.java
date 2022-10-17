package PrimaryMenu;

import Allies.AlliesDashboardMenuController;
import PrimaryMenu.WelcomeMenuController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlliesRegistrationMenuController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ListView<String> battlefieldsListView;

    @FXML
    private Label battleFieldChosen;

    @FXML
    private ImageView enigmaTitle;

    @FXML
    private AnchorPane anchorPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        anchorPane.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));

        for (String battlefield : WelcomeMenuController.getBattlefields()){
            battlefieldsListView.getItems().add(battlefield);
        }

        battlefieldsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String currentBattlefield = battlefieldsListView.getSelectionModel().getSelectedItem();

                battleFieldChosen.setText(currentBattlefield);
            }
        });

    }


}

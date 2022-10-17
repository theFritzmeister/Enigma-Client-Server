package PrimaryMenu;

import Allies.AlliesDashboardMenuController;
import UBoat.UBoatMachineMenuController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;


public class WelcomeMenuController implements Initializable {

    private OkHttpClient client = new OkHttpClient();

    private static final List<String> battlefields = new ArrayList<String>();

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private ImageView enigmaTitle;
    @FXML
    private TextArea txtBox;

    @FXML
    private RadioButton agentRadioBtn;

    @FXML
    private RadioButton alliesRadioBtn;

    @FXML
    private RadioButton uBoatRadioBtn;

    @FXML
    private AnchorPane anchorPane;


    public static List<String> getBattlefields() {
        return battlefields;
    }

    public void uBoatChecked(javafx.event.ActionEvent actionEvent) {
        if (alliesRadioBtn.isSelected()){
            alliesRadioBtn.setSelected(false);
        }

        if (agentRadioBtn.isSelected()){
            agentRadioBtn.setSelected(false);
        }

        uBoatRadioBtn.setSelected(true);
    }

    public void alliesChecked(javafx.event.ActionEvent actionEvent) {
        if (uBoatRadioBtn.isSelected()){
            uBoatRadioBtn.setSelected(false);
        }

        if (agentRadioBtn.isSelected()){
            agentRadioBtn.setSelected(false);
        }

        alliesRadioBtn.setSelected(true);
    }

    public void agentChecked(javafx.event.ActionEvent actionEvent) {
        if (alliesRadioBtn.isSelected()){
            alliesRadioBtn.setSelected(false);
        }

        if (uBoatRadioBtn.isSelected()){
            uBoatRadioBtn.setSelected(false);
        }

        agentRadioBtn.setSelected(true);
    }


    public void nextBtnClicked(ActionEvent actionEvent) throws IOException {

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/")
                .build();

        Response response = client.newCall(request).execute();
        txtBox.setText(response.body().string());

        if (uBoatRadioBtn.isSelected()){
            registerAsUBoat(actionEvent);
        }

        if (alliesRadioBtn.isSelected()){
            registerAsAllies(actionEvent);
        }

        if (agentRadioBtn.isSelected()){
            switchToAgent(actionEvent);
        }
    }

    private void registerAsUBoat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader((getClass()).getResource("UBoatRegistrationMenu.fxml"));
        root = loader.load();
        UBoatRegistrationMenuController uBoatRegistrationMenuController = loader.getController();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private void registerAsAllies(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader((getClass()).getResource("AlliesRegistrationMenu.fxml"));
        root = loader.load();
        AlliesRegistrationMenuController alliesRegistrationMenuController = loader.getController();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();

        switchToAllies(event); //?
    }

    private void switchToAllies(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader((AlliesDashboardMenuController.class).getResource("AlliesDashboardMenu.fxml"));
        root = loader.load();
        AlliesDashboardMenuController alliesDashboardMenuController = loader.getController();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void switchToAgent(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader((UBoatMachineMenuController.class).getResource("UBoatMachineMenu.fxml"));
//        root = loader.load();
//        UBoatMachineMenuController uBoatMachineMenuController = loader.getController();
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root, 600, 400);
//        stage.setScene(scene);
//        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));
    }




//    public void sanityClicked(ActionEvent actionEvent) throws IOException {
//
//        Request request = new Request.Builder()
//                .url("http://127.0.0.1:8080/")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        txtBox.setText(response.body().string());
//    }
}


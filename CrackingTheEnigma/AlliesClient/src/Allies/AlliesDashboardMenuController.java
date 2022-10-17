package Allies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlliesDashboardMenuController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane anchorPaneBottom;

    @FXML
    private AnchorPane anchorPaneTop;

    @FXML
    private ImageView enigmaTitle;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPaneTop.setStyle("-fx-background-color: #fdf5e6");
        anchorPaneBottom.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));
    }

    public void switchToContestMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlliesContestMenu.fxml"));
        root = loader.load();
        AlliesContestMenuController uBoatContestMenuController = loader.getController();
        double contestMenuWidth = ((Node)actionEvent.getSource()).getParent().getScene().getWidth();
        double contestMenuHeight = ((Node)actionEvent.getSource()).getParent().getScene().getHeight();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, contestMenuWidth, contestMenuHeight);
        stage.setScene(scene);
        stage.show();
    }
}

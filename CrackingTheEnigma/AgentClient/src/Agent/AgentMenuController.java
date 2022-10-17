package Agent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AgentMenuController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView enigmaTitle;

    @FXML
    private AnchorPane anchorPaneBottom;

    @FXML
    private AnchorPane anchorPaneTop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPaneTop.setStyle("-fx-background-color: #fdf5e6");
        anchorPaneBottom.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));
    }
}

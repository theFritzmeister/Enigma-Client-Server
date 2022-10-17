package UBoat;

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
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UBoatContestMenuController implements Initializable {

    public static String fileName;

    public static String encryptionMessage;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView enigmaTitle;

    @FXML
    private AnchorPane anchorPaneBottom;

    @FXML
    private AnchorPane anchorPaneTop;

    @FXML
    private HBox rotorsGraphics;

    @FXML
    private TextField message;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPaneTop.setStyle("-fx-background-color: #fdf5e6");
        anchorPaneBottom.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));




//        setRotorsGraphics(machineCurrentRotorsState);
    }

    void setRotorsGraphics(String letters) {

        rotorsGraphics.getChildren().clear();

        for (int i=0; i<letters.length(); i++){
            String imgName;

            if (letters.charAt(i) < 'A' || letters.charAt(i) > 'Z'){
                imgName = String.valueOf((int)letters.charAt(i));
            }
            else {
                imgName = String.valueOf(letters.charAt(i));
            }

            Image rotor = new Image("resources/" + imgName + ".png");
            ImageView rotorv = new ImageView(rotor);
            rotorv.setFitHeight(35);
            rotorv.setFitWidth(40);

            rotorsGraphics.getChildren().add(rotorv);
        }
    }


    public void loadXmlBtnListener(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Resource File");
        File f = fc.showOpenDialog(null);
        fileName = f.getPath();
    }

    public void switchToMachineMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UBoatMachineMenu.fxml"));
        root = loader.load();
        UBoatMachineMenuController uBoatMachineMenuController = loader.getController();
        double machineMenuWidth = ((Node)actionEvent.getSource()).getParent().getScene().getWidth();
        double machineMenuHeight = ((Node)actionEvent.getSource()).getParent().getScene().getHeight();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, machineMenuWidth, machineMenuHeight);
        stage.setScene(scene);
        stage.show();
    }

    public void dictionaryBtnListener(ActionEvent actionEvent) throws IOException {
        if (UBoatData.enigmaMachineExists) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dictionary.fxml"));
            Parent root = loader.load();
            DictionaryController dictionaryController = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dictionary");
            stage.showAndWait();
        }
    }

    public void readyBtnListener(ActionEvent actionEvent) {

        if (!UBoatData.calibrationCodeIsSet) {
            message.setPromptText("Set calibration code first... ");
        }

        else {
            encryptionMessage = message.getText();
            if (!isAllInDictionary(encryptionMessage.toUpperCase())) {
                message.setText("");
                message.setPromptText("Message not in dictionary");
            }

            else {
                try {
//                    messageAfterEncryption = ControllerToEnigma.sendMessage(enigmaMachine, encryptionMessage);
//                    encryptedMessage.setText(messageAfterEncryption + "   " + enigmaMachine.getCode());
                } catch (Exception e) {
                    message.setText("");
                    message.setPromptText(e.getMessage());
                }
            }
        }
    }

    private boolean isAllInDictionary(String text){
        List<String> words = Arrays.asList(text.split(" "));
        int matches = 0;
        for (String word: words) {
//            if (decryptor.getDictionary().isInDictionary(word)){
//                matches += 1;
//            }
        }
        return matches  == words.size();

    }
}

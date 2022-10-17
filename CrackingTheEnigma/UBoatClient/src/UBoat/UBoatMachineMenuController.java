package UBoat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class UBoatMachineMenuController implements Initializable {

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
    private TextField calibrationCodeTxt;

    @FXML
    private TextField xmlPath;

    @FXML
    private TextArea machineDetailsTxt;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPaneTop.setStyle("-fx-background-color: #fdf5e6");
        anchorPaneBottom.setStyle("-fx-background-color: #fdf5e6");
        enigmaTitle.setImage(new Image("resources/EnigmaTitle.png"));

        machineDetailsTxt.setText(UBoatData.machineDetails);
        calibrationCodeTxt.setText(UBoatData.machineCalibrationCode);
    }

    public void loadXmlBtnListener(javafx.event.ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Resource File");
        File xmlFile = fc.showOpenDialog(null);
        String filePath = xmlFile.getPath();

        if (!filePath.endsWith(".xml")) {
            xmlPath.setText("");
            xmlPath.setPromptText("Not an XML file...");
        }

        try {
            xmlPath.setText(filePath);
            calibrationCodeTxt.setText("");
            String response = UBoatToServer.sendXml(filePath);
            UBoatData.machineDetails = response;
            machineDetailsTxt.setText(response);
            System.out.println(response);

        }
        catch (Exception ex) {
            xmlPath.setText(ex.getMessage());
        }
    }


    public void SetCodeAutomaticallyBtnListener(javafx.event.ActionEvent actionEvent) {
        String response = UBoatToServer.setCalibrationCodeAuto();
        calibrationCodeTxt.setText(response);

        if(UBoatData.calibrationCodeIsSet) {
            UBoatData.machineCalibrationCode = response;
        }

//        ControllerToEnigma.setMachineAutomatically(enigmaMachine);
//        calibrationCodeOfMachine = enigmaMachine.getCode();
//        calibrationCode.setText(calibrationCodeOfMachine);
//        SetRotorsGraphics(enigmaMachine.getCurrentRotorsState());
    }

    public void SetCodeManuallyBtnListener(javafx.event.ActionEvent actionEvent) {

        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SetCodeManually.fxml"));
            Parent root = loader.load();
            SetCodeManuallyController controller = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Set Machine Code Manually");
            stage.showAndWait();

//            ControllerToEnigma.setMachineManually(enigmaMachine);
//            String _calibrationCode = enigmaMachine.getCode();
//            calibrationCode.setText(_calibrationCode);

        } catch (Exception e) {
            calibrationCodeTxt.setText(e.getMessage());

        }
    }

    public void switchToContestMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UBoatContestMenu.fxml"));
        root = loader.load();
        UBoatContestMenuController uBoatContestMenuController = loader.getController();
        double contestMenuWidth = ((Node)actionEvent.getSource()).getParent().getScene().getWidth();
        double contestMenuHeight = ((Node)actionEvent.getSource()).getParent().getScene().getHeight();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, contestMenuWidth, contestMenuHeight);
        stage.setScene(scene);
        stage.show();
    }
}

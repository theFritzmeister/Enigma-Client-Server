package UBoat;

import com.sun.org.apache.bcel.internal.generic.SWITCH;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetCodeManuallyController {

    static int[] rotors;
    static char[] startingPoints;
    static String reflector;

    @FXML
    private Button SetCodeButton;


    @FXML
    private TextField Reflector;

    @FXML
    private TextField Rotors;

    @FXML
    private TextField StratingPoints;

    @FXML
    void SetCodeButtonListener(ActionEvent event) throws Exception {
        String rotorsText = Rotors.getText();

        rotors = RotorsToInt(rotorsText);

        startingPoints = StratingPoints.getText().toUpperCase().toCharArray();

        reflector = MakeRoman(Reflector.getText().toUpperCase().trim().toUpperCase());

        Stage stage = (Stage)SetCodeButton.getScene().getWindow();
        stage.close();
    }

    private int[] RotorsToInt(String rotors)
    {
        String[] rotsStr = rotors.split(",");
        int[] rots = new int[rotsStr.length];

        for (int i=0; i< rots.length; i++)
        {
            rots[i] = Integer.parseInt(rotsStr[rotsStr.length-1-i]);
        }

        return rots;
    }

    private static String MakeRoman(String num) throws Exception{
        String res = null;
        switch (num) {
            case "1":
                res = "I";
                break;
            case "2":
                res = "II";
                break;
            case "3":
                res = "III";
                break;
            case "4":
                res = "IV";
                break;
            case "5":
                res = "V";
                break;
            default:
                throw new Exception("Illegal Reflector...");
        }

        return res;
    }

}

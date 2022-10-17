package UBoat;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import okhttp3.*;

import javax.activation.MimetypesFileTypeMap;
import javax.print.attribute.standard.Media;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UBoatToServer {

    private static OkHttpClient client = new OkHttpClient();
    private  static String uBoatName;

    public static boolean register(ActionEvent event, String name) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("uBoatName", name)
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/UBoatLogin")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        System.out.println(res);
        uBoatName = name;
        if (res == "") {
            return false;
        }

        else return true;
    }

    public static String sendXml(String filePath) throws IOException {

        MediaType xmlMediaType = MediaType.parse("text/plain");
        byte[] xmlFileInBytes = Files.readAllBytes(Paths.get(filePath));
        RequestBody body = RequestBody.create(xmlFileInBytes, xmlMediaType);

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/UBoat?uBoatName=" + uBoatName)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if(response.isSuccessful()){
                UBoatData.enigmaMachineExists = true;
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

       return "";
    }


    public static String setCalibrationCodeAuto(){
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/UBoat/setAuto?uBoatName=" + uBoatName)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                UBoatData.calibrationCodeIsSet = true;
                return response.body().string();
            }

        } catch (IOException e) {
            return e.getMessage();
        }
        return "";
    }
}

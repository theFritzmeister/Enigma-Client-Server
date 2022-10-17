package com.example.crackingtheenigmaserver;

//import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
public class GameManager {

   ServerData gameDB = new ServerData();

    @GetMapping("/")
    public String sanityCheck(@RequestParam(value = "battlefieldID", defaultValue = "0") String battlefieldID){
        return gameDB.getEnigmaInfo(Integer.parseInt(battlefieldID));
    }

    @GetMapping("/UBoat/setAuto")
    public String setMachineCodeAutomatically(@RequestParam(value = "uBoatName", defaultValue = "") String uBoatName){
        return gameDB.setMachineAutomatically(uBoatName);
    }

    @PostMapping("/UBoatLogin")
    public String registerUBoat(@RequestParam(value = "uBoatName", defaultValue = "") String uBoatName){

        if (gameDB.isUBoatNameAvailable(uBoatName)){
            UBoat uBoat = new UBoat(uBoatName);
            gameDB.initializeBattleField(uBoat);
            return "registered";
        }

        return "";
    }

    @PostMapping( value = "/UBoat", consumes = "text/plain")//MediaType.APPLICATION_XML
    public String uploadXml( @RequestParam String uBoatName, @RequestBody String xmlFile) {
        try {
            return gameDB.setUBoatsXmlFile(xmlFile, uBoatName);
        } catch (Exception e) {
            return e.getMessage();
        }
    }










}

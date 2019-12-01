package Server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ServerController {

    //saves password against userName
    public Hashtable<String, String> passWordList = new Hashtable<>();

    //saves password+reportername against match+date
    public Hashtable<String,String>reporterData = new Hashtable<>();

    //saves socket against userName
    public Hashtable<String, Socket> mapList = new Hashtable<>();

    //saves match against matchName
    public Hashtable<String,String[]> matchData = new Hashtable<>();


    ObjectOutputStream outputStream;

    //observableList for Clients
    ObservableList<String> observableListC = FXCollections.observableArrayList();
    @FXML
    public ListView<String> clientList = new ListView<>(observableListC);

    //observableList for Matches
    ObservableList<String> observableListM = FXCollections.observableArrayList();
    @FXML
    public ListView<String> matchList = new ListView<>(observableListM);


    //Called when update button is pressed . sends all updated data to all clients
    void update(String[] match ){
        //sets the matchName on server list , ignores if already set
        if(!observableListM.contains(match[0])){
            observableListM.add(match[0]);
        }
        matchList.setItems(observableListM);

        //saves it
        matchData.put(match[0],match);
        //sends it
        sendData(match);
    }

    //sends updated data to all clients . called within update() func
    void sendData(String[] match ){
        String name;
        Socket socket;
        try{
            for(int i = 0; i< observableListC.size(); i++) {
                name = observableListC.get(i);
                socket = mapList.get(name);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(match);
            }
        }catch(Exception e){
            System.out.println("Error sending msg");
        }
    }

    //called from server thread when a client is first connected
    void addClient(String[] clientData,Socket socket){
        //if its a reporter
        if(clientData[0].equals("reporter")){
            String s[] = new String[1];

            if(reporterData.contains(clientData[2])&&reporterData.get(clientData[1]).equals(clientData[2]))
            {
                s[0] = "ok";
                observableListM.add(clientData[1]);
                matchList.setItems(observableListM);

                clientData[0] = clientData[1];
                clientData[1] = "Not started yet !";

                update(clientData);
                matchData.put(clientData[0],clientData);
                new Reader(socket,this);
            }
            else s[0] = "no";

            try {
                //write it on the client stream and close it
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(s);

                if(s[0].equals("no"))socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //client wants to disconnect
        else if(clientData[1].equals("no")){
            try {
                //close the previous socket
                mapList.get(clientData[0]).close();
                //close the current socket used to send this data
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket");
            }
            //remove client data , but keep the userName and password
            mapList.remove(clientData[0]);
            observableListC.remove(clientData[0]);
            clientList.setItems(observableListC);

        }
        //if the userName already exists
        else if(observableListC.contains(clientData[0])){
            String s[] = new String[1];
            s[0] = "UserName already exists !!";

            try {
                //write it on the client stream and close it
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(s);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //if the password is incorrect
        else if(passWordList.contains(clientData[0])&&!passWordList.get(clientData[0]).equals(clientData[1])){
            String s[] = new String[1];
            s[0] = "Password Incorrect !!";

            try {
                //write it on the client stream and close it
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(s);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //if everything is ok
        else {
            //save the userName vs password
            passWordList.put(clientData[0],clientData[1]);

            //sends ok to the client
            try {
                String s[] = new String[1];
                s[0] = "ok";
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(s);
            }catch(Exception e){

            }

            //adds client to clientList , client vs socket
            observableListC.add(clientData[0]);
            mapList.put(clientData[0], socket);
            clientList.setItems(observableListC);

            //send this new client all ongoing match data
            Enumeration<String>names = matchData.keys();
            try{
                while(names.hasMoreElements()){
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(matchData.get(names.nextElement()));
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }


    //deletes a match from server and also clients
    @FXML
    void deleteMatch(){
        String match[] = new String[2];
        match[0] = matchList.getSelectionModel().getSelectedItem();
        //if no match is selected
        if(match[0] == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning !!");
            alert.setHeaderText("No match Selected");
            alert.setContentText("You didn't select any match name");
            alert.showAndWait();
        }
        else{
            //remove every data of it
            observableListM.remove(match[0]);
            matchData.remove(match);
            matchList.setItems(observableListM);

            match[1] = "delete";
            sendData(match);
        }
    }

    @FXML
    TextField match,date,reporter,password;
    @FXML
    Label warning;

    @FXML
    void addMatch()
    {
        String s = match.getText()+" "+date.getText();
        String r = reporter.getText()+password.getText();
        if(reporterData.contains(s))warning.setText("Already added !!");
        else{
            reporterData.put(s,r);
            System.out.println(s+" "+r);
        }

        match.clear();date.clear();
        reporter.clear();password.clear();
    }
}

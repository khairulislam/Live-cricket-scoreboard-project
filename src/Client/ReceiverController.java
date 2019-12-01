package Client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ReceiverController {

    public Hashtable<String,String[]> matchData = new Hashtable<>();

    ObservableList<String> observableListM = FXCollections.observableArrayList();
    ObservableList<String> observableListSM = FXCollections.observableArrayList();

    @FXML
    public ListView<String> matchList = new ListView<>(observableListM);
    @FXML
    public ListView<String> selectedMatchList = new ListView<>(observableListSM);

    ClientMain clientMain;
    Socket socket;

    @FXML
    public TextArea description;



    //disconnects from server . it creates a new connection with server , sends clientName and status-"no" and returns connection page
    public void disconnect() {
            try {
                Socket socket = new Socket("127.0.0.1",33333);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                clientMain.clientData[1] = "no";
                outputStream.writeObject(clientMain.clientData);

                clientMain.showConnectionPage();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    void setMatchList(String[] match){
        //if server says to delete this match
        if(match[1].equals("delete")){
            //if the deleted match was being viewed
            if(match[0].equals(selectedMatchList.getSelectionModel().getSelectedItem())){
                description.clear();
            }

            //remove data of that match
            observableListM.remove(match[0]);
            observableListSM.remove(match[0]);
            matchData.remove(match[0]);
        }
        else{
            //if already on matchList
            if(!observableListM.contains(match[0]))observableListM.add(match[0]);
            //save the new data
            matchData.put(match[0],match);

            //if the updated match is already selected and being viewed by client this will update that data
            String name = selectedMatchList.getSelectionModel().getSelectedItem();
            if(name!=null&&name.equals(match[0])){
                description.setText(match[1]);
            }
        }
        //update the list
        matchList.setItems(observableListM);
    }

    @FXML
    void selection(){
        matchList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String>selected = matchList.getSelectionModel().getSelectedItems();
        for(int i=0;i<selected.size();i++){
            String s =selected.get(i);
            System.out.println(s);
            if(!observableListSM.contains(s))observableListSM.add(s);
        }
        selectedMatchList.setItems(observableListSM);
    }

    @FXML
    void showMatchDetails(){
        String name = selectedMatchList.getSelectionModel().getSelectedItem();
        if(name!=null){
            String match[] = matchData.get(name);
            description.setText(match[1]);
        }
    }

    void setClientMain(ClientMain clientMain, Socket socket)
    {
        this.socket = socket;
        this.clientMain = clientMain;
    }
}

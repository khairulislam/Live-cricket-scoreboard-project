package Client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ClientController {

    ClientMain clientMain;

    String clientData[]=new String[2];

    @FXML
    private TextField nameField,passwordField;

    @FXML
    Label warning;

    @FXML
    void connect() {
        //collects the name and password
        clientData[0] = nameField.getText();
        clientData[1] = passwordField.getText();
        //if password incorrect or userName already exists
        if(clientData[0].equals("")||clientData[1].equals(""))warning.setText("Write userName and Password correctly !");
        else {
            clientMain.clientData = clientData;
            new Client(33333, clientData, clientMain);
        }
    }
    @FXML
    void setClientMain(ClientMain clientMain)
    {
        this.clientMain = clientMain;
    }

}

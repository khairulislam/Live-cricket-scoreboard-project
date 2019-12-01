package Reporter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

public class SenderController {

    public Hashtable<String,String[]> matchData = new Hashtable<>();

    ObservableList<String> observableListM = FXCollections.observableArrayList();

    @FXML
    public ListView<String> matchList = new ListView<>(observableListM);

    ReporterMain ReporterMain;
    Socket socket;

    @FXML
    public TextArea description;

    @FXML
    public TextField matchName;

    @FXML
    public void update(){
        String match[] = new String[2];
        match[0] = matchName.getText();
        match[1] = description.getText();

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(match);
        }catch(Exception e){
            System.out.println();
        }
    }
    public void setReporterMain(ReporterMain ReporterMain, Socket socket)
    {
        this.socket = socket;
        this.ReporterMain = ReporterMain;
    }

}

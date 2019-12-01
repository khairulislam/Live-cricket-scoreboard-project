package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class ClientMain extends Application {

    Stage stage;
    ReceiverController receiverController;
    ClientController clientController;
    Socket socket;
    Parent parent;
    String clientData[] = new String[2];

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        showConnectionPage();
;
    }

    public void showConnectionPage() throws Exception
    {
        FXMLLoader connector = new FXMLLoader(getClass().getResource("ClientConnector.fxml"));

        Parent root = connector.load();

        ClientController clientController = (ClientController) connector.getController();
        clientController.setClientMain(this);
        this.clientController = clientController;

        stage.setTitle("ClientScreen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showReceiverPage() throws Exception
    {
        FXMLLoader receiver = new FXMLLoader(getClass().getResource("Receiver.fxml"));

        Parent root = receiver.load();
        parent = root;

        ReceiverController receiverController = (ReceiverController) receiver.getController();
        this.receiverController = receiverController;
        receiverController.setClientMain(this, socket);

        stage.setScene(new Scene(root));
        stage.setTitle(clientData[0]+"'s ReceiverScreen");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

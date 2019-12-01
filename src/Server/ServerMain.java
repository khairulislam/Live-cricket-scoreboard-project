package Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server implements Runnable
{
    ServerController serverController;
    ServerSocket serverSocket;
    int port = 33333 ;

    Server(ServerController serverController)
    {
        this.serverController = serverController;
        new Thread(this).start();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                //this data contains clientName and is used to check if the client wants to be added or deleted
                String clientData[] = (String[]) in.readObject();

                //requests the controller if this client can be added
                Platform.runLater(() -> serverController.addClient(clientData,socket));
            }

        } catch (Exception e) {
            System.out.println("Error in serverMain");
        }
    }
}

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader serverFXML = new FXMLLoader(getClass().getResource("ServerController.fxml"));
        Parent Root = serverFXML.load();
        ServerController serverController = (ServerController) serverFXML.getController();
        //calls to enlists the fields and areas

        new Server(serverController);

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(Root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package Server;

import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.net.Socket;

public class Reader implements Runnable {
    Socket socket;
    ServerController serverController;
    String[] match = new String[2];
    Reader(Socket socket,ServerController serverController){
        this.socket = socket;
        this.serverController = serverController;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(socket.isConnected()){
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                match = (String[]) in.readObject();
                Platform.runLater(()->serverController.update(match));
            }catch(Exception e){
                System.out.println("Error reading");
                break;
            }
        }
    }
}

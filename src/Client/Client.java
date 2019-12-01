package Client;

import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

class Client implements Runnable
{
    ClientMain clientMain;
    Socket socket;
    Thread thread;
    int port;
    String clientData[] = new String[2];
    ObjectInputStream inputStream;

    Client(int port, String[] clientData, ClientMain clientMain)
    {
        this.clientMain = clientMain;
        this.clientData = clientData;
        this.port = port;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            socket = new Socket("127.0.0.1", port);
            clientMain.socket = socket;

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(clientData);

            inputStream = new ObjectInputStream(socket.getInputStream());
            String[] data = (String[])inputStream.readObject();
            if(data[0].equals("ok")){
                    Platform.runLater(()->{
                        try {
                            clientMain.showReceiverPage();
                        } catch (Exception e) {
                            System.out.println("Error loading");
                        }
                    });
            }
            else {
                Platform.runLater(()->clientMain.clientController.warning.setText(data[0]));
            }

            while(socket.isConnected()) {
                try {
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    String[] match = (String[])inputStream.readObject();
                    Platform.runLater(()->clientMain.receiverController.setMatchList(match));
                }catch (Exception e){
                    //System.out.println("Error reading");
                }

            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting !");
        }
    }
}

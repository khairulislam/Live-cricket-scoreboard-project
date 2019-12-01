package Reporter;

import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Reporter implements Runnable
{
    ReporterMain reporterMain;
    Socket socket;
    String reporterData[] = new String[3];

    Reporter(String[] reporterData, ReporterMain reporterMain)
    {
        this.reporterMain = reporterMain;
        this.reporterData = reporterData;
        new Thread(this).start();
    }

    public void run() {
        try {
            socket = new Socket("127.0.0.1", 33333);
            reporterMain.socket = socket;

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(reporterData);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String[] data = (String[])inputStream.readObject();

            if(data[0].equals("ok")) {
                Platform.runLater(() -> {
                    try {
                        reporterMain.showSenderPage(reporterData[1]);
                    } catch (Exception e) {
                        System.out.println("Error Sending");
                    }
                });
            }
            else Platform.runLater(()->reporterMain.reporterController.warning.setText("Username or Password incorrect !"));

        }catch(Exception e) {
            System.out.println("Error connecting !");
        }
    }
}

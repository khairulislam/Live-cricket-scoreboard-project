package Reporter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class ReporterMain extends Application {

    Stage stage;
    SenderController senderController;
    ReporterController reporterController;
    Socket socket;
    Parent parent;
    String reporterData[] = new String[3];
    String reporterName;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        showConnectionPage();
;
    }

    public void showConnectionPage() throws Exception
    {
        FXMLLoader connector = new FXMLLoader(getClass().getResource("ReporterConnector.fxml"));

        Parent root = connector.load();

        ReporterController reporterController = (ReporterController) connector.getController();
        reporterController.setReporterMain(this);
        this.reporterController = reporterController;

        stage.setTitle("ReporterScreen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showSenderPage(String matchName) throws Exception
    {
        FXMLLoader Sender = new FXMLLoader(getClass().getResource("Sender.fxml"));

        Parent root = Sender.load();
        parent = root;

        SenderController senderController = (SenderController) Sender.getController();
        this.senderController = senderController;

        senderController.setReporterMain(this, socket);
        senderController.matchName.setText(matchName);

        stage.setScene(new Scene(root));
        stage.setTitle("Reporter "+reporterName+"'s SenderScreen");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

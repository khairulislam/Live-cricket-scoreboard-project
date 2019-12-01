package Reporter;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ReporterController {

    ReporterMain reporterMain;

    String reporterData[]=new String[3];

    @FXML
    private TextField match,reporterName,date;
    @FXML
    private PasswordField password;

    @FXML
    Label warning;

    @FXML
    void connect() {
        //collects the name and password
        reporterData[0] = "reporter";
        reporterData[1] = match.getText()+" "+date.getText();
        reporterData[2] = reporterName.getText()+password.getText();

        reporterMain.reporterData = reporterData;
        reporterMain.reporterName = reporterName.getText();
        new Reporter(reporterData, reporterMain);
    }
    @FXML
    void setReporterMain(ReporterMain reporterMain)
    {
        this.reporterMain = reporterMain;
    }

}

package gui;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Case;

/**
 *
 * @author Mads Holm Jensen
 */
public class GUICaseCell extends ListCell<Case> {

    @FXML
    private Label caseNumber;
    @FXML
    private Label caseName;
    @FXML
    private Label caseCPR;
    @FXML
    private Label caseType;
    @FXML
    private Label caseDateCreated;
    @FXML
    private Label caseDateClosed;
    @FXML
    private Label caseDateClosedLabel;

    private Case currentCase;

    @Override
    protected void updateItem(Case item, boolean empty) {
        super.updateItem(item, empty);
        currentCase = item;
        if (empty || item == null) {
            setText("");
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("listCellCase.fxml"));
            loader.setController(this);

            try {
                Parent root = loader.load();

                caseNumber.setText("" + item.getCaseID());
                caseName.setText(item.getFirstName() + " " + item.getLastName());
                caseCPR.setText((int) Math.floor(item.getCprNumber() / 10000) + " - " + (item.getCprNumber() - (long) Math.floor(item.getCprNumber() / 10000) * 10000));
                caseType.setText(item.getType() + " sag");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
                caseDateCreated.setText(formatter.format(item.getDateCreated()));

                if (item.getDateClosed() != null) {
                    caseDateClosed.setText(formatter.format(item.getDateClosed()));
                    caseDateClosedLabel.setVisible(true);
                    caseDateClosed.setVisible(true);
                }

                setText("");
                setGraphic(root);
            } catch (IOException ex) {
                Logger.getLogger(GUICaseCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void btnOpenCase(MouseEvent event) throws IOException {

        if (GUIrun.getLogic().getUserType().equals("CASEWORKER")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("case.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            CaseController controller = loader.getController();
            controller.injectCase(currentCase);
        } else if (GUIrun.getLogic().getUserType().equals("SOCIALWORKER")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("socialWorkerCase.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            SocialWorkerCaseController controller = loader.getController();
            controller.injectCase(currentCase);
        }
    }
}

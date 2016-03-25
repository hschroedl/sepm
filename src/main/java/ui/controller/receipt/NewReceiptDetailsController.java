package ui.controller.receipt;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.Level;
import ui.Output;
import ui.model.ReceiptModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class NewReceiptDetailsController extends AbstractReceiptDetailsController {


    @Override
    public void initializeWith(ReceiptModel receipt) {
        this.receipt = new ReceiptModel(new Date(), "", "", 0.0, new ArrayList<>());
        receiver.setText(this.receipt.getReceiver());
        receiverAdress.setText(this.receipt.getReceiverAddress());
        date.setText(this.receipt.getDateProperty().getValue());
        viewReceiptEntrySelection();
    }

    @Override
    public void handleSave() {
        Output output = mainApp.getOutput();
        if (invalidInput()) {
            output.showNotification(Alert.AlertType.WARNING, "Warning", "Receipt can not be saved", "Please make sure all inputs are correct.");
            return;
        }
        boolean shouldSave = output.showConfirmationDialog("Confirmation", "Would you like to save this receipt?", "Created receipts can not be removed.");
        if (shouldSave) {
            receipt.setReceiver(receiver.getText());
            receipt.setReceiverAddress(receiverAdress.getText());
            mainApp.getReceiptList().get().add(receipt);
            mainApp.showReceiptOverview();
        }
    }

    @Override
    public void handleBack() {
        Output output = mainApp.getOutput();
        boolean shouldGoBack = true;
        if (receiptChanged()) {
            shouldGoBack = output.showConfirmationDialog("Confirmation", "You have unsaved changes", "Are you sure you want to navigate back?");
        }
        if (shouldGoBack) {
            mainApp.showReceiptOverview();
        }
    }

    private void viewReceiptEntrySelection() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ExistingReceiptDetailsController.class.getResource("/views/receiptEntrySelection.fxml"));
            AnchorPane articleDetails = loader.load();
            rootLayout.setCenter(articleDetails);
            ReceiptEntrySelectionController controller = loader.getController();
            controller.initialize(mainApp);
            controller.initializeWith(this.receipt);
        } catch (IOException e) {
            logger.log(Level.DEBUG,e);
            Output output = mainApp.getOutput();
            output.showNotification(Alert.AlertType.ERROR, "Error", "Could not load receipt entries", "Please view the logs for details.");
        }
    }

    private boolean receiptChanged() {
        if (!receipt.getReceiptEntries().isEmpty()) {
            return true;
        } else if ((!Objects.equals(receiver.getText(), "")) || (!Objects.equals(receiverAdress.getText(), ""))) {
            return true;
        }
        return false;
    }

    private boolean invalidInput() {
        if (receipt.getReceiptEntries().isEmpty()) {
            return true;
        } else if ((Objects.equals(receiver.getText(), "")) || (Objects.equals(receiverAdress.getText(), ""))) {
            return true;
        }
        return false;
    }
}

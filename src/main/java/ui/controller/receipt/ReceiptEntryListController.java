package ui.controller.receipt;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ui.controller.article.AbstractController;
import ui.model.ReceiptEntryModel;
import ui.model.ReceiptModel;

public class ReceiptEntryListController extends AbstractController {

    @FXML
    public TextField total;

    @FXML
    public TableView<ReceiptEntryModel> receiptEntryTable;
    @FXML
    public TableColumn<ReceiptEntryModel, String> nameColumn;
    @FXML
    public TableColumn<ReceiptEntryModel, Integer> quantityColumn;
    @FXML
    public TableColumn<ReceiptEntryModel, Double> priceColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty().asObject());

    }

    public void initializeWith(ReceiptModel receipt) {
        receiptEntryTable.setItems(receipt.getReceiptEntryModels());
        total.setText(String.valueOf(receipt.getTotalCostProperty().getValue()));
    }

}
package ui;

import dao.ArticleDao;
import dao.DaoException;
import dao.ReceiptDao;
import dao.h2.H2ArticleDao;
import dao.h2.H2Database;
import dao.h2.H2ReceiptDao;
import dao.h2.ImageFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ArticleRepository;
import service.ReceiptRepository;
import service.ServiceException;
import ui.controller.RootController;
import ui.controller.article.ArticleDetailsController;
import ui.controller.article.ArticleOverviewController;
import ui.controller.article.ArticleStatisticController;
import ui.controller.receipt.AbstractReceiptDetailsController;
import ui.controller.receipt.ExistingReceiptDetailsController;
import ui.controller.receipt.NewReceiptDetailsController;
import ui.controller.receipt.ReceiptOverviewController;
import ui.model.ArticleList;
import ui.model.ArticleModel;
import ui.model.ReceiptList;
import ui.model.ReceiptModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainApp extends Application {

    private final Output output = new Output();
    private ArticleList articleList;
    private ReceiptList receiptList;
    private ReceiptRepository receiptRepository;
    private ArticleRepository articleRepository;
    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    public ReceiptRepository getReceiptRepository() {
        return receiptRepository;
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Wendy's Easter Shop");
        this.primaryStage.getIcons().add(new Image("/icons/application.png"));

        initRootLayout();
        initServices();

        showArticleOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/root.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            RootController controller = loader.getController();
            controller.initialize(this);
            primaryStage.show();
        } catch (Exception e) {
            output.showExceptionNotification("Error", "Unexpected error occured", "Please view logs for more information", e);
        }
    }

    public void showArticleOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/article/articleOverview.fxml"));
            BorderPane personOverview = loader.load();
            rootLayout.setCenter(personOverview);
            ArticleOverviewController controller = loader.getController();
            controller.initialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showArticleDetails(ArticleModel article) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/article/articleDetails.fxml"));
            AnchorPane articleDetails = loader.load();
            rootLayout.setCenter(articleDetails);
            ArticleDetailsController controller = loader.getController();
            controller.initialize(this);
            controller.initializeWith(article);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArticleList getArticleList() {
        return articleList;
    }


    public void showReceiptDetails(ReceiptModel receipt) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/receipt/receiptDetailRoot.fxml"));
            AbstractReceiptDetailsController controller;
            if (receipt == null) {
                controller = new NewReceiptDetailsController();
            } else {
                controller = new ExistingReceiptDetailsController();
            }
            loader.setController(controller);
            BorderPane rootLayout = loader.load();
            this.rootLayout.setCenter(rootLayout);
            controller.initialize(this);
            controller.setRootLayout(rootLayout);
            controller.initializeWith(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showReceiptOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/receipt/receiptOverview.fxml"));
            BorderPane receiptOverview = loader.load();
            rootLayout.setCenter(receiptOverview);
            ReceiptOverviewController controller = loader.getController();
            controller.initialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReceiptList getReceiptList() {
        return receiptList;
    }

    public Output getOutput() {
        return output;
    }

    public File openFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter images = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(images);
        fileChooser.setTitle("Open Image");
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void showArticleStatistics(List<ArticleModel> selected) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/views/article/articleStatistics.fxml"));
            AnchorPane articleStatistic = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Article times sold");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(articleStatistic);
            dialogStage.setScene(scene);

            ArticleStatisticController controller = loader.getController();
            controller.initialize(this);
            controller.initializeWith(dialogStage, selected);

            dialogStage.showAndWait();
        } catch (IOException e) {
            output.showExceptionNotification("Error", "Could not open statistic window", "Please consult logs for more information", e);
        }
    }

    private void initServices() {
        try {
            H2Database database = new H2Database("/home/hschroedl/sepm");
            ArticleDao articleDao = new H2ArticleDao(database, new ImageFile());
            articleRepository = new ArticleRepository(articleDao);
            articleList = new ArticleList(articleRepository);
            ReceiptDao receiptDao = new H2ReceiptDao(database);
            receiptRepository = new ReceiptRepository(receiptDao);
            receiptList = new ReceiptList(receiptRepository);
        } catch (DaoException e) {
            output.showExceptionNotification("Error", "The database could not be reached.",
                    "Please make sure that it is not currently in use.", e);
        } catch (ServiceException e) {
            output.showExceptionNotification("Error", "One or more services could not be initialized",
                    "This might be caused by a database error.", e);
        }
    }


}
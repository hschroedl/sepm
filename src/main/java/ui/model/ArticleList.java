package ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ArticleRepository;
import service.ServiceException;

public class ArticleList {

    private ArticleRepository articleRepository;

    private ObservableList<ArticleModel> articles;

    private final Logger logger = LogManager.getLogger(ArticleList.class);

    public ArticleList(ArticleRepository repository) throws ServiceException {
        this.articleRepository = repository;
        initializeList();
    }

    public ObservableList<ArticleModel> get() {
        return articles;
    }

    public void update(ArticleModel article) throws ServiceException {
        articleRepository.update(article);
    }

    private void initializeList() throws ServiceException {
        articles = FXCollections.observableArrayList();
        ModelFactory factory = new ModelFactory();
        articles.addAll(factory.createArticleModels(articleRepository.getAll()));
        articles.addListener((ListChangeListener<ArticleModel>) c -> {
                    while (c.next()) {
                        removeItems(c);
                        addItems(c);
                    }
                }
        );
    }

    private void addItems(ListChangeListener.Change<? extends ArticleModel> c) {
        for (ArticleModel additem : c.getAddedSubList()) {
            try {
                articleRepository.create(additem);
            } catch (ServiceException e1) {
                logger.error("Failed to create article");
            }
        }
    }

    private void removeItems(ListChangeListener.Change<? extends ArticleModel> c) {
        for (ArticleModel remitem : c.getRemoved()) {
            try {
                articleRepository.delete(remitem);
            } catch (ServiceException e) {
                logger.error("Failed to delete article");
            }
        }
    }
}
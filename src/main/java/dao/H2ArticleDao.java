package dao;

import entities.Article;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2ArticleDao extends AbstractH2Dao implements ArticleDao {

    private final Logger logger = LogManager.getLogger(Database.class);

    private Connection connection;

    public H2ArticleDao(Database database) {
        this.connection = database.getConnection();
    }

    public List<Article> getVisible() throws SQLException {
        logger.debug("Getting all articles");
        String query = "SELECT * FROM ARTICLE WHERE VISIBLE=TRUE ORDER BY ID DESC;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        return parseArticles(resultSet);
    }

    public void update(Article article) throws SQLException {
        logger.debug("Updating article " + article.toString());
        if (isArticleLinked(article)) {
            makeInvisible(article);
            create(article);
        } else {
            updateArticle(article);
        }
    }

    public void create(Article article) throws SQLException {
        logger.debug("Creating article " + article.toString());
        PreparedStatement statement = getCreateStatement(article);
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        if (result.next()) {
            article.setId(result.getInt(1));
        } else {
            throw new SQLException("No id created for " + article.toString());
        }
    }

    public void delete(Article article) throws SQLException {
        logger.debug("Deleting article " + article.toString());
        if (isArticleLinked(article)) {
            makeInvisible(article);
        } else {
            deleteArticle(article);
        }
    }

    private List<Article> parseArticles(ResultSet resultSet) throws SQLException {
        List<Article> articles = new ArrayList<Article>();
        while (resultSet.next()) {
            articles.add(parseArticle(resultSet));
        }
        return articles;
    }

    private void updateArticle(Article article) throws SQLException {
        String updateQuery = "UPDATE ARTICLE SET NAME=?, PRICE=?, DESCRIPTION=?, IMAGE_PATH=?, CATEGORY=? WHERE ID=?;";
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setInt(6, article.getId());
        statement.setString(1, article.getName());
        statement.setDouble(2, article.getPrice());
        statement.setString(3, article.getDescription());
        statement.setString(4, article.getImage());
        statement.setString(5, article.getCategory());
        statement.executeUpdate();
    }

    private boolean isArticleLinked(Article article) throws SQLException {
        String getQuery = "SELECT * FROM ARTICLE_RECEIPT WHERE ARTICLE=?;";
        PreparedStatement statement = connection.prepareStatement(getQuery);
        statement.setInt(1, article.getId());
        ResultSet result = statement.executeQuery();
        return result.next();
    }

    private PreparedStatement getCreateStatement(Article article) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO article VALUES (default, ?, ?, ?, ?, ?,default)");
        statement.setString(1, article.getName());
        statement.setDouble(2, article.getPrice());
        statement.setString(3, article.getDescription());
        statement.setString(4, article.getImage());
        statement.setString(5, article.getCategory());
        return statement;
    }

    private void makeInvisible(Article article) throws SQLException {
        String alterQuery = "UPDATE ARTICLE SET VISIBLE=FALSE WHERE ID=?;";
        PreparedStatement statement = connection.prepareStatement(alterQuery);
        statement.setInt(1, article.getId());
        statement.executeUpdate();
    }

    private void deleteArticle(Article article) throws SQLException {
        String deleteQuery = "DELETE FROM ARTICLE WHERE ID =?;";
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        statement.setInt(1, article.getId());
        statement.executeUpdate();
    }
}

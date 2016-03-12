package dao;

import entities.Article;

import java.sql.SQLException;
import java.util.List;

/**
 * Article interaction with a database
 */
public interface ArticleDao {
    /**
     * Create a new article
     * @param entity
     * @throws SQLException
     */
    void create(Article entity) throws SQLException;

    /**
     * Retrieve all visible articles
     * @return A list of articles
     * @throws SQLException
     */
    List<Article> getVisible() throws SQLException;

    /**
     * Updates an existing article
     * @param entity
     * @throws SQLException
     */
    void update(Article entity) throws SQLException;

    /**
     * Deletes an existing article
     * @param entity
     * @throws SQLException
     */
    void delete(Article entity) throws SQLException;


}

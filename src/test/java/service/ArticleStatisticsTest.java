package service;

import base.BaseTest;
import entities.Article;
import entities.Receipt;
import org.junit.Test;
import org.mockito.Mock;
import service.filter.ReceiptCriteria;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static base.DummyEntityFactory.createDummyArticles;
import static base.DummyEntityFactory.createDummyReceipt;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ArticleStatisticsTest extends BaseTest {

    @Mock
    private ReceiptRepository mockRepository;

    @Test
    public void timesSold_SingleArticle_CalculatesTimesSold() throws Exception, CalculationException {
        List<Article> dummyArticles = createDummyArticles(1);
        List<Receipt> dummyReceipts = new ArrayList<>();
        dummyReceipts.add(createDummyReceipt(dummyArticles));
        dummyReceipts.add(createDummyReceipt(dummyArticles));
        dummyReceipts.add(createDummyReceipt(dummyArticles));
        when(mockRepository.getReceipts()).thenReturn(dummyReceipts);
        ArticleStatistics statistics = new ArticleStatistics(mockRepository, dummyArticles);
        int expectedTimesSold = 3;

        List<StatisticEntry> statisticEntries = statistics.timesSold();

        assertEquals(expectedTimesSold, statisticEntries.get(0).getTimesSold());
    }

    @Test
    public void timesSold_MultipleArticles_CalculatesStatForAllArticles() throws Exception, CalculationException {
        List<Article> dummyArticles = createDummyArticles(5);
        List<Receipt> dummyReceipts = new ArrayList<>();
        dummyReceipts.add(createDummyReceipt(dummyArticles));
        dummyReceipts.add(createDummyReceipt(dummyArticles));
        dummyReceipts.add(createDummyReceipt(dummyArticles));
        when(mockRepository.getReceipts()).thenReturn(dummyReceipts);
        ArticleStatistics statistics = new ArticleStatistics(mockRepository, dummyArticles);
        int expectedStatisticsCount = 5;

        List<StatisticEntry> statisticEntries = statistics.timesSold();

        assertEquals(expectedStatisticsCount, statisticEntries.size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = CalculationException.class)
    public void timesSold_DaoErrorOccurs_RethrowsCalculationExcpeption() throws Exception, CalculationException {
        List<Article> dummyArticles = createDummyArticles(1);
        when(mockRepository.getReceipts()).thenThrow(SQLException.class);
        ArticleStatistics statistics = new ArticleStatistics(mockRepository, dummyArticles);

        statistics.timesSold();
    }

    @Test
    public void timeSoldSince_SingleArticle_CalculatesTimesSold() throws Exception, CalculationException {
        List<Article> dummyArticles = createDummyArticles(1);
        List<Receipt> dummyReceipts = new ArrayList<>();
        dummyReceipts.add(createDummyReceipt("01/01/2001", dummyArticles));
        when(mockRepository.filter(any(ReceiptCriteria.class))).thenReturn(dummyReceipts);
        ArticleStatistics statistics = new ArticleStatistics(mockRepository, dummyArticles);
        int expectedTimesSold = 1;

        //Date is irrelevant in this case, as we build upon repository.filter(...)
        List<StatisticEntry> statisticEntries = statistics.timesSoldSince(new Date());

        assertEquals(expectedTimesSold, statisticEntries.get(0).getTimesSold());
    }
}
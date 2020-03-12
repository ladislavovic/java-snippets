package cz.kul.snippets.lucene.example03_queries;

import cz.kul.snippets.SnippetsTest;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestQueries extends SnippetsTest {
    
    @Test
    public void testQuery() throws IOException {
        Path path = Paths.get(getFilesystemHelper().createRandomDirPath());
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        try (
                FSDirectory dir = FSDirectory.open(path);
                IndexWriter writer = new IndexWriter(dir, iwc)) {
            Document doc = new Document();
            doc.add(new TextField("id", "1", Field.Store.YES));
            doc.add(new TextField("title", "Harry Potter and the Sorcerer's Stone", Field.Store.YES));
            doc.add(new TextField("year", "2001", Field.Store.NO));
            doc.add(new TextField("director", "Chris Columbus", Field.Store.NO));
            doc.add(new TextField("length", "152", Field.Store.NO));
            writer.addDocument(doc);
            Document doc2 = new Document();
            doc2.add(new TextField("id", "2", Field.Store.YES));
            doc2.add(new TextField("title", "Harry Potter and the Chamber of Secrets", Field.Store.YES));
            doc2.add(new TextField("year", "2002", Field.Store.NO));
            doc2.add(new TextField("director", "Chris Columbus", Field.Store.NO));
            doc2.add(new TextField("length", "161", Field.Store.NO));
            writer.addDocument(doc2);
        }

        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(reader);
        
        {
            // TermQuery 1
            //
            // Query which search by simple term
            Term term = new Term("title", "stone");
            TermQuery query = new TermQuery(term);
            TopDocs results = searcher.search(query, 10);
            assertOnlyHarryPotter1(results, searcher);
        }
        
        {
            // TermQuery 2
            //
            // Be carefull, there is no Analyzer! You must give the
            // exact term, because there is no filtering - no lowercase,
            // no stemming, ... so when you give "Stone" instead of "stone"
            // it finds nothing
            Term term = new Term("title", "Stone");
            TermQuery query = new TermQuery(term);
            TopDocs results = searcher.search(query, 10);
            Assert.assertEquals(0, results.scoreDocs.length);
        }
        
        {
            // PrefixQuery
            //
            // Search all tokens which start with the given prefix
            Term term = new Term("title", "pott");
            PrefixQuery query = new PrefixQuery(term);
            TopDocs results = searcher.search(query, 10);
            assertBothHarryPotters(results, searcher);
        }
        
        {
            // WildcardQuery
            //
            // Search all tokens which much the given pattern. You can use "*" and "?"
            // characters in the pattern.
            Term term = new Term("title", "po??er");
            Query query = new WildcardQuery(term);
            TopDocs results = searcher.search(query, 10);
            assertBothHarryPotters(results, searcher);
        }
        
        {
            // FuzzyQuery
            //
            // Can found "similar" word. There is an algorithm, which can calculate "distance"
            // between words. If the distance is below the given limit it hits. You can set the
            // limit.
            Term term = new Term("title", "slone"); // "stone" is close
            Query query = new FuzzyQuery(term);
            TopDocs results = searcher.search(query, 10);
            assertOnlyHarryPotter1(results, searcher);
        }
        
        {
            // BooleanQuery
            Term term1 = new Term("title", "harry");
            TermQuery query1 = new TermQuery(term1);
            
            Term term2 = new Term("title", "stone");
            TermQuery query2 = new TermQuery(term2);

            BooleanQuery query = new BooleanQuery.Builder()
                    .add(query1, BooleanClause.Occur.MUST)
                    .add(query2, BooleanClause.Occur.MUST_NOT)
                    .build();

            TopDocs results = searcher.search(query, 10);
            assertOnlyHarryPotter2(results, searcher);
        }
        
        {
            // BooleanQuery - SHOULD
            //
            // The document match, if the required number of SHOULD queries match. You can set
            // how many must match. The default value is 0 if there is at least one MUST query
            // and 1 when there is no MUST query.

            // not MUST
            {
                Term term1 = new Term("title", "_missing_");
                TermQuery query1 = new TermQuery(term1);

                BooleanQuery query = new BooleanQuery.Builder()
                        .add(query1, BooleanClause.Occur.SHOULD)
                        .build();
                
                TopDocs results = searcher.search(query, 10);
                Assert.assertEquals(0, results.scoreDocs.length);
            }
            
            // not MUST
            {
                Term term1 = new Term("title", "stone");
                TermQuery query1 = new TermQuery(term1);

                BooleanQuery query = new BooleanQuery.Builder()
                        .add(query1, BooleanClause.Occur.SHOULD)
                        .build();
                
                TopDocs results = searcher.search(query, 10);
                assertOnlyHarryPotter1(results, searcher);
            }
            
            // with MUST
            {
                Term term1 = new Term("title", "harry");
                TermQuery query1 = new TermQuery(term1);

                Term term2 = new Term("title", "stone");
                TermQuery query2 = new TermQuery(term2);

                BooleanQuery query = new BooleanQuery.Builder()
                        .add(query1, BooleanClause.Occur.MUST)
                        .add(query2, BooleanClause.Occur.SHOULD)
                        .build();
                
                TopDocs results = searcher.search(query, 10);
                assertBothHarryPotters(results, searcher);
            }
            
            // explicit should match
            {
                Term term1 = new Term("title", "harry");
                TermQuery query1 = new TermQuery(term1);

                Term term2 = new Term("title", "secrets");
                TermQuery query2 = new TermQuery(term2);

                BooleanQuery query = new BooleanQuery.Builder()
                        .setMinimumNumberShouldMatch(2)
                        .add(query1, BooleanClause.Occur.SHOULD)
                        .add(query2, BooleanClause.Occur.SHOULD)
                        .build();
                
                TopDocs results = searcher.search(query, 10);
                assertOnlyHarryPotter2(results, searcher);
            }
            
        }

    }
    
    private void assertOnlyHarryPotter1(TopDocs results, IndexSearcher searcher) {
        try {
            Assert.assertEquals(1, results.scoreDocs.length);
            Document doc = searcher.doc(results.scoreDocs[0].doc);
            Assert.assertEquals("1", doc.get("id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void assertOnlyHarryPotter2(TopDocs results, IndexSearcher searcher) {
        try {
            Assert.assertEquals(1, results.scoreDocs.length);
            Document doc = searcher.doc(results.scoreDocs[0].doc);
            Assert.assertEquals("2", doc.get("id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void assertBothHarryPotters(TopDocs results, IndexSearcher searcher) {
        Assert.assertEquals(2, results.scoreDocs.length);
    }
    
    private Document search(String query, QueryParser parser, IndexSearcher searcher) throws ParseException, IOException {
        Query q = parser.parse(query);
        TopDocs results = searcher.search(q, 1);
        if (results.scoreDocs.length == 0) {
            return null;
        } else {
            return searcher.doc(results.scoreDocs[0].doc);
        }
    }
    
}

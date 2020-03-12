package cz.kul.snippets.lucene.example02_queryparser;

import cz.kul.snippets.SnippetsTest;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestQueryParser extends SnippetsTest {
    
    @Test
    public void testQuery() throws IOException, ParseException {
        Path path = Paths.get(getFilesystemHelper().createRandomDirPath());
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        try (
                FSDirectory dir = FSDirectory.open(path);
                IndexWriter writer = new IndexWriter(dir, iwc)) {
            Document doc = new Document();
            doc.add(new TextField("content", "Harry Potter and the Sorcerer's Stone", Field.Store.YES));
            writer.addDocument(doc);
        }

        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(reader);
        
        // NOTE: the first param is a default field
        QueryParser parser = new QueryParser("content", analyzer);
        
        {
            // basic query
            Assert.assertNotNull(search("content:harry", parser, searcher));
            Assert.assertNull(search("content:harr", parser, searcher));
        }
        {
            // wildcard query
            Assert.assertNotNull(search("content:ha??y", parser, searcher));
            Assert.assertNotNull(search("content:ha*y", parser, searcher));
            Assert.assertNull(search("content:ha?y", parser, searcher));
        }
        {
            // combine more queries
            Assert.assertNotNull(search("content:harry AND content:potter", parser, searcher));
            Assert.assertNull(search("content:harry AND content:longbottom", parser, searcher));
        }
        {
            // fuzzy query
            Assert.assertNotNull(search("content:harri~", parser, searcher));
            Assert.assertNull(search("content:harriii~", parser, searcher));
        }

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

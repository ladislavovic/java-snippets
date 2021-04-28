package cz.kul.snippets.lucene.example04_documentsCanHaveDifferentFields;

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
import org.apache.lucene.index.IndexableField;
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
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Documents in the index does not have to have the same field structure.
 */
public class TestDocumentsWithDifferentFields extends SnippetsTest {
    
    @Test
    public void documentsWithDifferentFields() throws IOException, ParseException {
        // Common
        Path path = Paths.get(getFilesystemHelper().createRandomDirPath());
        Analyzer analyzer = new StandardAnalyzer();

        // Create an index
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // create new index
        try (FSDirectory dir = FSDirectory.open(path);
            IndexWriter writer = new IndexWriter(dir, iwc)) {
            Document doc1 = new Document();
            doc1.add(new TextField("f1", "d1f1", Field.Store.YES));
            doc1.add(new TextField("f2", "", Field.Store.YES));
            writer.addDocument(doc1);

            Document doc2 = new Document();
            doc2.add(new TextField("f1", "d2f1", Field.Store.YES));
            doc2.add(new TextField("f2", "d2f2", Field.Store.YES));
            doc2.add(new TextField("f3", "d2f3", Field.Store.YES));
            writer.addDocument(doc2);
        }

        // Search
        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("f1", analyzer);
        {
            Query query = parser.parse("d1f1");
            TopDocs result = searcher.search(query, 100);
            assertEquals(1, result.scoreDocs.length);
            Document doc1 = searcher.doc(result.scoreDocs[0].doc);
            Assert.assertEquals(
                Arrays.asList("f1", "f2"),
                doc1.getFields().stream().map(IndexableField::name).collect(Collectors.toList()));
        }
        {
            Query query = parser.parse("d2f1");
            TopDocs result = searcher.search(query, 100);
            assertEquals(1, result.scoreDocs.length);
            Document doc2 = searcher.doc(result.scoreDocs[0].doc);
            Assert.assertEquals(
                Arrays.asList("f1", "f2", "f3"),
                doc2.getFields().stream().map(IndexableField::name).collect(Collectors.toList()));
        }
    }
    
}

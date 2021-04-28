package cz.kul.snippets.lucene.example01_hw;

import cz.kul.snippets.SnippetsTest;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
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
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestHw extends SnippetsTest {
    
    private static class Film {
        String title;
        String description;
        int year;

        public Film(String title, String description, int year) {
            this.title = title;
            this.description = description;
            this.year = year;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getYear() {
            return year;
        }
    }
    
    @Test
    public void indexAndSearch() throws IOException, ParseException {
        // Common
        Path path = Paths.get(getFilesystemHelper().createRandomDirPath());
        Analyzer analyzer = new StandardAnalyzer();


        // Data
        List<Film> films = new ArrayList<>();
        films.add(new Film(
                "The Shawshank Redemption",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                1994
        ));
        films.add(new Film(
                "Harry Potter and the Sorcerer's Stone",
                "An orphaned boy enrolls in a school of wizardry, where he learns the truth about himself, his family and the terrible evil that haunts the magical world.",
                2001
        ));
        films.add(new Film(
                "Terminator",
                "In 1984, a human soldier is tasked to stop an indestructible cyborg killing machine, both sent from 2029, from executing...",
                1984
        ));

        // Init
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // create new index

        // Index
        try (FSDirectory dir = FSDirectory.open(path);
             IndexWriter writer = new IndexWriter(dir, iwc)) {
            for (Film film : films) {
                Document doc = new Document();
                doc.add(new TextField("title", film.getTitle(), Field.Store.YES));
                doc.add(new TextField("description", film.getDescription(), Field.Store.NO));
                doc.add(new IntPoint("year", film.getYear()));
                writer.addDocument(doc);
            }
        }
        
        // Search
        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("title", analyzer);
        {
            Query query = parser.parse("potter");
            TopDocs result = searcher.search(query, 100);
            
            assertEquals(1, result.scoreDocs.length);
            Document doc = searcher.doc(result.scoreDocs[0].doc);
            assertEquals("Harry Potter and the Sorcerer's Stone", doc.get("title"));
        }
    }
    
}

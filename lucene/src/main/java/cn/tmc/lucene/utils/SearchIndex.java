package cn.tmc.lucene.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class SearchIndex {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    @Before
    public void init() throws IOException {
        indexReader = DirectoryReader.open(FSDirectory.open(new File("D:\\files\\index").toPath()));
        indexSearcher=new IndexSearcher(indexReader);
    }

    @Test
    public void queryParserSearch() throws Exception {
        QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
        Query query = queryParser.parse("Learn about working at Oracle");
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("出现的次数:"+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc doc:scoreDocs){
            int docId = doc.doc;
            Document document = indexSearcher.doc(docId);
            String name = document.get("name");
            System.out.println(name);
        }
    }
}

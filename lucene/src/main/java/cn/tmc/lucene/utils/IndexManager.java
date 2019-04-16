package cn.tmc.lucene.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexManager {
    private IndexWriter indexWriter;

    @Before
    public void init() throws Exception{
        FSDirectory directory = FSDirectory.open(new File("D:\\files\\index").toPath());
        indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
    }

    //添加文档到索引库
    @Test
    public void addDocument() throws Exception{

        Document document = new Document();
        document.add(new TextField("name","新添加的文件", Field.Store.YES));
        document.add(new TextField("content","新添加文件的内容",Field.Store.YES));
        document.add(new StoredField("path","d:/txt"));
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    @Test
    public void deleteAllDoc() throws IOException {
        indexWriter.deleteAll();
        indexWriter.close();
    }

    @Test
    public void deleteByTerm() throws IOException {
        indexWriter.deleteDocuments(new Term("name","mysql"));
        indexWriter.close();
    }

    //lucene的更新文档是先删除后添加
    @Test
    public void updateDoc()throws Exception{
        Document document = new Document();
        document.add(new TextField("name","更新后的名字",Field.Store.YES));
        document.add(new TextField("field","更新了新的字段",Field.Store.YES));
        document.add(new StoredField("hobby","更新的爱好"));
        indexWriter.updateDocument(new Term("name","oracle"),document);
        indexWriter.close();
    }
}

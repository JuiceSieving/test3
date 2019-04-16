package cn.tmc.lucene.utils;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class LuceneUtils {

    @Test
    public void createIndex() throws IOException {
        //指定将索引库保存位置
        FSDirectory directory = FSDirectory.open(new File("D:\\files\\index").toPath());
        //创建IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        //读取指定目录中的文件，并为文件创建文档对象
        File dir = new File("D:\\txt");
        File[] files = dir.listFiles();
        for(File f:files){
            //取出文件名,文件路径,文件大小,文件内容
            String name = f.getName();
            String path = f.getPath();
            String content = FileUtils.readFileToString(f, "UTF-8");
            long size = FileUtils.sizeOf(f);
            //创建字段存储上面的内容
            Field fieldName = new TextField("name", name, Field.Store.YES);
            Field fieldPath = new StoredField("path", path);
            Field fieldContent = new TextField("content", content, Field.Store.YES);
//            Field fieldSizeValue = new LongPoint("size", size);
            Field fieldSizeStore=new StoredField("size",size);
            //创建文档对象
            Document document = new Document();
            //将字段添加到文档对象
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
//            document.add(fieldSizeValue);
            document.add(fieldSizeStore);
            //将文档对象写入索引库
            indexWriter.addDocument(document);
        }
        //关闭IndexWriter对象
        indexWriter.close();
    }

    @Test
    public void queryIndex() throws Exception{
        //打开索引库所在目录
        FSDirectory directory = FSDirectory.open(new File("D:\\files\\index").toPath());
        //通过索引读取对象创建索引搜索对象
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //创建查询对象
        TermQuery termQuery = new TermQuery(new Term("name", "oracle"));
        //通过搜索对象查询条件查询内容并设置最大查询结果数量
        TopDocs search = indexSearcher.search(termQuery, 10);
        //显示查询出现的总次数
        System.out.println("总共出现的次数为:"+search.totalHits);
        //获取查询到的列表
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for(ScoreDoc doc:scoreDocs){
            //取文档id
            int docId = doc.doc;
            //根据id获取所在文档
            Document document = indexSearcher.doc(docId);
            //显示文档名称
            System.out.println(document.get("name"));
            System.out.println("----------------------");
        }
        reader.close();
    }

    //测试标准分析器分词效果
    @Test
    public void queryAnalysis() throws Exception{
        //创建标准分析器对象
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //使用分析器获取TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "The Spring Framework provides a comprehensive programming and configuration model for modern Java-based enterprise applications.");
        //获取tokenStream数据的指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //将tokenStream数据指针放在第一个位置
        tokenStream.reset();
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        //关闭tokenStream
        tokenStream.close();
    }

    //测试IK分析器分词效果
    @Test
    public void queryIKAnalysis() throws Exception{
        //创建标准分析器对象
        IKAnalyzer analyzer = new IKAnalyzer();
        //使用分析器获取TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "介绍我们编写的应用程序要完成数据的收集，再将数据以document的形式用lucene的索引API创建索引、存储。");
        //获取tokenStream数据的指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //将tokenStream数据指针放在第一个位置
        tokenStream.reset();
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        //关闭tokenStream
        tokenStream.close();
    }

    @Test
    public void testFile(){
        Path path = new File("D:\\files\\index").toPath();
        System.out.println(path.toString());
    }
}

package com.company;

import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Indexer {
    private static DB db = new DB();
    private static Map<String, Integer> tag = new HashMap<>();
    private static List<String> stopWords = new ArrayList<>();
    private static HashMap<String, List<Integer>> wordMap = new HashMap<>();
    private static Stemmer stemmer = new Stemmer();

    public static Document getDocument(String url) throws IOException {
        Connection connect= Jsoup.connect(url);
        Document doc = connect.get();
        return doc;
    }

    public static void setTags() {
        tag.put("title",100);
        tag.put("h1",10);
        tag.put("h2",9);
        tag.put("h3",8);
        tag.put("h4",7);
        tag.put("h5",6);
        tag.put("h6",5);
        tag.put("p",4);
    }

    public static void main(String[] args) throws IOException {
        setTags();
        ReadStopWords();
        int Count = (int)db.getAttr("Globals", "key","counter","value" );
        for (int i = 0; i < Count; i++) {
            String url = (String)db.getAttr("URLs","id", i,"url");
            Document doc = getDocument(url);
            for (Map.Entry<String,Integer> entry : tag.entrySet())
                indexing(entry.getKey(), doc, entry.getValue());

            for (Map.Entry<String,List<Integer>> entry : wordMap.entrySet())
                System.out.println(entry.getKey()+" ---> "+ entry.getValue());

            insertToIndexer(wordMap, url);
            wordMap.clear();
        }
    }
    public static void indexing(String tag, Document doc, int weight) {
        String temp_text =doc.select(tag).text();
        String[] text_split = temp_text.split(" "); // split the text
        text_split = removeStopWords(text_split);
        for (String s : text_split)
            if (!s.equals("")) {
                String Temp = stemmer.Stemming(s);
                wordMap.putIfAbsent(Temp, new ArrayList<Integer>() {{
                    add(0);
                    add(0);
                }}); // For initial insertion
                wordMap.put(Temp, new ArrayList<>() {{
                    add(wordMap.get(Temp).get(0) + weight);
                    add(wordMap.get(Temp).get(1) + 1);
                }}); // Increase weight & TF of each word
            }
    }

//    Indexer structure in the DB
//    {
//        "word":"computer"
//        "urls": [{"TF":"val", "weight":"val", "url":"url"}, {...}, {...}];
//        "DF":"val"
//    }

    public static void insertToIndexer(HashMap<String, List<Integer>> words, String url) {
        for (Map.Entry<String,List<Integer>> entry : words.entrySet()) {
            if (db.isExists("words", "word", entry.getKey())) {
                int DF = (int) db.getAttr("words", "word", entry.getKey(), "DF");
                db.updateDB("words", "word", entry.getKey(), "DF", DF + 1);
                BasicDBObject doc = new BasicDBObject("TF", entry.getValue().get(1));
                doc.append("weight", entry.getValue().get(0));
                doc.append("url", url);
                ArrayList<BasicDBObject> arr = (ArrayList<BasicDBObject>) db.getAttr("words", "word", entry.getKey(), "urls");
                System.out.println(arr);
                arr.add(doc);
                System.out.println(arr);
                db.updateDB("words", "word", entry.getKey(), "urls", arr);
            }
            else {
                ArrayList<String> keys = new ArrayList<>(){{add("word"); add("urls"); add("DF");}};
                ArrayList<BasicDBObject> urls = new ArrayList<BasicDBObject>();
                BasicDBObject doc = new BasicDBObject("TF", entry.getValue().get(1));
                doc.append("weight", entry.getValue().get(0));
                doc.append("url", url);
                urls.add(doc);
                ArrayList<Object> values = new ArrayList<>(){{add(entry.getKey()); add(urls); add(1);}};
                db.insertToDB("words", keys, values);
            }
        }
    }

    public static void ReadStopWords() throws FileNotFoundException, IOException {
        try {
            File file = new File("D:\\Github\\Search-Engine\\webCrawler\\src\\main\\java\\com\\company\\stopwords.txt");
            Scanner Reader = new Scanner(file);
            while (Reader.hasNextLine()) {
                stopWords.add(Reader.nextLine());
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    private static String[] removeStopWords (String[] M){
        for(String word : stopWords) {
            for (int i = 0; i < M.length; i++) {
                M[i] = M[i].replaceAll("[^a-zA-Z]","");
                if (M[i].toLowerCase(Locale.ROOT).equals(word))
                    M = ArrayUtils.remove(M, i);
            }
        }
        return M;
    }
}

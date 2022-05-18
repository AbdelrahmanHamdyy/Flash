package com.company;

import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;
public class Indexer {
    public static DB db = new DB();
    public static Map<String, Integer> tag = new HashMap<>();
    public static List<String> stopWords = new ArrayList<>();
    public static HashMap<String, HashMap<String,IndexerObject>> WordInsert = new HashMap<>();
    public static HashMap<String, HashSet<String>> stem = new HashMap<>();
    public static Stemmer stemmer = new Stemmer();
    public static int numberOfParagraphs;
    public static HashMap<String,Integer>wordParagraphsMapping;
    public static ArrayList<Integer>paragraphs=new ArrayList<Integer>();
    public static int pageIndex = 0;
    public static Document getDocument(String url) throws IOException {
        try {
            Connection connect= Jsoup.connect(url);
            Document doc=connect.get();
            if(connect.response().statusCode()==200)
            {
                return doc;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public static void setTags() {
        tag.put("title",100);
        tag.put("h1",40);
        tag.put("h2",20);
        tag.put("h3",10);
        tag.put("h4",8);
        tag.put("h5",6);
        tag.put("h6",5);
        tag.put("p",4);
    }
    public static void setCounter() {
        if(db.isExists("Globals","key","paragraphsCounter"))
            return;
        ArrayList<String> keys = new ArrayList<>();
        keys.add("key");
        keys.add("value");
        ArrayList<Object> values = new ArrayList<>();
        values.add("paragraphsCounter");
        values.add(0);
        db.insertToDB("Globals", keys, values);
    }

    public static void main(String[] args) throws IOException {
        setTags();
        ReadStopWords();
        wordParagraphsMapping=new HashMap<String,Integer>();
        setCounter();
        numberOfParagraphs=(int)db.getAttr("Globals", "key","paragraphsCounter","value" );
        int Count = (int)db.getAttr("Globals", "key","counter","value" );
        for (int i = 0; i < Count; i++) {
            pageIndex = 0;
            String url = (String)db.getAttr("URLs","id", i,"url");
            Document doc = getDocument(url);
            paragraphs.clear();
            if (doc != null) {
                for (Map.Entry<String, Integer> entry : tag.entrySet()) {
                    indexing(entry.getKey(), doc, entry.getValue(),url);
                }
                db.updateDB("URLs","url",url,"paragraphs",paragraphs);
            }
        }
        insertToIndexer();
        insertToStem();
        db.updateDB("Globals","key","paragraphsCounter","value",numberOfParagraphs);
    }
    public static void indexing(String tag, Document doc, int weight,String url) {
        Elements elements = doc.select(tag);
        for (Element i : elements) {
            ArrayList<String> keys1 = new ArrayList<String>();
            ArrayList<Object> values1 = new ArrayList<Object>();
            paragraphs.add(numberOfParagraphs);
            keys1.add("id");values1.add(numberOfParagraphs++);
            keys1.add("content");values1.add(i.text());
            db.insertToDB("Paragraphs",keys1,values1);
            String temp_text = i.text().toLowerCase(Locale.ROOT);
            String[] text_split = temp_text.split(" "); // split the text
            text_split = removeStopWords(text_split);
            for (String s : text_split) {
                if (!s.equals("")) {
                    String lowerCaseString = s.toLowerCase();
                    String Temp = stemmer.Stemming(lowerCaseString);
                    stem.putIfAbsent(Temp,new HashSet<String>());
                    stem.get(Temp).add(lowerCaseString);
                    /////////////
                    WordInsert.putIfAbsent(lowerCaseString, new HashMap<>());
                    WordInsert.get(lowerCaseString).putIfAbsent(url,new IndexerObject());
                    HashMap<String,IndexerObject> Obj = WordInsert.get(lowerCaseString);
                    WordInsert.get(lowerCaseString).get(url).TF+=1;
                    if(WordInsert.get(lowerCaseString).get(url).paragraphID==-1)
                        WordInsert.get(lowerCaseString).get(url).paragraphID=numberOfParagraphs-1;
                    WordInsert.get(lowerCaseString).get(url).Weight+=weight;
                    WordInsert.get(lowerCaseString).get(url).positions.add(pageIndex);
                    ////////
                    pageIndex++;
                }
            }
        }

    }

    public static void insertToIndexer() {
        for (Map.Entry<String, HashMap<String,IndexerObject>> entry : WordInsert.entrySet()) {
            ArrayList<String> keys = new ArrayList<>() {{
                add("word");
                add("urls");
                add("DF");
            }};
            ArrayList<BasicDBObject> urls = new ArrayList<BasicDBObject>();
            for (Map.Entry<String,IndexerObject> entry2 :entry.getValue().entrySet()) {
                BasicDBObject doc = new BasicDBObject("TF",entry2.getValue().TF);
                doc.append("weight", entry2.getValue().Weight);
                doc.append("url", entry2.getKey());
                doc.append("positions",  entry2.getValue().positions);
                doc.append("paragraphID", entry2.getValue().paragraphID);
                urls.add(doc);
            }
            ArrayList<Object> values = new ArrayList<>() {{
                add(entry.getKey());
                add(urls);
                add(urls.size());
            }};
            db.insertToDB("words", keys, values);
        }
    }
    public static void insertToStem(){
        for (Map.Entry<String,HashSet<String>> entry : stem.entrySet()) {
            ArrayList<String> keys = new ArrayList<String>();
            ArrayList<Object> values = new ArrayList<Object>();
            keys.add("key");
            values.add(entry.getKey());
            keys.add("array");
            values.add(entry.getValue());
            db.insertToDB("stemming", keys, values);
        }
    }
    public static void ReadStopWords() throws FileNotFoundException, IOException {
        try {
            File file = new File("stopwords.txt");
            Scanner Reader = new Scanner(file);
            while (Reader.hasNextLine()) {
                stopWords.add(Reader.nextLine().toLowerCase(Locale.ROOT));
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    public static String[] removeStopWords (String[] M){
        ArrayList<String> MList = new ArrayList();
        for(String word : M) {
            //MList.removeAll(Collections.singleton(word.toLowerCase(Locale.ROOT)));
            word = word.replaceAll("[^a-zA-Z0-9]","");
           if(word!="" &&!stopWords.contains(word))
               MList.add(word);
        }

        return MList.toArray(new String[0]);
    }
}
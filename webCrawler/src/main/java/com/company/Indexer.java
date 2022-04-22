package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Indexer {
    private static DB db = new DB();
    private static Stemmer stemmer = new Stemmer();
    private static  Map<String, Integer> tag = new HashMap<>();
    private static List<String> stopWords = new ArrayList<>();
    private static HashMap<String, Integer> word = new HashMap<>();
    private static int Count ;
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
        for (int i = 0; i < Count; i++) {
            String url = (String)db.getAttr("URLs","ID",i,"url");
            Document doc = getDocument(url);
            for (Map.Entry<String,Integer> entry : tag.entrySet())
            {
                indexing(entry.getKey(),doc, entry.getValue());
            }
            for (Map.Entry<String,Integer> entry : word.entrySet()) {
                System.out.println("*****************");
                System.out.println(entry.getKey()+"--->"+ entry.getValue());
            }
            word.clear();
        }
    }
    public static void indexing(String tag, Document doc, int weight) {
        String temp_text =doc.select(tag).text();
        temp_text = removeStopWords(temp_text);
        String[] text_split = temp_text.split(" "); // split the text
        for(int i=0;i<text_split.length;i++) {
            String stemmed=stemmer.Stemming(text_split[i]);
            if (!stemmed.equals("")) {
                word.putIfAbsent(stemmed, 0); // if first time to put it
                word.put(stemmed, word.get(stemmed) + weight); // increase weight
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

    private static String removeStopWords (String M){
        for(String word : stopWords) {
            M.replaceAll(word, "");
        }
        return M;
    }

}

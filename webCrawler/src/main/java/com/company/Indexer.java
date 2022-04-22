package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Indexer {
    private static List<String> urls;
    private static DB db = new DB();
    private static  Map<String, Integer> tag = new HashMap<>();
    private static List<String> stopWords = new ArrayList<>();
    private static HashMap<String, Integer> word = new HashMap<>();

    public static void setUrls() {
        urls = db.getUrls();

    }
    public static Document getDocument(String url) throws IOException {
        Connection connect= Jsoup.connect(url);
        Document doc = connect.get();
        return doc;
    }

    public static void main(String[] args) throws IOException {
        tag.put("title",100);
        tag.put("h1",10);
        tag.put("h2",9);
        tag.put("h3",8);
        tag.put("h4",7);
        tag.put("h5",6);
        tag.put("h6",5);
        tag.put("p",4);
        setUrls();
        int Count = urls.size();
        ReadStopWords();
        for (String url : urls) {
            Document doc = getDocument(url);
           // HashMap<String,Integer> unfilter =new HashMap<>();
            for (Map.Entry<String,Integer> entry : tag.entrySet()) {
                indexing(entry.getKey(),doc, entry.getValue());
            }
            word=removeStopWords(word);
            for (Map.Entry<String,Integer> entry : word.entrySet()) {
                System.out.println("*****************");
                System.out.println(entry.getKey()+"--->"+ entry.getValue());
                System.out.println("*****************");
            }
            word.clear();
            // For each document:
            // Convert to text and split into words (remove html tags, etc..)
            // Remove Stop words
            // Stemming
            // Store each word in db along with ...?
        }

    }
    public static void indexing(String tag, Document doc, int weight) {
        String temp_text =doc.select(tag).text();
        String[] text_split = temp_text.split(" "); // split the text
        for(int i=0;i<text_split.length;i++) {
            if (!text_split[i].equals("")) {
                word.putIfAbsent(text_split[i], 0); // if first time to put it
                word.put(text_split[i], word.get(text_split[i]) + weight); // increase weight
            }
        }
    }
    public static void ReadStopWords() throws FileNotFoundException, IOException {
        try {
            File file = new File("D:\\Projects\\Search-Engine\\Search-Engine\\webCrawler\\src\\main\\java\\com\\company\\stopwords.txt");
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

    private static HashMap<String, Integer> removeStopWords (HashMap<String, Integer> M){
        HashMap<String, Integer> Filtered = new HashMap<>();

        for(String word : stopWords) {
            if(M.containsKey(word))
                System.out.println( M.remove(word));
        }

        return M;
    }
}

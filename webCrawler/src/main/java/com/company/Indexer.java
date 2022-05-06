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

import static com.company.Phrase_Searching.*;

public class Indexer {
    public static DB db = new DB();
    public static Map<String, Integer> tag = new HashMap<>();
    public static List<String> stopWords = new ArrayList<>();
    public static HashMap<String, List<Integer>> wordMap = new HashMap<>();
    public static HashMap<String, List<Integer>> wordMap_phrase = new HashMap<>();
    public static Stemmer stemmer = new Stemmer();

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
        //tag.put("span",3);
    }

    public static void main(String[] args) throws IOException {
        setTags();
        ReadStopWords();
        int Count = (int)db.getAttr("Globals", "key","counter","value" );
        for (int i = 0; i < Count; i++) {
            String url = (String)db.getAttr("URLs","id", i,"url");
            Document doc = getDocument(url);
            if (doc != null) {
                for (Map.Entry<String, Integer> entry : tag.entrySet()) {
                    indexing(entry.getKey(), doc, entry.getValue());
                    Phrase_Searching(entry.getKey(), doc, entry.getValue());
                }
                optimize_Phrase();
//          for (Map.Entry<String,List<Integer>> entry : wordMap.entrySet()) {
//              System.out.println(entry.getKey() + " ---> " + entry.getValue());
//          }
                insert("words", wordMap, url);
                insert("Phrase", wordMap_phrase, url);
                wordMap.clear();
                wordMap_phrase.clear();
            }
        }
    }
    public static void indexing(String tag, Document doc, int weight) {
        String temp_text =doc.select(tag).text();
        String[] text_split = temp_text.split(" "); // split the text
        text_split = removeStopWords(text_split);
        for (String s : text_split)
            if (!s.equals("")) {
                String lowerCaseString=s.toLowerCase();
                String Temp = stemmer.Stemming(lowerCaseString);
                if(!db.isExists("stemming","key",Temp))
                {
                    ArrayList<String>keys=new ArrayList<String>();
                    ArrayList<Object>values=new ArrayList<Object>();
                    keys.add("key");
                    values.add(Temp);
                    keys.add("array");
                    ArrayList<String> fir=new ArrayList<String>();
                    fir.add(lowerCaseString);
                    values.add(fir);
                    db.insertToDB("stemming",keys,values);
                }
                else
                {
                    db.pushToList("stemming","key",Temp,"array",lowerCaseString);
                }
                wordMap.putIfAbsent(lowerCaseString, new ArrayList<Integer>() {{
                    add(0);
                    add(0);
                }}); // For initial insertion
                wordMap.put(lowerCaseString, new ArrayList<>() {{
                    add(wordMap.get(lowerCaseString).get(0) + weight);
                    add(wordMap.get(lowerCaseString).get(1) + 1);
                }}); // Increase weight & TF of each word
            }
    }

    public static void insert(String collec, HashMap<String, List<Integer>> words, String url) {
        for (Map.Entry<String,List<Integer>> entry : words.entrySet()) {
            if (db.isExists(collec, "word", entry.getKey())) {
                int DF = (int) db.getAttr(collec, "word", entry.getKey(), "DF");
                db.updateDB(collec, "word", entry.getKey(), "DF", DF + 1);
                BasicDBObject doc = new BasicDBObject("TF", entry.getValue().get(1));
                doc.append("weight", entry.getValue().get(0));
                doc.append("url", url);
                ArrayList<BasicDBObject> arr = (ArrayList<BasicDBObject>) db.getAttr(collec, "word", entry.getKey(), "urls");
                System.out.println(arr);
                arr.add(doc);
                System.out.println(arr);
                db.updateDB(collec, "word", entry.getKey(), "urls", arr);
            }
            else {
                ArrayList<String> keys = new ArrayList<>(){{add("word"); add("urls"); add("DF");}};
                ArrayList<BasicDBObject> urls = new ArrayList<BasicDBObject>();
                BasicDBObject doc = new BasicDBObject("TF", entry.getValue().get(1));
                doc.append("weight", entry.getValue().get(0));
                doc.append("url", url);
                urls.add(doc);
                ArrayList<Object> values = new ArrayList<>(){{add(entry.getKey()); add(urls); add(1);}};
                db.insertToDB(collec, keys, values);
            }
        }
    }

    public static void ReadStopWords() throws FileNotFoundException, IOException {
        try {
            File file = new File("stopwords.txt");
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

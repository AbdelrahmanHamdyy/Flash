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
    public static final DB db = new DB(); // Database
    public static Map<String, Integer> tags = new HashMap<>(); // HTML Tags
    public static List<String> stopWords = new ArrayList<>(); // Stop Words List
    public static int urlsCount; // Number Of URLs

    public static HashMap<String, HashMap<String,IndexerObject>> IndexerWords = new HashMap<>();
    // (Contains each word and its corresponding url + array of indexer object)

    public static HashMap<String, HashSet<String>> stemmedWords = new HashMap<>();
    // (Contains each word and its stemmed derivations)

    public static Stemmer stemmer = new Stemmer(); // Stemmer

    // **** Paragraphs ****
    public static long numberOfParagraphs;
    public static ArrayList<Long> paragraphs = new ArrayList<Long>();
    public static HashMap<String,Integer>NumberOfWords;
    // Multi-threading
    private ArrayList<Thread> threads;
    private int numOfThreads;

    private class IndexThread implements Runnable {
        private int start;
        private int end;
        IndexThread(int i, int j) {
            start = i;
            end = j;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                String url = (String) db.getAttr("URLs","id", i,"url");
                Integer numOfWords=(int)db.getAttr("URLs","id", i,"NumberOfWords");
                NumberOfWords.put(url,numOfWords);
                Document doc = null;
                try {
                    doc = getDocument(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int pageIndex = 0;
                paragraphs.clear();
                if (doc != null) {
                    RunIndexer(doc, url, pageIndex);
                    ArrayList<Long>temp=(ArrayList<Long>)db.getAttr("URLs", "url", url, "paragraphs");
                    temp.addAll(paragraphs);
                    db.updateDB("URLs", "url", url, "paragraphs", temp);
                }
            }
        }
    }

    Indexer(int num) throws IOException {
        NumberOfWords=new HashMap<String,Integer>();
        urlsCount = (int) db.getAttr("Globals", "key","counter","value" );
        System.out.println("Indexer Created..");
        numOfThreads = Math.min(num, urlsCount);
        threads = new ArrayList<Thread>(numOfThreads);
        int s = 0;
        int quantity = urlsCount / numOfThreads;
        int rem = urlsCount % numOfThreads;
        System.out.println("numOfURLs : " + urlsCount + " numOfThreads : " + numOfThreads + " quantity : " + quantity + " rem : " + rem);
        setTags();
        ReadStopWords();
        setCounter();
        numberOfParagraphs = (Long) db.getAttr("Globals","key","paragraphsCounter","value" );
        for (int i = 0; i < numOfThreads; i++) {
            int e = s + quantity;
            if (rem != 0) {
                e++;
                rem--;
            }
            System.out.println(i + " Starts with : " + s + " Ends with : " + e);
            Thread currentThread = new Thread(new Indexer.IndexThread(s, e));
            s = e;
            currentThread.setName(Integer.toString(i));
            threads.add(currentThread);
            Integer id = (int) (long) currentThread.getId();
            currentThread.start();
        }
        for (Thread i : threads) {
            try {
                i.join();
            } catch (InterruptedException exe) {
                System.out.println("Error in joining thread " + i.getName());
            }
        }
        insertToIndexer();
        insertToStem();
        db.updateDB("Globals","key","paragraphsCounter","value",numberOfParagraphs);
    }

    public static Document getDocument(String url) throws IOException {
        try {
            Connection connect = Jsoup.connect(url);
            Document doc = connect.get();
            if(connect.response().statusCode() == 200)
                return doc;
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public static void setTags() {
        // Weights
        tags.put("title",100);
        tags.put("h1",40);
        tags.put("h2",20);
        tags.put("h3",10);
        tags.put("h4",8);
        tags.put("h5",6);
        tags.put("h6",5);
        tags.put("p",4);
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

    public static void RunIndexer(Document doc, String url, int index) {
        for (Map.Entry<String, Integer> entry : tags.entrySet()) {
            synchronized(db) {
                indexing(entry.getKey(), doc, entry.getValue(), url, index);
            }
        }
    }



    public static void indexing(String tag, Document doc, int weight,String url, int pageIndex) {
        Elements elements = doc.select(tag);
        for (Element i : elements) {
            // Insert Paragraphs
            ArrayList<String> keys = new ArrayList<String>();
            ArrayList<Object> values = new ArrayList<Object>();
            int sz=i.text().length();
            paragraphs.add(numberOfParagraphs);
            keys.add("id");
            values.add(numberOfParagraphs++);
            keys.add("url");
            values.add(url);
            keys.add("content");
            if(sz<=400)
            {
                values.add(i.text());
            }
            else
            {
                values.add(i.text().substring(0,300));
            }
            db.insertToDB("Paragraphs", keys, values);

            String temp_text = i.text().toLowerCase(Locale.ROOT);
            String[] text_split = temp_text.split(" "); // split the text
            text_split = removeStopWords(text_split);

            for (String s : text_split) {
                if (!s.equals("")) {
                    String lowerCaseString = s.toLowerCase();
                    String Temp = stemmer.Stemming(lowerCaseString);
                    stemmedWords.putIfAbsent(Temp, new HashSet<String>());
                    stemmedWords.get(Temp).add(lowerCaseString);
                    IndexerWords.putIfAbsent(lowerCaseString, new HashMap<>());
                    IndexerWords.get(lowerCaseString).putIfAbsent(url,new IndexerObject());

                    IndexerObject Obj = IndexerWords.get(lowerCaseString).get(url);
                    Obj.TF += 1;
                    if(Obj.paragraphID == -1)
                        Obj.paragraphID = numberOfParagraphs - 1;
                    Obj.Weight += weight;
                    Obj.positions.add(pageIndex);

                    pageIndex++;
                }
            }
        }
    }

    public static void insertToIndexer() {
        for (Map.Entry<String, HashMap<String, IndexerObject>> entry : IndexerWords.entrySet()) {
            ArrayList<String> keys = new ArrayList<>() {{
                add("word");
                add("urls");
            }};
            int DF=entry.getValue().size();
            ArrayList<BasicDBObject> urls = new ArrayList<BasicDBObject>();
            for (Map.Entry<String, IndexerObject> entry2 : entry.getValue().entrySet()) {
                BasicDBObject doc = new BasicDBObject();
                int TF=entry2.getValue().TF;
                doc.append("weight", entry2.getValue().Weight);
                doc.append("url", entry2.getKey());
                doc.append("positions",  entry2.getValue().positions);
                doc.append("paragraphID", entry2.getValue().paragraphID);
                double NTF=((double)TF)/NumberOfWords.get( entry2.getKey());
                double IDF=Math.log(((double)urlsCount)/DF);
                doc.append("TF-IDF",NTF*IDF);
                urls.add(doc);
            }
            ArrayList<Object> values = new ArrayList<>() {{
                add(entry.getKey());
                add(urls);
            }};
            db.insertToDB("words", keys, values);
        }
    }
    public static void insertToStem(){
        for (Map.Entry<String, HashSet<String>> entry : stemmedWords.entrySet()) {
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

    /*public static String[] removeStopWords (String[] M){
        ArrayList<String> MList = new ArrayList();
        for(String word : M) {
            word = word.replaceAll("[^a-zA-Z]","");
            if(!word.equals("") && !stopWords.contains(word))
                MList.add(word);
        }
        return MList.toArray(new String[0]);
    }*/

    public static String[] removeStopWords (String[] M){
        for(String word : stopWords) {
            for (int i = 0; i < M.length; i++) {
                M[i] = M[i].replaceAll("[^a-zA-Z0-9]","");
                if (M[i].toLowerCase(Locale.ROOT).equals(word))
                    M[i] = "";
                //M = ArrayUtils.remove(M, i);
            }
        }
        return M;
    }

}
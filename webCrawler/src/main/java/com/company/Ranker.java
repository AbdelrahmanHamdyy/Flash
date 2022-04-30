package com.company;

import org.bson.Document;

import java.io.IOException;
import java.util.*;

public class Ranker {
    public static DB db = new DB();
    public static List<Pair> urls;
    public static List<Pair> stemmedUrls;
    public static HashMap<String, Double> sortedMap = new HashMap<>();
    Ranker(List<Pair> list, List<Pair> stemmed) {
        urls = list;
        stemmedUrls = stemmed;
    }

    public static void sortByValue(boolean order) {
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(sortedMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
    }

    public static void rank(List<Pair> list, int stem) {
        for (Pair P : list) {
            for (Document object : (List<Document>) P.first) {
                String url = (String) object.get("url");
                long TotalWords = (long) db.getAttr("URLs", "url", url, "NumberOfWords");
                int TotalDocuments = (int) db.getAttr("Globals", "key", "counter", "value");
                int DF = (Integer) P.second;
                double IDF = Math.log((double) TotalDocuments / DF);
                int TF = (int) object.get("TF");
                double NormalizedTF = (double) TF / TotalWords;
                double TF_IDF = NormalizedTF * IDF;
                int weight = (int) object.get("weight");
                double Priority = TF_IDF * weight * 100; // Multiplied by 100 to obtain a reasonable range
                int factor;
                if (stem == 0) factor = 2; // For original words
                else factor = 1; // For stemmed words
                if (sortedMap.containsKey(url))
                    sortedMap.replace(url, Priority + sortedMap.get(url) + (factor * 5)); // Factor * 5 for urls containing
                                                                                     // different words in the search query
                else
                    sortedMap.put(url, Priority + factor);
            }
        }
        sortByValue(false);
    }

    // For testing
    public static void print() {
        for (Map.Entry<String, Double> entry : sortedMap.entrySet())
            System.out.println(entry.getKey()+" ---> "+ entry.getValue());
    }

    public static void main(String[] args) throws IOException {
        //String query = WebInterface.getInput();
        queryProcessor myq = new queryProcessor("offer product");
        urls = myq.list;
        stemmedUrls = myq.stemmed;
        rank(urls, 0);
        rank(stemmedUrls, 1);
        print();
    }
}
// query: Computer Engineering Cairo University
// Outer loop:
//    Iterates over the lists of each word and DF
//          Inner loop:
//              Iterates over each list and calculates priority

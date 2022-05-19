package com.company;

import org.bson.Document;

import java.util.*;

public class Ranker {
    private DB db;
    private HashMap<String, Double> sortedMap;
    private List<String> output;
    private HashMap<String, List<String>> urlResults;
    public Ranker() {
        sortedMap = new HashMap<>();
        urlResults = new HashMap<>();
        output = new ArrayList<String>();
        db = new DB();
    }

    public void sortByValue(boolean order) {
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

    public void rank(ArrayList<ArrayList<Document>> list, int stem) {
        for (ArrayList<Document> P : list) {
            for (Document object : P) {
                System.out.println(object);
                String url = (String) object.get("url");
                double TF_IDF = (double)object.get("TF-IDF");
                int pid = -1;
                if(object.containsKey("paragraphID"))
                     pid = (int) object.get("paragraphID");
                int weight = (int) object.get("weight");
                int popularity = (int) db.getAttr("URLs", "url", url, "popularity");
                double relevance = TF_IDF * weight;
                double Priority = relevance * popularity;
                if (sortedMap.containsKey(url)) {
                    sortedMap.replace(url, Priority + sortedMap.get(url) + (5 * stem));
                    // We multiply the stem factor by 5 if the url is already found because it makes more sense to
                    // give urls that contain more words from the search query a higher priority to be displayed
                }
                else {
                    List<String> TitleDesc = new ArrayList<String>();
                    String title = (String) db.getAttr("URLs", "url", url, "title");
                    TitleDesc.add(title);
                    String desc="No Paragraphs Found!";
                    if(pid != -1)
                        desc = (String) db.getAttr("Paragraphs", "id", pid, "content");
                    TitleDesc.add(desc);
                    urlResults.put(url, TitleDesc);
                    sortedMap.put(url, Priority + stem);
                }
            }
        }
    }


    // For testing
    public void print() {
        for (Map.Entry<String, Double> entry : sortedMap.entrySet())
            System.out.println(entry.getKey()+" ---> "+ entry.getValue());
    }

    // Output to query processor
    public List<String> getOutput() {
        sortByValue(false);
        for (Map.Entry<String, Double> entry : sortedMap.entrySet())
            output.add(entry.getKey());
        return output;
    }

    public HashMap<String, List<String>> getResults() {
        return urlResults;
    }

}
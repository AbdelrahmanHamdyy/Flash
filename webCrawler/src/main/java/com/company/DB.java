package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class DB {
    private static final MongoClient client = MongoClients.create();
    private final MongoDatabase db = client.getDatabase("SearchEngine");


    public void insertToDB(String collectionName, ArrayList<String> key, ArrayList<Object> value) {
        MongoCollection<Document> col = db.getCollection(collectionName);
        int size=key.size();
        Document doc = new Document();
        for(int i=0;i<size;i++)
        {
            doc.append(key.get(i),value.get(i));
        }
        col.insertOne(doc);
    }


    public boolean isExists(String collectionName, String key, String value)
    {
        MongoCollection<Document> col = db.getCollection(collectionName);
        Document doc = col.find(eq(key,value)).first();
        System.out.println(doc);
        if (doc != null)
            return true;
        return false;
    }

    public Object getAttr(String collectionName, String key, Object value,String attr) {
        MongoCollection<Document> col = db.getCollection(collectionName);
        Document doc = col.find(eq(key,value)).first();
        return doc.get(attr);
    }

    public void updateDB(String collectionName, String key, String value,String attr,Object newVal)
    {
        MongoCollection<Document> col = db.getCollection(collectionName);

        Document query = col.find(eq(key,value)).first();

        Document newDocument = new Document();
        newDocument.put(attr, newVal);

        Document updateObject = new Document();
        updateObject.put("$set", newDocument);

        col.updateOne(query, updateObject);

    }

    public void insertToIndexer(HashMap<String, List<Integer>> words, String url) {
        for (Map.Entry<String,List<Integer>> entry : words.entrySet()) {
            if (isExists("words", "word", entry.getKey())) {
                int DF = (int) getAttr("words", "word", entry.getKey(), "DF");
                updateDB("words", "word", entry.getKey(), "DF", DF + 1);
                Document doc = new Document();
                doc.append("weight", entry.getValue().get(0));
                doc.append("url", url);
                ArrayList<Document> arr = (ArrayList<Document>) getAttr("words", "word", entry.getKey(), "urls");
                System.out.println(arr);
                arr.add(doc);
                System.out.println(arr);
                updateDB("words", "word", entry.getKey(), "urls", arr);
            }
            else {
                ArrayList<String> keys = new ArrayList<>(){{add("word"); add("urls"); add("DF");}};
                BasicDBObject doc = new BasicDBObject("TF", entry.getValue().get(1));
                doc.append("weight", entry.getValue().get(0));
                doc.append("url", url);
                ArrayList<Object> values = new ArrayList<>(){{add(entry.getKey()); add(doc); add(1);}};
                insertToDB("words", keys, values);
            }
        }
    }
}
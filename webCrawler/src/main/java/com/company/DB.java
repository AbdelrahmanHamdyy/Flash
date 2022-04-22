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
import java.util.List;
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

    public void updateDB(String collectionName, String key, String value,String attr,int newVal)
    {
        MongoCollection<Document> col = db.getCollection(collectionName);

        Document query = col.find(eq(key,value)).first();

        Document newDocument = new Document();
        newDocument.put(attr, newVal);

        Document updateObject = new Document();
        updateObject.put("$set", newDocument);

        col.updateOne(query, updateObject);

    }
}
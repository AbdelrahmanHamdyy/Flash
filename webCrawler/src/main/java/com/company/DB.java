package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

public class DB {
    private final MongoDatabase db;

    DB() {
        MongoClient client = MongoClients.create();
        db = client.getDatabase("SearchEngine");
    }

    public void insertToDB(String collectionName, ArrayList<String> key, ArrayList<Object> value) {
        MongoCollection<Document> col = db.getCollection(collectionName);
        int size = key.size();
        Document doc = new Document();
        for(int i = 0; i < size; i++)
            doc.append(key.get(i),value.get(i));
        col.insertOne(doc);
    }

    public boolean isExists(String collectionName, String key, String value)
    {
        MongoCollection<Document> col = db.getCollection(collectionName);
        Document doc = col.find(eq(key,value)).first();
        return doc != null;
    }

    public Object getAttr(String collectionName, String key, Object value,String attr) {
        MongoCollection<Document> col = db.getCollection(collectionName);
        Document doc = col.find(eq(key,value)).first();
        assert doc != null;
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

        assert query != null;
        col.updateOne(query, updateObject);

    }
    public void pushToList(String collectionName, String key, String value,String array,Object newVal)
    {
        ArrayList<String>arr=(ArrayList<String>)getAttr(collectionName,key,value,array);
        if(!arr.contains(newVal))
        {
            MongoCollection<Document> col = db.getCollection(collectionName);
            Bson filter = Filters.eq(key, value);
            Bson update = Updates.push(array, newVal);
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER);
            col.findOneAndUpdate(filter, update, options);
        }
    }

}
package com.company;

import com.mongodb.MongoInterruptedException;
import com.mongodb.client.*;

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
        if(doc == null)
            return null;
        return doc.get(attr);
    }
    public Object getListOf(String collectionName, String attr)
    {
        MongoCollection<Document> col = db.getCollection(collectionName);

        FindIterable<Document> ListOfdoc = col.find();
        ArrayList<Object>result=new ArrayList<Object>();
        for(Document i:ListOfdoc)
        {
            result.add(i.get(attr));
        }
        return result;
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
    public Object getTwoAttrs(String collectionName,String attr1,String attr2)
    {
        MongoCollection<Document> col = db.getCollection(collectionName);

        FindIterable<Document> ListOfdoc = col.find();
        ArrayList<Object>result=new ArrayList<Object>();
        for(Document i:ListOfdoc)
        {
            Pair p=new Pair();
            p.first=i.get(attr1);
            p.second=i.get(attr2);
            result.add(p);
        }
        return result;
    }
}
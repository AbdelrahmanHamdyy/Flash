package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import static com.mongodb.client.model.Filters.exists;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bson.Document;

public class DB {
    private static final MongoClient client = MongoClients.create();
    private final MongoDatabase db = client.getDatabase("SearchEngine");

    public static void db_connect() {
        String connectionString = "mongodb://sudo:123456@localhost";
        MongoIterable<String> dbNames = client.listDatabaseNames();
        for (String Name : dbNames) {
            System.out.println(Name);
        }
    }

    public void insertToDB(String collectionName, String key, String value) {
        MongoCollection<Document> col = db.getCollection(collectionName);
        Document doc = new Document(key, value);
        col.insertOne(doc);
    }

    public boolean checkCompactString(String cs) {
        MongoCollection<Document> col = db.getCollection("CompactStrings");
        Document doc = (Document) col.find(exists(cs)).first();
        if (doc == null)
            return true;
        return false;
    }

    public String getUrl(int i) {
        final String[] url = {""};
        MongoCollection<Document> col = db.getCollection("URLs");
        Consumer<Document> extractUrls = doc -> {
            if (doc.get(Integer.toString(i)) != null)
                url[0] = doc.get(Integer.toString(i)).toString();
        };
        col.find().forEach(extractUrls);
        return url[0];
    }

    public int getUrlsCount() {
        MongoCollection<Document> col = db.getCollection("URLs");
        final int[] count = new int[1];
        Consumer<Document> extractUrls = doc -> {
            if (doc.get("counter") != null)
                count[0] = (int) doc.get("counter");
        };
        col.find().forEach(extractUrls);
        return count[0];
    }
}
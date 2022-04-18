package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class DB {
    public static void db_connect() {
        String connectionString = "mongodb://sudo:123456@localhost";
        MongoClient client = MongoClients.create();
        MongoDatabase db = client.getDatabase("test");
        MongoIterable<String> dbNames = client.listDatabaseNames();
        for (String Name : dbNames) {
            System.out.println(Name);
        }
    }
}
package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class Indexer {
    private static List<String> urls;
    private static DB db = new DB();
    public static void setUrls() {
        urls = db.getUrls();
    }

    public static Document getDocument(String url) throws IOException {
        Connection connect= Jsoup.connect(url);
        Document doc = connect.get();
        return doc;
    }

    public static void main(String[] args) throws IOException {
        setUrls();
        int Count = urls.size();
        for (String url : urls) {
            Document doc = getDocument(url);
            // For each document:
            // Convert to text and split into words (remove html tags, etc..)
            // Remove Stop words
            // Stemming
            // Store each word in db along with ...?
        }
    }
}

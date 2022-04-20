package com.company;

import java.util.List;

public class Indexer {
    private static List<String> urls;
    private DB db = new DB();
    public void setUrls() {
        urls = db.getUrls();
    }
}

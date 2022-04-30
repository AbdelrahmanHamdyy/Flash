package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class WebInterface {
    public static void main(String[] args) throws IOException {
        // File HTML = new File("D:\\Github\\Search-Engine\\Voice Search\\Display.html");
        getInput();
    }

    public static String getInput() throws IOException {
        //File input = new File("D:\\Github\\Search-Engine\\Voice Search\\index.html");
        //Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Document doc = Indexer.getDocument("http://127.0.0.1:5500/index.html");
        System.out.println(doc.select("input").val());
        return null;
    }
}

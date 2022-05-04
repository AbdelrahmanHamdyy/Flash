package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class jsoupUtilities {
    public static String getTagIfContains(String url,String tag,String word)
    {
        Document doc = fetch(url);
        Elements elements=doc.select(tag);
        for(Element i:elements)
        {
            String curr=i.text();
            if(curr.contains(word))
            {
                return curr;
            }
        }
        return "";
    }
    public static Document fetch(String url)
    {
        try {
            Connection connect= Jsoup.connect(url);
            Document doc=connect.get();
            if(connect.response().statusCode()==200)
            {
                return doc;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}

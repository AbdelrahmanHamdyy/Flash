
package com.company;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class mtWebCrawler implements Runnable {
    private static final int MAX_DEPTH =3;
    private  Thread thread;
    private  String first_link;
    private ArrayList<String > visitedLinks=new ArrayList<String>();
    private ArrayList<String > String_compact=new ArrayList<String>();
    private int ID;

    public mtWebCrawler(String link ,int num){
        System.out.print("WebCrawler created");
        first_link=link;
        ID=num;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run(){
        crawl(1,first_link);
    }
    private  void crawl (int level , String url){
        if(level <=MAX_DEPTH){
            Document doc=request(url);
           /* if(doc !=null){
                for(Element link : doc.select("a[href]")){
                    String next_link = link.absUrl("href");
                    if(visitedLinks.contains(next_link)==false){
                        crawl(level++,next_link);
                    }
                }
            }*/
        }
    }
    private Document request(String url){
        try {
            Connection con = Jsoup.connect(url);
            Document doc =con.get();
            if(con.response().statusCode()==200) {
                System.out.println("\n**Bot ID:"
            + ID +" Receivved webpage at ");
            int count_string=0;
            String compact="";
            String title=doc.title();
           String p_text =doc.select("p").text();
           String[] p_text_split = p_text.split(" ");
                System.out.println(p_text);
                for(int i=0;i<p_text_split.length;i++) {
                    if (!p_text_split[i].equals("")) {
                        compact+=p_text_split[i].charAt(0);
                        count_string++;
                        if(count_string==50)
                            break;
                    }
                }
                if(count_string!=50) {
                    String span_text =doc.select("span").text();
                    String[] span_text_split = span_text.split(" ");
                    System.out.println(span_text);
                    for(int i=0;i<span_text_split.length;i++) {
                        if (!span_text_split[i].equals("")) {
                            compact+=span_text_split[i].charAt(0);
                            count_string++;
                            if(count_string==50)
                                break;
                        }
                    }
                }
                if(count_string!=50) {
                    String div_text =doc.select("div").text();
                    String[] div_text_split = div_text.split(" ");
                    System.out.println(div_text);
                    for(int i=0;i<div_text_split.length;i++) {
                        if (!div_text_split[i].equals("")) {
                            compact+=div_text_split[i].charAt(0);
                            count_string++;
                            if(count_string==50)
                                break;
                        }
                    }
                }
            System.out.println(compact);
                if(String_compact.contains(compact)) {
                    System.out.println("SPyyyyyyyyyyyyyyyyyyyyyyyyyy");
                    return null;
                }

            visitedLinks.add(url);
            String_compact.add(compact);
            return doc;
            }
            return null;
        } catch (IOException e) {
           return null;
        }
    }
    public Thread getThread(){
     return thread;
    }
}


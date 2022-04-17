
package com.company;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Crawler {
    private static HashSet<String> links=new HashSet<String>();
    private int numOfThreads;
    private ArrayList<String>myLinks;
    private class myThread implements Runnable{
        private int start;
        private int end;
        myThread(int s,int e)
        {
            start=s;
            end=e;
        }
        @Override
        public void run() {
            //System.out.println(Thread.currentThread().getName()+" Starts with : "+Integer.toString(start)+" Ends with : "+Integer.toString(end));
            for(int i=start;i<end;i++)
            {
                //System.out.println(Thread.currentThread().getName()+" crawls with : "+myLinks.get(i));
                crawl(myLinks.get(i));
            }
        }
    }
    public Crawler(String link,int num)  {
        myLinks=new ArrayList<String>();
        System.out.println("WebCrawler created");
        try {
            Connection connect = Jsoup.connect(link);
            Document doc = connect.get();
            Elements childrenLinks = doc.select("a[href]");
            for (Element child : childrenLinks) {
                String childLink = child.absUrl("href");
                myLinks.add(childLink);
            }
            int numOfLinks = myLinks.size();
            numOfThreads = Math.min(num, numOfLinks);
            ArrayList<Thread> threads = new ArrayList<Thread>(numOfThreads);
            int s = 0;
            int quantity = numOfLinks / numOfThreads;
            int rem = numOfLinks % numOfThreads;
            System.out.println("numOfLinks : " + numOfLinks + " numOfThreads : " + numOfThreads + " quantity : " + quantity + " rem : " + rem);
            for (int i = 0; i < numOfThreads; i++) {
                int e = s + quantity;
                if (rem != 0) {
                    e++;
                    rem--;
                }
                //System.out.println(i+" Starts with : "+s+" Ends with : "+e);
                Thread temp = new Thread(new myThread(s, e));
                s = e;
                temp.setName(Integer.toString(i));
                threads.add(temp);
                //System.out.print(threads.get(i).getName());
                temp.start();
            }
            for (Thread i : threads) {
                try {
                    i.join();
                } catch (InterruptedException exe) {
                    System.out.println("Error with joining");
                }
            }
        }
        catch (IOException e) {
        System.out.println("Error");
    }
    }

    public void crawl(String url)
    {
        if(links.size()>100)
        {
            return;
        }
        Document doc =request(url);
        if(doc!=null)
        {
            Elements childrenLinks=doc.select("a[href]");
            for(Element child:childrenLinks)
            {
                String childLink=child.absUrl("href");
                crawl(childLink);
            }
        }
    }
    public Document request(String url)
    {
        try {
            Connection connect= Jsoup.connect(url);
            Document doc=connect.get();
            synchronized (links)
            {
                if(!links.contains(url)&& connect.response().statusCode()==200)
                {
                    links.add(url);
                    System.out.println(Thread.currentThread().getName()+" : "+url);
                    return doc;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}


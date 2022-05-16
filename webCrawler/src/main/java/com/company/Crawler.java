package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.net.*;

public class Crawler {
    private int numOfThreads;
    private ArrayList<String>myLinks;
    private long NumberOfWords;
    private CompactString CS = new CompactString();
    private DB db = new DB();
    private int numberOfLinks;
    ArrayList<Thread> threads;
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
            for(int i=start;i<end;i++)
            {
                //System.out.println(Thread.currentThread().getName()+" crawls with : "+myLinks.get(i));
                if(Thread.currentThread().isInterrupted())
                    break;
                try {
                    crawl(myLinks.get(i));
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("~~");
                }
            }
        }
    }

    public Crawler(int num)  {
        setCounter();
        numberOfLinks=(int)db.getAttr("Globals", "key","counter","value" );
        System.out.println("counter : "+numberOfLinks);
        myLinks=(ArrayList<String>)db.getListOf("CrawlerLinks","url");
        System.out.println("WebCrawler is created"+myLinks);

        int numOfLinks = myLinks.size();
        numOfThreads = Math.min(num, numOfLinks);
        threads= new ArrayList<Thread>(numOfThreads);
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
            temp.start();
        }
        for (Thread i : threads) {
            try {
                i.join();
            } catch (InterruptedException exe) {
                System.out.println("Error with joining");
            }
        }
        db.updateDB("Globals","key","counter","value",numberOfLinks);
    }

    public void crawl(String url) throws IOException {
        Document doc = null;

        synchronized (db)
        {
            if(Thread.currentThread().isInterrupted())
                return;
            if(numberOfLinks >= 200)
            {
                for(Thread t:threads)
                    t.interrupt();
                return;
            }
            try {
                if(!CheckRobots(url))
                    return;
            } catch(Exception e) {
                System.out.println("(Robots.txt): Exception Thrown!");
            }
            doc = request(url);
            if(doc == null)
                return;
            String C_String = CS.String_Compact(doc);
            if (db.isExists("URLs", "CompactString", C_String)) {
                String dbUrl = (String) db.getAttr("URLs", "CompactString", C_String, "url");
                int popularity = (int) db.getAttr("URLs", "url", dbUrl, "popularity");
                db.updateDB("URLs", "url", dbUrl, "popularity", popularity + 1);
                return;
            }
            ArrayList<String>keys=new ArrayList<String>();
            ArrayList<Object>values=new ArrayList<Object>();
            keys.add("url");values.add(url);
            keys.add("id");values.add(numberOfLinks);
            keys.add("CompactString");values.add(C_String);
            keys.add("popularity");values.add(1);
            ArrayList<Integer>paragraphs=new ArrayList<Integer>();
            keys.add("paragraphs");values.add(paragraphs);
            String title = doc.select("title").text();
            //String description = doc.select("meta[name=description]").attr("content");
            String link= doc.body().text();
            NumberOfWords=link.length();
            keys.add("title"); values.add(title);
            //keys.add("description"); values.add(description);
            keys.add("NumberOfWords");values.add(NumberOfWords);
            db.insertToDB("URLs",keys,values);
            numberOfLinks++;
        }

        Elements childrenLinks=doc.select("a[href]");
        for(Element child:childrenLinks)
        {
            String childLink=child.absUrl("href");
            crawl(childLink);
        }

    }
    public Document request(String url)
    {
        try {
            if (db.isExists("URLs", "url", url))
                return null;
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

    private void setCounter() {
        if(db.isExists("Globals","key","counter"))
            return;
        ArrayList<String> keys = new ArrayList<>();
        keys.add("key");
        keys.add("value");
        ArrayList<Object> values = new ArrayList<>();
        values.add("counter");
        values.add(0);
        db.insertToDB("Globals", keys, values);
    }

    public boolean CheckRobots(String MyUrl) throws IOException {
        ArrayList<String>ForbiddenLinks=new ArrayList<String>();
        URL Specific_Url=new URL(MyUrl);
        String Robots_Link;
        //we must take a url that has www.
        if(Specific_Url.getHost().contains("www."))
        {
            Robots_Link = Specific_Url.getProtocol() + "://" + Specific_Url.getHost() + "/robots.txt";
        }
        else
            return true;
        Connection connect = Jsoup.connect(Robots_Link);
        System.out.println(Robots_Link);
        //here we take the whole content of url/robots.txt
        Document SpecialDoc = connect.get();
        String FullContent=SpecialDoc.outerHtml();
        //now we start searching for what we cannot search in
        int StartIndex=FullContent.indexOf("User-agent: *");
        System.out.println(StartIndex);
        if(StartIndex!=-1)
        {
            //now we find our User-agent:*
            //we will start to crop the whole content until we find User-agent:*
            //here is the content from User-agent:* till the end
            System.out.println("UserAgentFound");
            String subOne = FullContent.substring(StartIndex);
            //we will crop User-agent:* it has done it's mission
            int SubStart=subOne.indexOf("Disallow");
            //that's content is from the first Disallow
            int SecondSubStart=subOne.indexOf("disallow");
            if(SecondSubStart!=-1&& SecondSubStart<SubStart)
                SubStart=SecondSubStart;
            if(SubStart==-1 && SecondSubStart!=-1)
                SubStart=SecondSubStart;
            if(SecondSubStart==-1 && SubStart==-1)
            {
                System.out.println("No Disallow");
                return true;
            }
            //now we make sure that we have just found disallow statement
            //we will crop the content until the first disallow we have found
            String subTwo=subOne.substring(SubStart);
            //here we will determine where is the end of the content we want
            //if we have another User-agent or the end of the document
            int EndIndex1=subTwo.indexOf("User-agent:");
            int EndIndex2=subTwo.indexOf("</body>");
            String FinalString;
            if(EndIndex1!=-1)
                FinalString=subTwo.substring(0,EndIndex1);
            else
                FinalString=subTwo.substring(0,EndIndex2);
            //now we will loop all over the content we found after User-agent:*
            String WhileString=FinalString;
            while(true)
            {
                //every WhileString will start with disallow so we want to skip it
                int WhileStart=WhileString.indexOf("/");
                //here is the content without the first disallow our content started with
                String Statement=WhileString.substring(WhileStart);
                WhileString=WhileString.substring(WhileStart);
                //then we check for the next disallow
                int WhileEnd=Statement.indexOf("Disallow");
                if(WhileEnd==-1)
                    WhileEnd=Statement.indexOf("disallow");
                if(WhileEnd==-1)
                    WhileEnd=Statement.length();
                String WantedString=Statement.substring(1,WhileEnd - 1);
                ForbiddenLinks.add(WantedString);
                //if there is'nt then we have reached the end and we will break
                if(WhileEnd==Statement.length())
                    break;
                WhileString=WhileString.substring(WhileEnd);
            }
        }
        else{
            System.out.println("No UserAgent");
        }
        for(int i=0;i< ForbiddenLinks.size();i++)
        {
            System.out.println(ForbiddenLinks.get(i));
            String Path = Specific_Url.getPath();
            String UnwantedStringTemp = ForbiddenLinks.get(i);
            int index=UnwantedStringTemp.indexOf('*');
            if(index!=-1)
            {
                if(index!=0) {
                    String EveryPlace = UnwantedStringTemp.substring(0, index);
                    if (Path.contains(EveryPlace))
                        return false;
                }
                else
                    return false;
            }
            if (UnwantedStringTemp == "")
                return false;
            if (Path.contains(UnwantedStringTemp))
                return false;
        }
        System.out.println(ForbiddenLinks.size());
        return true;
    }
}
package com.company;
import org.bson.Document;

import java.io.IOException;
import java.util.*;

public class queryProcessor {
    String[]words;
    private DB db;
    public ArrayList<ArrayList<Document>>list;
    public ArrayList<ArrayList<Document>>stemmed;
    private Stemmer stemmer ;
    private Ranker ranker;
    boolean isPhrase;
    private Phrase_Searching phraser;
    private HashMap<String,Integer> IDF;

    public queryProcessor(String query)
    {
        if(query.contains("\""))
        {
            isPhrase=true;
            query=query.substring(1,query.length()-1);
            phraser=new Phrase_Searching();
            System.out.println(query);
        }
        else
        {
            IDF=new HashMap<String,Integer>();
            isPhrase=false;
            stemmed=new ArrayList<ArrayList<Document>>();
            stemmer= new Stemmer();
            ranker=new Ranker();
            db= new DB();

        }
        list=new ArrayList<ArrayList<Document>>();
        words=query.split(" ");

    }
    private boolean Process()
    {
        int success= words.length;
        for(String s:words)
        {
            System.out.println(s+"*********************************");
            String lowerCaseString=s.toLowerCase();
            String str=stemmer.Stemming(lowerCaseString);
            System.out.println(str);
            ArrayList<String>all=(ArrayList<String>)db.getAttr("stemming","key",str,"array");
            if(all==null)
            {
                System.out.println("all is null*********************************");
                success-=1;
                continue;
            }
            int stemmedDF=0;
            ArrayList<Document>stemmedDocs=new ArrayList<Document>();
            for(String i:all)
            {
                if(i.equals(lowerCaseString))
                {
                    ArrayList<Document>docs=(ArrayList<Document>)db.getAttr("words","word",lowerCaseString,"urls");
                    list.add(docs);
                }
                else {
                    ArrayList<Document>docs=(ArrayList<Document>)db.getAttr("words","word",i,"urls");
                    stemmedDocs.addAll(docs);
                }
            }
            stemmed.add(stemmedDocs);
        }
        return success!=0;
    }

    private void Rank()
    {
        ranker.rank(list, 3);
        ranker.rank(stemmed, 1);

    }
    public Pair Run() throws IOException {
        Pair p=new Pair();
        if(isPhrase)
        {
            if(phraser.phrase(words))
            {
                p.first=phraser.getOutput();
                p.second=phraser.getResults();
                if(((List<String>)p.first).size()==0)
                    return null;
                return p;
            }
        }
        else
        {
            if(Process())
            {
                Rank();
                p.first=ranker.getOutput();
                p.second=ranker.getResults();
                return p;
            }
        }
        return null;
    }
}
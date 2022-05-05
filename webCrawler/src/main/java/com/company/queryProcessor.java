package com.company;
import org.bson.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class queryProcessor {
    String[]words;
    private DB db;
    public ArrayList<Pair>list;
    public ArrayList<Pair>stemmed;
    private Stemmer stemmer ;
    private Ranker ranker;
    public queryProcessor(String query)
    {
        list=new ArrayList<Pair>();
        stemmed=new ArrayList<Pair>();
        db= new DB();
        words=query.split(" ");
        stemmer= new Stemmer();
        ranker=new Ranker();
    }

    private boolean Process()
    {
        int success= words.length;
        for(String s:words)
        {
            String lowerCaseString=s.toLowerCase();
            String str=stemmer.Stemming(lowerCaseString);
            System.out.println(str);
            ArrayList<String>all=(ArrayList<String>)db.getAttr("stemming","key",str,"array");
            if(all==null)
            {
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
                    int DF=(int)db.getAttr("words","word",lowerCaseString,"DF");
                    Pair p=new Pair();
                    p.first=docs;
                    p.second=DF;
                    list.add(p);
                }
                else {
                    stemmedDocs.addAll((ArrayList<Document>)db.getAttr("words","word",i,"urls"));
                    stemmedDF+=((int)db.getAttr("words","word",i,"DF"));
                }
            }
            Pair p=new Pair();
            p.first=stemmedDocs;
            p.second=stemmedDF;
            stemmed.add(p);
        }
        return success!=0;
    }

    private void Rank()
    {
        ranker.rank(list, 2);
        ranker.rank(stemmed, 1);

    }
    public Pair Run()
    {
        if(Process())
        {
            Rank();
            Pair p=new Pair();
            p.first=ranker.getOutput();
            p.second=ranker.getResults();
            return p;
        }
        return null;
    }
}

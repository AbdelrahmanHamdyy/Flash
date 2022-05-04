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
    private HashMap<String,String>paragraphs;
    public queryProcessor(String query)
    {
        list=new ArrayList<Pair>();
        stemmed=new ArrayList<Pair>();
        db= new DB();
        words=query.split(" ");
        stemmer= new Stemmer();
        ranker=new Ranker();
        paragraphs=new HashMap<String,String>();
    }
    public HashMap<String,String> getParagraphs()
    {
        return paragraphs;
    }
    private void addParagraph(ArrayList<Document>docs,String word)
    {
        for(Document i:docs)
        {
            String url= (String) i.get("url");
            if(paragraphs.containsKey(url))
            {
                continue;
            }
            String newParagraph = jsoupUtilities.getTagIfContains(url,"p",word);
            if(!newParagraph.isEmpty())
            {
                paragraphs.put(url,newParagraph);
            }
        }
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
                    addParagraph(docs,i);
                    int DF=(int)db.getAttr("words","word",lowerCaseString,"DF");
                    Pair p=new Pair();
                    p.first=docs;
                    p.second=DF;
                    list.add(p);
                }
                else {
                    stemmedDocs.addAll((ArrayList<Document>)db.getAttr("words","word",i,"urls"));
                    addParagraph(stemmedDocs,i);
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
    public List<String> Run()
    {
        if(Process())
        {
            Rank();
            return ranker.getOutput();
        }
        return null;
    }
}

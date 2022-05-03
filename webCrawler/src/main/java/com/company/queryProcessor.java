package com.company;


import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class queryProcessor {
    String[]words;
    private DB db;
    public ArrayList<Pair>list;
    public ArrayList<Pair>stemmed;
    private static Stemmer stemmer = new Stemmer();
    public queryProcessor(String query)
    {
        list=new ArrayList<Pair>();
        stemmed=new ArrayList<Pair>();
        db= new DB();
        words=query.split(" ");
        for(String s:words)
        {
            String lowerCaseString=s.toLowerCase();
            String str=stemmer.Stemming(lowerCaseString);
            System.out.println(str);
            ArrayList<String>all=(ArrayList<String>)db.getAttr("stemming","key",str,"array");
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
                    System.out.println(stemmedDF+"++++++++\n");
                }
            }
            Pair p=new Pair();
            p.first=stemmedDocs;
            p.second=stemmedDF;
            stemmed.add(p);
        }
    }
    public static void main(String[] args)
    {
        File myFile = new File("input.txt");
        try {
            Scanner cin = new Scanner(myFile);
            String query=cin.next();
            queryProcessor myq=new queryProcessor(query);
            System.out.println(query);
            try {
                FileWriter myWriter = new FileWriter("output.txt");
                ArrayList<Pair>l=myq.list;
                int n=l.size();
                for(int i=0;i<n;i++)
                {
                    myWriter.write(String.valueOf((int)l.get(i).second));
                    myWriter.write("\n");
                    ArrayList<Document>temp=(ArrayList<Document>)l.get(i).first;
                    for(Document h:temp)
                    {
                        myWriter.write(String.valueOf((int)h.get("TF")));
                        myWriter.write("\n");
                        myWriter.write(String.valueOf((int)h.get("weight")));
                        myWriter.write("\n");
                        myWriter.write((String) h.get("url"));
                        myWriter.write("\n");
                    }
                }
                myWriter.write("***************************************\n");
                l=myq.stemmed;
                n=l.size();
                for(int i=0;i<n;i++)
                {
                    myWriter.write(String.valueOf((int)l.get(i).second));
                    myWriter.write("\n");
                    ArrayList<Document>temp=(ArrayList<Document>)l.get(i).first;
                    for(Document h:temp)
                    {
                        myWriter.write(String.valueOf((int)h.get("TF")));
                        myWriter.write("\n");
                        myWriter.write(String.valueOf((int)h.get("weight")));
                        myWriter.write("\n");
                        myWriter.write((String) h.get("url"));
                        myWriter.write("\n");
                    }
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}

package com.company;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Phrase_Searching {
    private  DB db = new DB();
    //private  Indexer indexer = new Indexer();
    private   Map<String, Integer> count = new HashMap<>();
    private   Map<String, Integer> URL = new HashMap<>();
    private  Map<String, Integer> temp = new HashMap<>();
    private    Map<String, ArrayList<ArrayList<Integer> >> indx = new HashMap<>();
    private  HashMap<String, List<String>> Results= new HashMap<>();
    private   List<String> link=new ArrayList<String>();
    public List<String> stopWords = new ArrayList<>();
    private    String[] query;
    public  boolean  phrase(String[]words) throws IOException {
        ReadStopWords();
        query= removeStopWords(words);

        int target =query.length;
        boolean flag=false;
        for(int i=0;i<query.length;i++)
        {
            flag=false;
            String lowerCaseString=query[i].toLowerCase();
            List<Document>docs=(List<Document>)db.getAttr("words","word",lowerCaseString,"urls");
            if(docs==null)
            {
                return false;
            }
            for (Document object :  docs) {
                String url = (String) object.get("url");
                ArrayList<Integer> post =(ArrayList<Integer>) object.get("positions");
                count.putIfAbsent(url, 0);
                count.put(url,count.get(url) +1 );
                indx.putIfAbsent(url, new ArrayList<ArrayList<Integer>>());
                indx.get(url).add(post);
                if( target==count.get(url)){
                    URL.putIfAbsent(url, 0);
                }
            }
        }

        check();

        return true;
    }
    public   void check (){
        for (Map.Entry<String, Integer> entry : URL.entrySet()) {
            ArrayList<Integer> first =indx.get(entry.getKey()).get(0);
            int n=indx.get(entry.getKey()).size();
            int count=0;
            boolean flag=true;
            for(int i=0;i<first.size();i++)
            {
                flag=true;
                for(int j=1;j<n;j++){
                    if(!indx.get(entry.getKey()).get(j).contains(indx.get(entry.getKey()).get(0).get(i)+j)){
                        flag= false;
                        break;
                    }
                }
                if(flag)
                    count++;
            }
            if (count > 0) {
                temp.put(entry.getKey(), count);
            }
        }
        sortByValue(false);
        for (Map.Entry<String, Integer> entry : temp.entrySet()){
            link.add(entry.getKey());
            List<String> TitleDesc = new ArrayList<String>();
            String title = (String) db.getAttr("URLs", "url", entry.getKey(), "title");
            List<Document> pra =(List<Document>) db.getAttr("URLs", "url", entry.getKey(), "paragraphs");
            TitleDesc.add(title);
            String  paragraphs=String.join(" ",query).toLowerCase();
            String result=paragraphs;
            for(int i=0;i<pra.size();i++){
                String original =(String) db.getAttr("Paragraphs", "id",pra.get(i) , "content");
                String content=original.toLowerCase();
                String[] contentArr=content.split(" ");
                contentArr=removeStopWords(contentArr);
                content=String.join(" ",contentArr);
                if(content.contains(paragraphs)){
                    result=original;
                    break;
                }
            }
            TitleDesc.add(result);
            Results.put(entry.getKey(), TitleDesc);
        }
    }
    public  void sortByValue(boolean order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(temp.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            temp.put(entry.getKey(), entry.getValue());
        }
    }
    public List<String> getOutput() {
        return link;
    }
    public HashMap<String, List<String>> getResults()
    {
        return Results;
    }
    public void ReadStopWords() throws FileNotFoundException, IOException {
        try {
            File file = new File("D:\\Github\\Search-Engine\\Project Files\\stopwords.txt");
            Scanner Reader = new Scanner(file);
            while (Reader.hasNextLine()) {
                stopWords.add(Reader.nextLine().toLowerCase(Locale.ROOT));
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }
    public String[] removeStopWords (String[] M){
        for(String word : stopWords) {
            for (int i = 0; i < M.length; i++) {
                M[i] = M[i].replaceAll("[^a-zA-Z0-9]","");
                if (M[i].toLowerCase(Locale.ROOT).equals(word))
                    M[i] = "";
            }
        }
        List<String> arr=new ArrayList<>();
        for (int i = 0; i < M.length; i++) {
            if(M[i] != "")
                arr.add(M[i]);
        }
        return arr.toArray(new String[0]);
    }
}
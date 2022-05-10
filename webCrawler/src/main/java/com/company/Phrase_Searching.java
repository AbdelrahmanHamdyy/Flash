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
    private static DB db;
    private static Map<String, Integer> count = new HashMap<>();
    private static Map<String, Integer> URL = new HashMap<>();
    private static  Map<String, ArrayList<ArrayList<Integer> >> indx = new HashMap<>();
//    public static void Phrase_Searching (String tag, Document doc, int weight){
//
//        String temp_text="";
//        for(int i=0;i<doc.select(tag).size() ;i++) {
//            temp_text+=doc.select(tag).get(i).text();
//            temp_text+=" , ";
//        }
//        temp_text = temp_text.replaceAll("[^a-zA-Z0-9\\s]"," , ").toLowerCase(Locale.ROOT);
//        String[]  text_split;
//        text_split= temp_text.split(" ");
//        text_split = Phrase_StopWords(text_split);
//        temp_text = String.join(" ",text_split);
//        text_split= temp_text.split(",");
//        text_split= temp_text.split(",");
//        for (String s : text_split){
//            s=s.trim();
//            if (!s.equals(""))
//            {
//                wordMap_phrase.putIfAbsent(s, new ArrayList<Integer>() {{
//                    add(0);
//                    add(0);
//                }}); // For initial insertion
//                String temp=s;
//                wordMap_phrase.put(s, new ArrayList<>() {{
//                    add(wordMap_phrase.get(temp).get(0) + weight);
//                    add(wordMap_phrase.get(temp).get(1) + 1);
//                }}); // Increase weight & TF of each word
//            }
//        }
//    }
//    public static void optimize_Phrase (){
//        for (Map.Entry<String,List<Integer>> entry : wordMap_phrase.entrySet()) {
//            for (Map.Entry<String, List<Integer>> entry2 : wordMap_phrase.entrySet()) {
//                if (entry == entry2)
//                    continue;
//                if (entry2.getKey().contains(entry.getKey())) {
//                    wordMap_phrase.put(entry.getKey(), new ArrayList<>() {{
//                        add(entry.getValue().get(0) + entry2.getValue().get(0));
//                        add(entry.getValue().get(1) + entry2.getValue().get(1));
//                    }}); // Increase weight & TF of each word
//                }
//            }
//        }
//    }
public static void main(String[] args) {
    String[]word = new String[0];
    phase(word);
}
    public static void phase( String[]words)
    {
        int target =words.length-2;// delete "";
        for(int i=1;i<words.length-1;i++)
        {
            String lowerCaseString=words[i].toLowerCase();
          List<Document>docs=(List<Document>)db.getAttr("words","word",lowerCaseString,"urls");
            for (Document object :  docs) {
                String url = (String) object.get("url");
                String positions = "positions";
                ArrayList<Integer> post =(ArrayList<Integer>) object.get("positions");
                count.putIfAbsent(url, 0);
                count.put(url,count.get(url) +1 );
                indx.get(url).add(post);
                if( target==count.get(url)){
                    URL.putIfAbsent(url, 0);
                }
            }
        }
        check();
    }
    public static void check (){
        for (Map.Entry<String, Integer> entry : URL.entrySet()) {
            ArrayList<Integer> first =indx.get(entry.getKey()).get(0);
            int n=indx.get(entry.getKey()).size();
            int count=0;
            boolean flag=true;
            for(int i=0;i<first.size();i++)
            {
                for(int j=1;j<n;j++){
                    if(!indx.get(entry.getKey()).get(j).contains(indx.get(entry.getKey()).get(0).get(i)+j)){
                        flag= false;
                        break;
                    }
                }
                if(flag)
                    count++;
            }
            URL.put(entry.getKey(),count );
        }
    }
}

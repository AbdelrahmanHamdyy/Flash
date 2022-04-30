package com.company;

import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.company.Indexer.*;

public class Phrase_Searching {

    private static String[] Phrase_StopWords (String[] M){
        for(String word : stopWords) {
            for (int i = 0; i < M.length; i++) {
                if (M[i].toLowerCase(Locale.ROOT).equals(word))
                    M[i] = " , ";
            }
        }
        return M;
    }
    public static void Phrase_Searching (String tag, Document doc, int weight){

        String temp_text="";
        for(int i=0;i<doc.select(tag).size() ;i++) {
            temp_text+=doc.select(tag).get(i).text();
            temp_text+=" , ";
        }
        temp_text = temp_text.replaceAll("[^a-zA-Z0-9\\s]"," , ").toLowerCase(Locale.ROOT);
        String[]  text_split;
        text_split= temp_text.split(" ");
        text_split = Phrase_StopWords(text_split);
        temp_text = String.join(" ",text_split);
        text_split= temp_text.split(",");
        text_split= temp_text.split(",");
        for (String s : text_split){
            s=s.trim();
            if (!s.equals(""))
            {
                wordMap_phrase.putIfAbsent(s, new ArrayList<Integer>() {{
                    add(0);
                    add(0);
                }}); // For initial insertion
                String temp=s;
                wordMap_phrase.put(s, new ArrayList<>() {{
                    add(wordMap_phrase.get(temp).get(0) + weight);
                    add(wordMap_phrase.get(temp).get(1) + 1);
                }}); // Increase weight & TF of each word
            }
        }
    }
    public static void optimize_Phrase (){
        for (Map.Entry<String,List<Integer>> entry : wordMap_phrase.entrySet()) {
            for (Map.Entry<String, List<Integer>> entry2 : wordMap_phrase.entrySet()) {
                if (entry == entry2)
                    continue;
                if (entry2.getKey().contains(entry.getKey())) {
                    wordMap_phrase.put(entry.getKey(), new ArrayList<>() {{
                        add(entry.getValue().get(0) + entry2.getValue().get(0));
                        add(entry.getValue().get(1) + entry2.getValue().get(1));
                    }}); // Increase weight & TF of each word
                }
            }
        }
    }
}

package com.company;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class CompactString {
    ArrayList<String> String_compact=new ArrayList<String>();
    public String String_Compact(Document doc){
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
//        if(String_compact.contains(compact)) {
//            System.out.println("SPyyyyyyyyyyyyyyyyyyyyyyyyyy");
//            return null;
//        }
        return compact;
    }
}

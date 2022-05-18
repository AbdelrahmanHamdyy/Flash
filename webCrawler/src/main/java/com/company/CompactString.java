package com.company;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class CompactString {
    ArrayList<String> String_compact = new ArrayList<String>();
    int count_string = 0;

    public String String_Compact(Document doc){
        count_string = 0;
        String compact = "";
        String title = doc.title();
        compact+= comp ("p", doc);
        if(count_string != 50) {
            compact+= comp ("div", doc);
        }
      //  System.out.println(compact);
        return compact;
    }
    private String comp (String tag,Document doc ) {
        String compact = "";
        String temp_text =doc.select(tag).text();
        String[] span_text_split = temp_text.split(" ");
       // System.out.println(temp_text);

        for(int i = 0; i < span_text_split.length; i++) {
            span_text_split[i]= span_text_split[i].replaceAll("[^a-zA-Z]","");
            if (!span_text_split[i].equals("")) {
                compact += span_text_split[i].charAt(0);
                count_string++;
                if(count_string==50)
                    break;
            }
        }

        return compact;
    }
}

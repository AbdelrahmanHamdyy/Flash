package com.company;
import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<mtWebCrawler> bots = new ArrayList<>();
        bots.add(new mtWebCrawler("https://abcnews.go.com", 1));
        //bots.add(new mtWebCrawler("https://www.npr.org", 2));
        //bots.add(new mtWebCrawler("https://www.nytimes.com", 3));
        for (mtWebCrawler w : bots) {
            try {
                w.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
      /* String name="vcAs";
        if(name.matches("[a-zA-Z]+")) {
            System.out.println("Bot ID:");
        }
        else
            System.out.println("hhhh ID:");*/

    }


}

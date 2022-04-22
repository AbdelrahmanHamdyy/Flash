package com.company;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //DB.db_connect();
        Scanner cin =new Scanner(System.in);
        int numOfTreads=cin.nextInt();
        System.out.println(numOfTreads);
        Crawler myCrawler =new Crawler("https://www.webharvy.com/",numOfTreads);
        //https://www.cyotek.com/cyotek-webcopy
        //
//        DB db = new DB();
//        ArrayList<String> keys=new ArrayList<String>();
//        ArrayList<Object>values=new ArrayList<Object>();
//        keys.add("url");values.add("https://www.baeldung.com/java-mongodb");
//        keys.add("id");values.add(0);
//        keys.add("CompactString");values.add("whehghgh");
//        db.insertToDB("URLs",keys,values);
    }
}

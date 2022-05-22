package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class insertion {
    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("seedSet.txt");
        Scanner cin =new Scanner(myFile);
        DB db=new DB();
        while(cin.hasNext())
        {
            String s=cin.nextLine();
            String name=cin.nextLine();
            ArrayList<String>keys=new ArrayList<String>();
            ArrayList<Object>values=new ArrayList<Object>();
            keys.add("url");
            values.add(s);
            db.insertToDB("CrawlerLinks",keys,values);
        }
    }


}

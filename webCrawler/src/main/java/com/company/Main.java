package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void test(ArrayList<Integer> l)
    {
        l=new ArrayList<>();
        l.add(1);
    }

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int numOfTreads = cin.nextInt();
        System.out.println(numOfTreads);
        Crawler myCrawler = new Crawler(numOfTreads);
        //https://www.cyotek.com/cyotek-webcopy
//        ArrayList<Integer> l=null;
//        test(l);
//        System.out.println(l.get(0));
    }
}

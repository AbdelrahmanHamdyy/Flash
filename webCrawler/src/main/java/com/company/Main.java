package com.company;

import java.util.*;

public class Main {
    public static void test(ArrayList<Integer> l)
    {
        l=new ArrayList<>();
        l.add(1);
    }


    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        System.out.print("Number of Threads: ");
        int numOfTreads = cin.nextInt();
        Crawler myCrawler = new Crawler(numOfTreads);
    }
}

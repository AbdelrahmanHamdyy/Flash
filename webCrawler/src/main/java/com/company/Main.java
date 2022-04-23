package com.company;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int numOfTreads = cin.nextInt();
        System.out.println(numOfTreads);
        Crawler myCrawler = new Crawler("https://www.cyotek.com/cyotek-webcopy", numOfTreads);
        //https://www.cyotek.com/cyotek-webcopy
    }
}

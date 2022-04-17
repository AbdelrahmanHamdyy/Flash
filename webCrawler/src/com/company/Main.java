package com.company;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner cin =new Scanner(System.in);
        int numOfTreads=cin.nextInt();
        System.out.println(numOfTreads);
        Crawler myCrawler =new Crawler("https://www.youtube.com/watch?v=KZK5rnxBWcU",numOfTreads);

    }

}

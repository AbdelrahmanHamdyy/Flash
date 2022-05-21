package com.company;

import java.io.IOException;
import java.util.Scanner;

public class IndexerMain {
    public static void main(String[] args) throws IOException {
        Scanner Input = new Scanner(System.in);
        System.out.print("Number of Threads: ");
        int Threads = Input.nextInt();
        Indexer index = new Indexer(Threads);
    }
}

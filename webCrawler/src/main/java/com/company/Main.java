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
        int numOfTreads = cin.nextInt();
        System.out.println(numOfTreads);
        Crawler myCrawler = new Crawler(numOfTreads);
        //https://www.cyotek.com/cyotek-webcopy
//        ArrayList<Integer> l=null;
//        test(l);
//        System.out.println(l.get(0));
//        String[]words=new String[2];
//        HashMap<String,Integer>IDF=new HashMap<String,Integer>();
//        words[0]="adham";IDF.put("adham",1);
//        words[1]="mohamed";IDF.put("mohamed",2);
//        Arrays.sort(words, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                if (IDF.containsKey(o1) && IDF.containsKey(o2))
//                    return IDF.get(o2)-IDF.get(o1);
//                return 0;
//            }
//        });
//        System.out.println(words[0]);
//        System.out.println(words[1]);
    }
}

package com.company;

import java.util.ArrayList;

public class IndexerObject {
    public int TF = 0; // Not stored in the db but used to calculate TF-IDF
    public int Weight = 0;
    public int paragraphID = -1;
    public ArrayList<Integer> positions = new ArrayList<>();
}

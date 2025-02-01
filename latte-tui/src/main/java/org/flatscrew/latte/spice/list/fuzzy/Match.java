package org.flatscrew.latte.spice.list.fuzzy;

import java.util.List;

public class Match {
    private String str;
    private int index;
    private List<Integer> matchedIndexes;
    private int score;

    public Match(String str, int index, List<Integer> matchedIndexes, int score) {
        this.str = str;
        this.index = index;
        this.matchedIndexes = matchedIndexes;
        this.score = score;
    }

    public String getStr() {
        return str;
    }

    public int getIndex() {
        return index;
    }

    public List<Integer> getMatchedIndexes() {
        return matchedIndexes;
    }

    public int getScore() {
        return score;
    }
}